package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EnderiteBow extends BowItem {

    public EnderiteBow(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {

            /* New Code */
            boolean bl = canUseWithoutArrow(stack, playerEntity);
            /////////////

            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (bl && itemStack.isEmpty()) {
                // Fake stack, if it can use Enderite Bow without arrow
                itemStack = Items.ARROW.getDefaultStack().copyWithCount(64);
            }
            if (!itemStack.isEmpty()) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double) f < 0.1)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    int proj_count = playerEntity.getWorld() instanceof ServerWorld serverWorld ? EnchantmentHelper.getProjectileCount(serverWorld, stack, playerEntity, 1) : 1;
                    if (bl) {
                        // If can use Enderite Bow without Arrow, then fill up projectiles list
                        for (int proj_idx = 0; proj_idx < proj_count - list.size(); proj_idx++) {
                            list.add(Items.ARROW.getDefaultStack());
                        }
                    }
                    if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                        this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list,
                            /* New Code */
                            f * this.getSpeedMultiplier(),
                            //////////////
                            1.0F, f == 1.0F, null);
                    }

                    world.playSound(
                        null,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void shootAll(
        ServerWorld world,
        LivingEntity shooter,
        Hand hand,
        ItemStack stack,
        List<ItemStack> projectiles,
        float speed,
        float divergence,
        boolean critical,
        @Nullable LivingEntity target
    ) {
        float f = EnchantmentHelper.getProjectileSpread(world, stack, shooter, 0.0F);
        float g = projectiles.size() == 1 ? 0.0F : 2.0F * f / (float) (projectiles.size() - 1);
        float h = (float) ((projectiles.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectiles.size(); j++) {
            ItemStack itemStack = (ItemStack) projectiles.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float) ((j + 1) / 2) * g;
                i = -i;
                PersistentProjectileEntity projectileEntity = (PersistentProjectileEntity) this.createArrowEntity(world, shooter, stack, itemStack, critical);

                /* New Code */
                projectileEntity.setCustomName(Text.literal("Enderite Arrow"));

                projectileEntity.setDamage(this.getBaseDamage());

                if (shooter instanceof PlayerEntity && canUseWithoutArrow(stack, (PlayerEntity) shooter)) {
                    projectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
                /////////////

                this.shoot(shooter, projectileEntity, j, speed, divergence, k, target);
                world.spawnEntity(projectileEntity);
                stack.damage(this.getWeaponStackDamage(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
    }

    protected static ItemStack getProjectile(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot) {
        boolean no_ammo_use = shooter instanceof PlayerEntity && canUseWithoutArrow(stack, (PlayerEntity) shooter);
        int i = !multishot && !shooter.isInCreativeMode() && shooter.getWorld() instanceof ServerWorld serverWorld && !no_ammo_use
            ? EnchantmentHelper.getAmmoUse(serverWorld, stack, projectileStack, 1)
            : 0;
        if (i > projectileStack.getCount()) {
            return ItemStack.EMPTY;
        } else if (i == 0) {
            ItemStack itemStack = projectileStack.copyWithCount(1);
            itemStack.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            return itemStack;
        } else {
            ItemStack itemStack = projectileStack.split(i);
            if (projectileStack.isEmpty() && shooter instanceof PlayerEntity playerEntity) {
                playerEntity.getInventory().removeOne(projectileStack);
            }

            return itemStack;
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / EnderiteMod.CONFIG.tools.enderiteBowChargeTime;
        ;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        if (!bl && !canUseWithoutArrow(itemStack, user)) {
            return ActionResult.FAIL;
        } else {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME.withNewHandStack(itemStack);
        }
    }

    private static boolean canUseWithoutArrow(ItemStack bow, PlayerEntity user) {
        var inf_enchant = user.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.INFINITY.getValue());
        return user.getAbilities().creativeMode || !EnderiteMod.CONFIG.tools.enderiteBowNeedsArrow
            || (!EnderiteMod.CONFIG.tools.enderiteBowWithInfinityNeedsArrow && inf_enchant.isPresent() && EnchantmentHelper.getLevel(inf_enchant.get(), bow) > 0);
    }

    public float getBaseDamage() {
        return EnderiteMod.CONFIG.tools.enderiteBowAD;
    }

    public float getSpeedMultiplier() {
        return EnderiteMod.CONFIG.tools.enderiteBowArrowSpeed;
    }
}
