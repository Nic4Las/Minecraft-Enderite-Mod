package net.enderitemc.enderitemod.tools;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.misc.EnderiteCrossbowUser;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class EnderiteCrossbow extends CrossbowItem {
    private boolean charged = false;
    private boolean loaded = false;

    public EnderiteCrossbow(Item.Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return CROSSBOW_HELD_PROJECTILES;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (isCharged(itemStack)) {
            shootAll(world, user, hand, itemStack, getSpeed(itemStack), 1.0F);
            setCharged(itemStack, false);
            return TypedActionResult.consume(itemStack);
        } else if (!user.getArrowType(itemStack).isEmpty()) {
            if (!isCharged(itemStack)) {
                this.charged = false;
                this.loaded = false;
                user.setCurrentHand(hand);
            }

            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getPullProgress(i, stack);
        if (f >= 1.0F && !isCharged(stack) && loadProjectiles(user, stack)) {
            setCharged(stack, true);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0F,
                    1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    private static boolean loadProjectiles(LivingEntity shooter, ItemStack projectile) {
        int i = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, projectile);
        int j = i == 0 ? 1 : 3;
        boolean bl = shooter instanceof PlayerEntity && ((PlayerEntity) shooter).getAbilities().creativeMode;
        ItemStack itemStack = shooter.getArrowType(projectile);
        ItemStack itemStack2 = itemStack.copy();

        for (int k = 0; k < j; ++k) {
            if (k > 0) {
                itemStack = itemStack2.copy();
            }

            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(Items.ARROW);
                itemStack2 = itemStack.copy();
            }

            if (!loadProjectile(shooter, projectile, itemStack, k > 0, bl)) {
                return false;
            }
        }

        return true;
    }

    private static boolean loadProjectile(LivingEntity shooter, ItemStack crossbow, ItemStack projectile,
            boolean simulated, boolean creative) {
        if (projectile.isEmpty()) {
            return false;
        } else {
            boolean bl = creative && projectile.getItem() instanceof ArrowItem;
            ItemStack itemStack2;
            if (!bl && !creative && !simulated) {
                itemStack2 = projectile.split(1);
                if (projectile.isEmpty() && shooter instanceof PlayerEntity) {
                    ((PlayerEntity) shooter).getInventory().removeOne(projectile);
                }
            } else {
                itemStack2 = projectile.copy();
            }

            putProjectile(crossbow, itemStack2);
            return true;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        NbtCompound compoundTag = stack.getNbt();
        return compoundTag != null && compoundTag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean charged) {
        NbtCompound compoundTag = stack.getOrCreateNbt();
        compoundTag.putBoolean("Charged", charged);
    }

    private static void putProjectile(ItemStack crossbow, ItemStack projectile) {
        NbtCompound compoundTag = crossbow.getOrCreateNbt();
        NbtList listTag2;
        if (compoundTag.contains("ChargedProjectiles", 9)) {
            listTag2 = compoundTag.getList("ChargedProjectiles", 10);
        } else {
            listTag2 = new NbtList();
        }

        NbtCompound compoundTag2 = new NbtCompound();
        projectile.writeNbt(compoundTag2);
        listTag2.add(compoundTag2);
        compoundTag.put("ChargedProjectiles", listTag2);
    }

    private static List<ItemStack> getProjectiles(ItemStack crossbow) {
        List<ItemStack> list = Lists.newArrayList();
        NbtCompound compoundTag = crossbow.getNbt();
        if (compoundTag != null && compoundTag.contains("ChargedProjectiles", 9)) {
            NbtList listTag = compoundTag.getList("ChargedProjectiles", 10);
            if (listTag != null) {
                for (int i = 0; i < listTag.size(); ++i) {
                    NbtCompound compoundTag2 = listTag.getCompound(i);
                    list.add(ItemStack.fromNbt(compoundTag2));
                }
            }
        }

        return list;
    }

    private static void clearProjectiles(ItemStack crossbow) {
        NbtCompound compoundTag = crossbow.getNbt();
        if (compoundTag != null) {
            NbtList listTag = compoundTag.getList("ChargedProjectiles", 9);
            listTag.clear();
            compoundTag.put("ChargedProjectiles", listTag);
        }

    }

    public static boolean hasProjectile(ItemStack crossbow, Item projectile) {
        return getProjectiles(crossbow).stream().anyMatch((s) -> {
            return s.getItem() == projectile;
        });
    }

    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile,
            float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (!world.isClient) {
            boolean bl = projectile.getItem() == Items.FIREWORK_ROCKET;
            Object projectileEntity2;
            if (bl) {
                projectileEntity2 = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(),
                        shooter.getEyeY() - 0.15000000596046448D, shooter.getZ(), true);
            } else {
                projectileEntity2 = createArrow(world, shooter, crossbow, projectile);
                if (creative || simulated != 0.0F) {
                    ((PersistentProjectileEntity) projectileEntity2).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof EnderiteCrossbowUser) {
                EnderiteCrossbowUser crossbowUser = (EnderiteCrossbowUser) shooter;
                crossbowUser.shoot(crossbowUser.getTarget(), crossbow, (ProjectileEntity) projectileEntity2, simulated);
            } else {
                Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vec3f(vec3d), simulated, true);
                Vec3d vec3d2 = shooter.getRotationVec(1.0F);
                Vec3f vector3f = new Vec3f(vec3d2);
                vector3f.rotate(quaternion);
                ((ProjectileEntity) projectileEntity2).setVelocity((double) vector3f.getX(), (double) vector3f.getY(),
                        (double) vector3f.getZ(), speed, divergence);
            }

            crossbow.damage(bl ? 3 : 1, shooter, (e) -> {
                e.sendToolBreakStatus(hand);
            });
            world.spawnEntity((Entity) projectileEntity2);
            world.playSound((PlayerEntity) null, shooter.getX(), shooter.getY(), shooter.getZ(),
                    SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    private static PersistentProjectileEntity createArrow(World world, LivingEntity entity, ItemStack crossbow,
            ItemStack arrow) {
        ArrowItem arrowItem = (ArrowItem) ((ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem()
                : Items.ARROW));
        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, arrow, entity);

        persistentProjectileEntity.setCustomName(Text.literal("Enderite Arrow"));

        if (entity instanceof PlayerEntity) {
            persistentProjectileEntity.setCritical(true);
        }

        persistentProjectileEntity.setDamage(EnderiteMod.CONFIG.tools.enderiteCrossbowAD);

        persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        persistentProjectileEntity.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, crossbow);
        if (i > 0) {
            persistentProjectileEntity.setPierceLevel((byte) i);
        }

        return persistentProjectileEntity;
    }

    public static void shootAll(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed,
            float divergence) {
        List<ItemStack> list = getProjectiles(stack);
        float[] fs = EnderiteCrossbow.getSoundPitches(entity.getRandom());

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = (ItemStack) list.get(i);
            boolean bl = entity instanceof PlayerEntity && ((PlayerEntity) entity).getAbilities().creativeMode;
            if (!itemStack.isEmpty()) {
                if (i == 0) {
                    shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 0.0F);
                } else if (i == 1) {
                    shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, -10.0F);
                } else if (i == 2) {
                    shoot(world, entity, hand, stack, itemStack, fs[i], bl, speed, divergence, 10.0F);
                }
            }
        }

        postShoot(world, entity, stack);
    }

    private static float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[] { 1.0f, getSoundPitch(bl, random), getSoundPitch(!bl, random) };
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63f : 0.43f;
        return 1.0f / (random.nextFloat() * 0.5f + 1.8f) + f;
    }

    private static void postShoot(World world, LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
            if (!world.isClient) {
                Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
            }

            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (stack.getMaxUseTime() - remainingUseTicks) / (float) getPullTime(stack);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), soundEvent,
                        SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(), soundEvent2,
                        SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

        }

    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return getPullTime(stack) + 30;
    }

    public static int getPullTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 35 : 35 - 5 * i;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.ITEM_CROSSBOW_LOADING_START;
        }
    }

    private static float getPullProgress(int useTicks, ItemStack stack) {
        float f = (float) useTicks / (float) getPullTime(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        List<ItemStack> list = getProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemStack = (ItemStack) list.get(0);
            tooltip.add((Text.translatable("item.minecraft.crossbow.projectile")).append(" ")
                    .append(itemStack.toHoverableText()));
            if (context.isAdvanced() && itemStack.getItem() == Items.FIREWORK_ROCKET) {
                List<Text> list2 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendTooltip(itemStack, world, list2, context);
                if (!list2.isEmpty()) {
                    for (int i = 0; i < list2.size(); ++i) {
                        list2.set(i, (Text.literal("  ")).append((Text) list2.get(i)).formatted(Formatting.GRAY));
                    }

                    tooltip.addAll(list2);
                }
            }

        }
    }

    private static float getSpeed(ItemStack stack) {
        return stack.getItem() == EnderiteMod.ENDERITE_CROSSBOW && hasProjectile(stack, Items.FIREWORK_ROCKET) ? 2.1F
                : EnderiteMod.CONFIG.tools.enderiteCrossbowArrowSpeed;
    }

    @Override
    public int getRange() {
        return 8;
    }

    @Override
    public int getEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }
}
