package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class EnderiteCrossbow extends CrossbowItem {
    private boolean charged = false;
    private boolean loaded = false;

    private static final CrossbowItem.ChargingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.ChargingSounds(
        Optional.of(SoundEvents.CROSSBOW_LOADING_START),
        Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE),
        Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    public EnderiteCrossbow(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        ChargedProjectiles chargedProjectilesComponent = itemStack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.performShooting(world, user, hand, itemStack, EnderiteCrossbow.getShootingPower(chargedProjectilesComponent), 1.0f, null);
            return InteractionResult.CONSUME.heldItemTransformedTo(itemStack);
        } else if (!user.getProjectile(itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            user.startUsingItem(hand);
            return InteractionResult.CONSUME.heldItemTransformedTo(itemStack);
        } else {
            return InteractionResult.FAIL;
        }
    }

    private static float getShootingPower(ChargedProjectiles stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 2.1F
            : EnderiteMod.CONFIG.tools.enderiteCrossbowArrowSpeed;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        int i = this.getUseDuration(stack, user) - remainingUseTicks;
        float f = getPowerForTime(i, stack, user);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(user, stack)) {
            CrossbowItem.ChargingSounds loadingSounds = this.getChargingSounds(stack);
            loadingSounds.end()
                .ifPresent(
                    sound -> world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        (SoundEvent) sound.value(),
                        user.getSoundSource(),
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                    )
                );
            return true;
        }
        return false;
    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        List<ItemStack> list = draw(crossbow, shooter.getProjectile(crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(list));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d = target.getX() - shooter.getX();
            double e = target.getZ() - shooter.getZ();
            double f = Math.sqrt(d * d + e * e);
            double g = target.getY(0.3333333333333333) - projectile.getY() + f * (double) 0.2f;
            vector3f = EnderiteCrossbow.getProjectileShotVector(shooter, new Vec3(d, g, e), yaw);
        } else {
            Vec3 vec3d = shooter.getUpVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double) (yaw * ((float) Math.PI / 180)), vec3d.x, vec3d.y, vec3d.z);
            Vec3 vec3d2 = shooter.getViewVector(1.0f);
            vector3f = vec3d2.toVector3f().rotate(quaternionf);
        }
        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        float h = EnderiteCrossbow.getShotPitch(shooter.getRandom(), index);
        shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, shooter.getSoundSource(), 1.0f, h);
    }

    private static Vector3f getProjectileShotVector(LivingEntity shooter, Vec3 direction, float yaw) {
        Vector3f vector3f = direction.toVector3f().normalize();
        Vector3f vector3f2 = new Vector3f(vector3f).cross(new Vector3f(0.0f, 1.0f, 0.0f));
        if ((double) vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3 vec3d = shooter.getUpVector(1.0f);
            vector3f2 = new Vector3f(vector3f).cross(vec3d.toVector3f());
        }
        Vector3f vector3f3 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f2.x, vector3f2.y, vector3f2.z);
        return new Vector3f(vector3f).rotateAxis(yaw * ((float) Math.PI / 180.0f), vector3f3.x, vector3f3.y, vector3f3.z);
    }

    @Override
    protected Projectile createProjectile(Level world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        Projectile weakArrow = super.createProjectile(world, shooter, weaponStack, projectileStack, critical);
        if (weakArrow instanceof AbstractArrow projectileEntity) {
            projectileEntity.setCustomName(Component.literal("Enderite Arrow"));
            projectileEntity.setBaseDamage(EnderiteMod.CONFIG.tools.enderiteCrossbowAD);
            return projectileEntity;
        }
        return weakArrow;
    }

    private static float getShotPitch(RandomSource random, int index) {
        if (index == 0) {
            return 1.0f;
        }
        return EnderiteCrossbow.getRandomShotPitch((index & 1) == 1, random);
    }

    private static float getRandomShotPitch(boolean flag, RandomSource random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClientSide()) {
            CrossbowItem.ChargingSounds loadingSounds = this.getChargingSounds(stack);
            float f = (float)(stack.getUseDuration(user) - remainingUseTicks) / getChargeDuration(stack, user);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                loadingSounds.start()
                    .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid()
                    .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(user, stack)) {
                loadingSounds.end()
                    .ifPresent(
                        sound -> world.playSound(
                            null,
                            user.getX(),
                            user.getY(),
                            user.getZ(),
                            (SoundEvent)sound.value(),
                            user.getSoundSource(),
                            1.0F,
                            1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                        )
                    );
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return getChargeDuration(stack, user) + 3;
    }

    public static int getChargeDuration(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, user, 1.25F);
        return Mth.floor(f * EnderiteMod.CONFIG.tools.enderiteCrossBowChargeTime);
    }

    CrossbowItem.ChargingSounds getChargingSounds(ItemStack stack) {
        return (CrossbowItem.ChargingSounds) EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
            .orElse(DEFAULT_LOADING_SOUNDS);
    }

    private static float getPowerForTime(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float) useTicks / (float) EnderiteCrossbow.getChargeDuration(stack, user);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }
}
