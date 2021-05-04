package net.enderitemc.enderitemod.item;

import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EnderiteBow extends BowItem {

    public float speedMulitplier = 4.0f; // 3.0
    public double newBaseDamage = EnderiteModConfig.ENDERITE_BOW_BASE_DAMAGE.get(); // default: 1.5d
    public static float chargeTime = EnderiteModConfig.ENDERITE_BOW_CHARGE_TIME.get().floatValue(); // default: 30.0f,
                                                                                                    // in config: 30.0d

    public EnderiteBow(Properties builder) {
        super(builder);
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            boolean flag = playerentity.abilities.instabuild
                    || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i,
                    !itemstack.isEmpty() || flag);
            if (i < 0)
                return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = playerentity.abilities.instabuild || (itemstack.getItem() instanceof ArrowItem
                            && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isClientSide) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem
                                ? itemstack.getItem()
                                : Items.ARROW);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack,
                                playerentity);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F,
                                f * this.speedMulitplier, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setCritArrow(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                        if (j > 0) {
                            abstractarrowentity.setBaseDamage(
                                    abstractarrowentity.getBaseDamage() + (double) j * 0.5D + this.newBaseDamage);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                        if (k > 0) {
                            abstractarrowentity.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                            abstractarrowentity.setSecondsOnFire(100);
                        }

                        stack.hurtAndBreak(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
                        });
                        if (flag1 || playerentity.abilities.instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW
                                || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addFreshEntity(abstractarrowentity);
                    }

                    worldIn.playSound((PlayerEntity) null, playerentity.getX(), playerentity.getY(),
                            playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                            1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.removeItem(itemstack);
                        }
                    }

                    playerentity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float) charge / chargeTime;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

}