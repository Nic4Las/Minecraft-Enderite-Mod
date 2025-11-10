package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class EnderiteCrossbow extends CrossbowItem {
    private boolean charged = false;
    private boolean loaded = false;

    private static final CrossbowItem.LoadingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.LoadingSounds(
        Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
        Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
        Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
    );

    public EnderiteCrossbow(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ChargedProjectilesComponent chargedProjectilesComponent = itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.shootAll(world, user, hand, itemStack, EnderiteCrossbow.getSpeed(chargedProjectilesComponent), 1.0f, null);
            return ActionResult.CONSUME.withNewHandStack(itemStack);
        } else if (!user.getProjectileType(itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            user.setCurrentHand(hand);
            return ActionResult.CONSUME.withNewHandStack(itemStack);
        } else {
            return ActionResult.FAIL;
        }
    }

    private static float getSpeed(ChargedProjectilesComponent stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 2.1F
            : EnderiteMod.CONFIG.tools.enderiteCrossbowArrowSpeed;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float f = getPullProgress(i, stack, user);
        if (f >= 1.0F && !isCharged(stack) && loadProjectiles(user, stack)) {
            CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            loadingSounds.end()
                .ifPresent(
                    sound -> world.playSound(
                        null,
                        user.getX(),
                        user.getY(),
                        user.getZ(),
                        (SoundEvent) sound.value(),
                        user.getSoundCategory(),
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                    )
                );
            return true;
        }
        return false;
    }

    private static boolean loadProjectiles(LivingEntity shooter, ItemStack crossbow) {
        List<ItemStack> list = load(crossbow, shooter.getProjectileType(crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d = target.getX() - shooter.getX();
            double e = target.getZ() - shooter.getZ();
            double f = Math.sqrt(d * d + e * e);
            double g = target.getBodyY(0.3333333333333333) - projectile.getY() + f * (double) 0.2f;
            vector3f = EnderiteCrossbow.calcVelocity(shooter, new Vec3d(d, g, e), yaw);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double) (yaw * ((float) Math.PI / 180)), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0f);
            vector3f = vec3d2.toVector3f().rotate(quaternionf);
        }
        projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
        float h = EnderiteCrossbow.getSoundPitch(shooter.getRandom(), index);
        shooter.getEntityWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, shooter.getSoundCategory(), 1.0f, h);
    }

    private static Vector3f calcVelocity(LivingEntity shooter, Vec3d direction, float yaw) {
        Vector3f vector3f = direction.toVector3f().normalize();
        Vector3f vector3f2 = new Vector3f(vector3f).cross(new Vector3f(0.0f, 1.0f, 0.0f));
        if ((double) vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0f);
            vector3f2 = new Vector3f(vector3f).cross(vec3d.toVector3f());
        }
        Vector3f vector3f3 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f2.x, vector3f2.y, vector3f2.z);
        return new Vector3f(vector3f).rotateAxis(yaw * ((float) Math.PI / 180.0f), vector3f3.x, vector3f3.y, vector3f3.z);
    }

    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ProjectileEntity weakArrow = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
        if (weakArrow instanceof PersistentProjectileEntity projectileEntity) {
            projectileEntity.setCustomName(Text.literal("Enderite Arrow"));
            projectileEntity.setDamage(EnderiteMod.CONFIG.tools.enderiteCrossbowAD);
            return projectileEntity;
        }
        return weakArrow;
    }

    private static float getSoundPitch(Random random, int index) {
        if (index == 0) {
            return 1.0f;
        }
        return EnderiteCrossbow.getSoundPitch((index & 1) == 1, random);
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient()) {
            CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            float f = (float)(stack.getMaxUseTime(user) - remainingUseTicks) / getPullTime(stack, user);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                loadingSounds.start()
                    .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid()
                    .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 1.0F && !isCharged(stack) && loadProjectiles(user, stack)) {
                loadingSounds.end()
                    .ifPresent(
                        sound -> world.playSound(
                            null,
                            user.getX(),
                            user.getY(),
                            user.getZ(),
                            (SoundEvent)sound.value(),
                            user.getSoundCategory(),
                            1.0F,
                            1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                        )
                    );
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return getPullTime(stack, user) + 3;
    }

    public static int getPullTime(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.getCrossbowChargeTime(stack, user, 1.25F);
        return MathHelper.floor(f * EnderiteMod.CONFIG.tools.enderiteCrossBowChargeTime);
    }

    CrossbowItem.LoadingSounds getLoadingSounds(ItemStack stack) {
        return (CrossbowItem.LoadingSounds) EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS)
            .orElse(DEFAULT_LOADING_SOUNDS);
    }

    private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float) useTicks / (float) EnderiteCrossbow.getPullTime(stack, user);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }
}
