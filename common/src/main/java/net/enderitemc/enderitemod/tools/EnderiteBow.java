package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import java.util.List;

public class EnderiteBow extends BowItem {

    public EnderiteBow(Item.Properties settings) {
        super(settings);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {

            /* New Code */
            boolean bl = canUseWithoutArrow(stack, playerEntity);
            /////////////

            ItemStack itemStack = playerEntity.getProjectile(stack);
            if (bl && itemStack.isEmpty()) {
                // Fake stack, if it can use Enderite Bow without arrow
                itemStack = Items.ARROW.getDefaultInstance().copyWithCount(64);
                itemStack.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), new EnderiteChargeComponent(1337));
            }
            if (!itemStack.isEmpty()) {
                int i = this.getUseDuration(stack, user) - remainingUseTicks;
                float f = getPowerForTime(i);
                if (!((double) f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemStack, playerEntity);
                    int proj_count = playerEntity.level() instanceof ServerLevel serverWorld ? EnchantmentHelper.processProjectileCount(serverWorld, stack, playerEntity, 1) : 1;
                    if (bl) {
                        // If can use Enderite Bow without Arrow, then fill up projectiles list
                        for (int proj_idx = 0; proj_idx < proj_count - list.size(); proj_idx++) {
                            list.add(Items.ARROW.getDefaultInstance());
                        }
                    }
                    if (world instanceof ServerLevel serverWorld && !list.isEmpty()) {
                        this.shoot(serverWorld, playerEntity, playerEntity.getUsedItemHand(), stack, list,
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
                        SoundEvents.ARROW_SHOOT,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        }
        return false;
    }

    protected Projectile createProjectile(Level world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ArrowItem arrowItem2 = projectileStack.getItem() instanceof ArrowItem arrowItem ? arrowItem : (ArrowItem)Items.ARROW;
        AbstractArrow persistentProjectileEntity = arrowItem2.createArrow(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritArrow(true);
        }
        /* New Code */
        persistentProjectileEntity.setCustomName(Component.literal("Enderite Arrow"));
        persistentProjectileEntity.setBaseDamage(this.getBaseDamage());
        if (projectileStack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), new EnderiteChargeComponent(0)).charge() == 1337) {
            // Make fake stack unpickupable
            projectileStack.remove(EnderiteDataComponents.TELEPORT_CHARGE.get());
            persistentProjectileEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        //////////////
        return persistentProjectileEntity;
    }

    public static float getPowerForTime(int useTicks) {
        float f = (float) useTicks / EnderiteMod.CONFIG.tools.enderiteBowChargeTime;
        ;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        boolean bl = !user.getProjectile(itemStack).isEmpty();
        if (!bl && !canUseWithoutArrow(itemStack, user)) {
            return InteractionResult.FAIL;
        } else {
            user.startUsingItem(hand);
            return InteractionResult.CONSUME.heldItemTransformedTo(itemStack);
        }
    }

    private static boolean canUseWithoutArrow(ItemStack bow, Player user) {
        var inf_enchant = user.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.INFINITY.identifier());
        return user.getAbilities().instabuild || !EnderiteMod.CONFIG.tools.enderiteBowNeedsArrow
            || (!EnderiteMod.CONFIG.tools.enderiteBowWithInfinityNeedsArrow && inf_enchant.isPresent() && EnchantmentHelper.getItemEnchantmentLevel(inf_enchant.get(), bow) > 0);
    }

    public float getBaseDamage() {
        return EnderiteMod.CONFIG.tools.enderiteBowAD;
    }

    public float getSpeedMultiplier() {
        return EnderiteMod.CONFIG.tools.enderiteBowArrowSpeed;
    }
}
