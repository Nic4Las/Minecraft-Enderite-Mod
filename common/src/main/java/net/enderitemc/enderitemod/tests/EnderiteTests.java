package net.enderitemc.enderitemod.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EnderiteTests {
    public static final String TMPL_MI = EnderiteMod.MOD_ID + ":";
    public static final String TMPL_NS = "enderitetests.";
    public static final String TMPL_PRE = TMPL_MI + TMPL_NS;

    @GameTest(templateName = TMPL_PRE + "explode_enderite_ore")
    public static void explodeEnderiteOreTest(TestContext ctx) {
        ctx.spawnEntity(EntityType.TNT, new BlockPos(1, 1, 0));
        ctx.waitAndRun(99, () -> ctx.checkBlock(new BlockPos(1, 1, 1),
            (Block block) -> block.equals(EnderiteMod.CRACKED_ENDERITE_ORE.get()),
            "TnT didn't crack the enderite ore"
        ));
        ctx.expectBlockAtEnd(EnderiteMod.CRACKED_ENDERITE_ORE.get(), new BlockPos(1, 1, 1));
    }

    @GameTest(templateName = TMPL_PRE + "enderite_shulkerbox_recipe")
    public static void enderiteShulkerboxRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.CONTAINER);
            if (component != null) {
                ctx.assertTrue(component.copyFirstStack().isOf(Items.DIRT),
                    "No items found in Enderite Shulkerbox item");
            } else {
                ctx.assertTrue(false, "No Container Component in stack" + stack.toString());
            }
        });
    }

    @GameTest(templateName = TMPL_PRE + "enderite_elytra_recipe")
    public static void enderiteElytraRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.UNBREAKING.getValue());
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get()) == 3,
                    "Enchantment wrong on Enderite Elytra");
                var enchant1 = ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.PROTECTION.getValue());
                ctx.assertTrue(enchant1.isPresent() && component.getLevel(enchant1.get()) == 1,
                    "Enchantment wrong on Enderite Elytra");
            } else {
                ctx.assertTrue(false, "No Container Component in stack" + stack.toString());
            }
        });
    }

    @GameTest(templateName = TMPL_PRE + "enderite_shield_deco_recipe")
    public static void enderiteShieldDecoRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.UNBREAKING.getValue());
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get()) == 3,
                    "Enchantment wrong on decorated Enderite Shield");
            } else {
                ctx.assertTrue(false, "No Enchantment Component in stack" + stack.toString());
            }
        });
    }

    @GameTest(templateName = TMPL_PRE + "enderite_respawn_anchor")
    public static void enderiteRespawnAnchorTest(TestContext ctx) {
        BlockPos anchor_pos = new BlockPos(0, 1, 0);
        BlockPos dirt_pos = new BlockPos(1, 1, 0);

        // Check block entity
        BlockEntity be = ctx.getBlockEntity(anchor_pos);
        ctx.assertTrue(be instanceof EnderiteRespawnAnchorBlockEntity,
            "Enderite Respawn Anchor Block Entity is missing");
        ctx.expectBlock(Blocks.DIRT, dirt_pos);

        // Check charges
        PlayerEntity player = ctx.createMockPlayer(GameMode.SURVIVAL);
        player.setPosition(ctx.getAbsolute(Vec3d.ZERO));
        player.getInventory().insertStack(Items.ENDER_PEARL.getDefaultStack().copyWithCount(4));
        for (int expected_charge = 1; expected_charge <= 4; expected_charge++) {
            ctx.useBlock(anchor_pos, player);
            ctx.useStackOnBlock(player, Items.ENDER_PEARL.getDefaultStack(), anchor_pos, Direction.NORTH);
            ctx.assertEquals(ctx.getBlockState(anchor_pos).get(EnderiteRespawnAnchor.CHARGES),
                expected_charge,
                "Enderite Respawn anchor has wrong amount of charge");
        }

        ctx.useBlock(anchor_pos, player);
        if (EnderiteRespawnAnchor.isNether(ctx.getWorld())) {
            // End -> Check if there
            ctx.expectBlock(EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(), anchor_pos);
            ctx.expectBlock(Blocks.DIRT, dirt_pos);
        } else {
            // Not End -> Check if exploded
            ctx.dontExpectBlock(EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(), anchor_pos);
            ctx.dontExpectBlock(Blocks.DIRT, dirt_pos);
        }
        ctx.complete();
    }

    @GameTest(templateName = TMPL_PRE + "void_death_with_enderite")
    public static void voidDeathWithEnderiteTest(TestContext ctx) {
        Vec3d relative_pos = new Vec3d(0, 0, 0);
        Vec3d absolute_pos = ctx.getAbsolute(relative_pos);

        ItemEntity ie = ctx.spawnItem(EnderiteMod.ENDERITE_HELMET.get(),
            ctx.getRelative(new Vec3d(absolute_pos.getX(), ctx.getWorld().getBottomY() - 64, absolute_pos.getZ())));

        ctx.waitAndRun(1, () -> {
            ctx.expectEntityAround(
                EntityType.ITEM,
                ctx.getRelativePos(new BlockPos((int) absolute_pos.getX(), ctx.getWorld().getBottomY() + 10, (int) absolute_pos.getZ())),
                2);
            ie.kill(ctx.getWorld());
            ctx.complete();
        });
    }

    @GameTest(templateName = TMPL_PRE + "enderite_armor_trims")
    public static void enderiteArmorTrimsTest(TestContext ctx) {
        BlockPos pos1 = ctx.getAbsolutePos(new BlockPos(-1,0,-1));
        BlockPos pos2 = ctx.getAbsolutePos(new BlockPos(4,46,1));

        List<ArmorStandEntity> stands = ctx.getWorld().getNonSpectatingEntities(ArmorStandEntity.class, Box.enclosing(pos1, pos2));
        stands.forEach(Entity::discard);

        if(!ctx.getWorld().getClosestPlayer(TargetPredicate.createNonAttackable(), pos1.getX(), pos1.getY(), pos1.getZ()).getMainHandStack().getItem().equals(Items.ARMOR_STAND)) {
            ctx.complete();
            return;
        }

        BlockPos pos = ctx.getAbsolutePos(new BlockPos(0,3,0));
        ArrayList<Entity> armor_stands = new ArrayList<>();
        for(int i=0; i<4; i++) {
            pos = pos.offset(Direction.Axis.X, 1);
            Map<EquipmentSlot, Item> equip_map = new HashMap<>();
            Identifier material_id = Identifier.of(EnderiteMod.MOD_ID, "enderite");
            switch(i) {
                case 0: {
                    equip_map.put(EquipmentSlot.HEAD, EnderiteMod.ENDERITE_HELMET.get());
                    equip_map.put(EquipmentSlot.CHEST, EnderiteMod.ENDERITE_CHESTPLATE.get());
                    equip_map.put(EquipmentSlot.LEGS, EnderiteMod.ENDERITE_LEGGINGS.get());
                    equip_map.put(EquipmentSlot.FEET, EnderiteMod.ENDERITE_BOOTS.get());
                    break;
                }
                case 1: {
                    equip_map.put(EquipmentSlot.CHEST, EnderiteMod.ENDERITE_ELYTRA.get());
                    material_id = ArmorTrimMaterials.GOLD.getValue();
                    break;
                }
                case 2: {
                    equip_map.put(EquipmentSlot.CHEST, EnderiteMod.ENDERITE_ELYTRA_SEPERATED.get());
                    break;
                }
                case 3: {
                    equip_map.put(EquipmentSlot.HEAD, Items.GOLDEN_HELMET);
                    equip_map.put(EquipmentSlot.CHEST, Items.GOLDEN_CHESTPLATE);
                    equip_map.put(EquipmentSlot.LEGS, Items.GOLDEN_LEGGINGS);
                    equip_map.put(EquipmentSlot.FEET, Items.GOLDEN_BOOTS);
                    break;
                }
            }
            RegistryEntry<ArmorTrimMaterial> material = ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.TRIM_MATERIAL).getEntry(material_id).get();
            int idx = 0;
            for (RegistryEntry<ArmorTrimPattern> pattern: ctx.getWorld().getRegistryManager().getOrThrow(RegistryKeys.TRIM_PATTERN).getIndexedEntries()) {
                BlockPos new_pos = pos.up(idx);
                idx+=2;
                ArmorStandEntity e = new ArmorStandEntity(ctx.getWorld(), new_pos.getX(), new_pos.getY(), new_pos.getZ());
                int equip_idx = ctx.getWorld().getRandom().nextInt(equip_map.keySet().toArray().length);
                EquipmentSlot slot = equip_map.keySet().stream().toList().get(equip_idx);
                for(Map.Entry<EquipmentSlot, Item> entry: equip_map.entrySet()) {
                    ItemStack stack = entry.getValue().getDefaultStack();
                    stack.set(DataComponentTypes.TRIM, new ArmorTrim(material, pattern));
                    e.equipStack(entry.getKey(), stack);

                    if(entry.getKey().equals(slot)) {
                        e.equipStack(EquipmentSlot.MAINHAND, stack);
                        e.setShowArms(true);
                    }
                }
                e.setNoGravity(true);
                e.setHideBasePlate(true);
                armor_stands.add(e);
            }
        }
        ctx.getWorld().addEntities(armor_stands.stream());
        ctx.complete();
    }

    @GameTest(templateName = TMPL_PRE + "enderite_elytra_trim_recipe")
    public static void enderiteElytraTrimRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.TRIM);
            if (component != null) {
                String trim = component.material().getIdAsString();
                ctx.assertEquals(trim, "minecraft:gold", "Wrong trim was applied.");
            } else {
                ctx.assertTrue(false, "No Trim Component in stack" + stack.toString());
            }
        });
    }

    @GameTest(templateName = TMPL_PRE + "enderman_enderite")
    public static void endermanEnderiteTest(TestContext ctx) {
        ServerWorld world = ctx.getWorld();
        BlockPos pos = ctx.getAbsolutePos(new BlockPos(0,1,0));

        EndermanEntity enderman = new EndermanEntity(EntityType.ENDERMAN, world);
        enderman.setPosition(pos.toBottomCenterPos());
        world.spawnEntity(enderman);

        PlayerEntity player = ctx.createMockPlayer(GameMode.CREATIVE);

        PersistentProjectileEntity persistentProjectileEntity2 = ((ArrowItem)Items.ARROW)
            .createArrow(world, Items.ARROW.getDefaultStack(), player, EnderiteTools.ENDERITE_BOW.get().getDefaultStack());
        persistentProjectileEntity2.setCustomName(Text.literal("Enderite Arrow"));
        DamageSource source2 = world.getDamageSources().arrow(persistentProjectileEntity2, player);
        int damage = 10;
        enderman.damage(world, source2, damage);
        ctx.assertEquals(enderman.getHealth(), enderman.getMaxHealth() - damage, "Enderman not damaged from enderite arrow!");

        PersistentProjectileEntity persistentProjectileEntity = ((ArrowItem)Items.ARROW)
            .createArrow(world, Items.ARROW.getDefaultStack(), player, Items.BOW.getDefaultStack());
        DamageSource source = world.getDamageSources().arrow(persistentProjectileEntity, player);
        enderman.damage(world, source, damage);
        ctx.assertEquals(enderman.getHealth(), enderman.getMaxHealth() - damage, "Enderman damaged from default arrow!");

        enderman.discard();
        ctx.complete();
    }

    //public dispenserBehaviourTest, ore smelting recipe


    // HELPER
    public static void crafterRecipeTest(TestContext ctx, Consumer<ItemStack> stackVerifier) {
        ctx.pushButton(1, 1, 1);
        ctx.waitAndRun(15, () -> {
            BlockEntity be = ctx.getBlockEntity(new BlockPos(0, 1, 0));
            if (be instanceof ChestBlockEntity cbe) {
                ItemStack stack = cbe.getStack(0);
                stackVerifier.accept(stack);
            } else {
                ctx.assertTrue(false, "No Chest Block Entity found");
            }
            ctx.complete();
        });
    }
}
