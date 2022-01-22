package net.enderitemc.enderitemod.tools;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class EnderiteBow extends BowItem {

    public static float chargeTime = 30.0f;

    public EnderiteBow(Item.Settings settings) {
        super(settings);
        // this.addPropertyGetter(new Identifier("pull"), (itemStack_1, world_1,
        // livingEntity_1) -> {
        // if (livingEntity_1 == null) {
        // return 0.0F;
        // } else {
        // return !(livingEntity_1.getActiveItem().getItem() instanceof BowItem) ? 0.0F
        // : (float) (itemStack_1.getMaxUseTime() - livingEntity_1.getItemUseTimeLeft())
        // / settings.getTime();
        // }
        // });
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) user;
            boolean bl = playerEntity.getAbilities().creativeMode
                    || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getArrowType(stack);
            if (!itemStack.isEmpty() || bl) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int i = this.getMaxUseTime(stack) - remainingUseTicks;
                float f = getPullProgress(i);
                if ((double) f >= 0.1D) {
                    boolean bl2 = bl && itemStack.getItem() == Items.ARROW;
                    if (!world.isClient) {
                        ArrowItem arrowItem = (ArrowItem) ((ArrowItem) (itemStack.getItem() instanceof ArrowItem
                                ? itemStack.getItem()
                                : Items.ARROW));
                        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack,
                                playerEntity);

                        persistentProjectileEntity.setCustomName(new LiteralText("Enderite Arrow"));

                        persistentProjectileEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(),
                                0.0F, f * this.getSpeedMultiplier(), 1.0F);
                        if (f == 1.0F) {
                            persistentProjectileEntity.setCritical(true);
                        }

                        int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            persistentProjectileEntity.setDamage(
                                    persistentProjectileEntity.getDamage() + (double) j * 0.5D + this.getBaseDamage());
                        } else {
                            persistentProjectileEntity.setDamage(this.getBaseDamage());
                        }

                        int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            persistentProjectileEntity.setPunch(k);
                        }

                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            persistentProjectileEntity.setOnFireFor(100);
                        }

                        stack.damage(1, (LivingEntity) playerEntity, (Consumer) ((p) -> {
                            ((LivingEntity) p).sendToolBreakStatus(playerEntity.getActiveHand());
                        }));
                        if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.getItem() == Items.SPECTRAL_ARROW
                                || itemStack.getItem() == Items.TIPPED_ARROW)) {
                            persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }

                        world.spawnEntity(persistentProjectileEntity);
                    }

                    world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                            1.0F / (new Random().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / chargeTime;
        ;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getArrowType(itemStack).isEmpty();
        if (!user.getAbilities().creativeMode && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    public int getEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }

    public float getBaseDamage() {
        return EnderiteMod.CONFIG.tools.enderiteBowAD;
    }

    public float getSpeedMultiplier() {
        return EnderiteMod.CONFIG.tools.enderiteBowArrowSpeed;
    }
}
