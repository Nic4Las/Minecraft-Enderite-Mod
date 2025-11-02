package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

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
                itemStack.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), new EnderiteChargeComponent(1337));
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

    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ArrowItem arrowItem2 = projectileStack.getItem() instanceof ArrowItem arrowItem ? arrowItem : (ArrowItem)Items.ARROW;
        PersistentProjectileEntity persistentProjectileEntity = arrowItem2.createArrow(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }
        /* New Code */
        persistentProjectileEntity.setCustomName(Text.literal("Enderite Arrow"));
        persistentProjectileEntity.setDamage(this.getBaseDamage());
        if (projectileStack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), new EnderiteChargeComponent(0)).charge() == 1337) {
            // Make fake stack unpickupable
            projectileStack.remove(EnderiteDataComponents.TELEPORT_CHARGE.get());
            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        //////////////
        return persistentProjectileEntity;
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
