package net.enderitemc.enderitemod.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EnderiteTests {
    public static final String TMPL_MI = EnderiteMod.MOD_ID + ":";
    public static final String TMPL_NS = "enderitetests.";
    public static final String TMPL_PRE = TMPL_MI + TMPL_NS;

    public static void explodeEnderiteOreTest(GameTestHelper ctx) {
        ctx.spawn(EntityTypes.TNT, new BlockPos(1, 1, 0));
        ctx.runAfterDelay(99, () -> ctx.assertBlock(new BlockPos(1, 1, 1),
            (Block block) -> block.equals(EnderiteMod.CRACKED_ENDERITE_ORE.get()),
            (a) -> Component.nullToEmpty("TnT didn't crack the enderite ore")
        ));
        ctx.succeedWhenBlockPresent(EnderiteMod.CRACKED_ENDERITE_ORE.get(), new BlockPos(1, 1, 1));
    }

    public static void enderiteShulkerboxRecipeTest(GameTestHelper ctx) {
        Player player = ctx.makeMockPlayer(GameType.SURVIVAL);
        SmithingMenu menu = new SmithingMenu(0, player.getInventory());
        ItemStack original = Items.SHULKER_BOX.getDefaultInstance();
        original.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIRT, 32))));
        original.set(DataComponents.CUSTOM_NAME, Component.literal("Upgrade test"));
        menu.getSlot(SmithingMenu.BASE_SLOT).set(original);
        menu.getSlot(SmithingMenu.ADDITIONAL_SLOT).set(EnderiteMod.ENDERITE_INGOT.get().getDefaultInstance());
        ctx.assertTrue(menu.getSlot(SmithingMenu.RESULT_SLOT).getItem().isEmpty(),
            Component.literal("Shulker upgrade must require a smithing template"));
        menu.getSlot(SmithingMenu.TEMPLATE_SLOT).set(EnderiteMod.ENDERITE_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance());
        ItemStack upgraded = menu.getSlot(SmithingMenu.RESULT_SLOT).getItem();
        ctx.assertTrue(upgraded.is(EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.get()),
            Component.literal("Smithing did not produce an Enderite shulker box"));
        ctx.assertValueEqual(upgraded.get(DataComponents.CONTAINER), original.get(DataComponents.CONTAINER),
            Component.literal("Smithing must preserve all shulker contents"));
        ctx.assertValueEqual(upgraded.get(DataComponents.CUSTOM_NAME), original.get(DataComponents.CUSTOM_NAME),
            Component.literal("Smithing must preserve the shulker name"));
        ctx.succeed();
    }

    public static void enderiteElytraRecipeTest(GameTestHelper ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponents.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.UNBREAKING.identifier());
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get()) == 3,
                    Component.nullToEmpty("Enchantment wrong on Enderite Elytra"));
                var enchant1 = ctx.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.PROTECTION.identifier());
                ctx.assertTrue(enchant1.isPresent() && component.getLevel(enchant1.get()) == 1,
                    Component.nullToEmpty("Enchantment wrong on Enderite Elytra"));
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No Container Component in stack" + stack.toString()));
            }
        });
    }

    public static void enderiteShieldDecoRecipeTest(GameTestHelper ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponents.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.UNBREAKING.identifier());
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get()) == 3,
                    Component.nullToEmpty("Enchantment wrong on decorated Enderite Shield"));
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No Enchantment Component in stack" + stack.toString()));
            }
        });
    }

    public static void enderiteRespawnAnchorTest(GameTestHelper ctx) {
        BlockPos anchor_pos = new BlockPos(0, 1, 0);
        BlockPos dirt_pos = new BlockPos(1, 1, 0);

        // Check block entity
        BlockEntity be = ctx.getBlockEntity(anchor_pos, EnderiteRespawnAnchorBlockEntity.class);
        ctx.assertTrue(be instanceof EnderiteRespawnAnchorBlockEntity,
            Component.nullToEmpty("Enderite Respawn Anchor Block Entity is missing"));
        ctx.assertBlockPresent(Blocks.DIRT, dirt_pos);

        // Check charges
        Player player = ctx.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(ctx.absoluteVec(Vec3.ZERO));
        player.getInventory().add(Items.ENDER_PEARL.getDefaultInstance().copyWithCount(4));
        for (int expected_charge = 1; expected_charge <= 4; expected_charge++) {
            ctx.useBlock(anchor_pos, player);
            ctx.placeAt(player, Items.ENDER_PEARL.getDefaultInstance(), anchor_pos, Direction.NORTH);
            ctx.assertValueEqual(ctx.getBlockState(anchor_pos).getValue(EnderiteRespawnAnchor.CHARGE),
                expected_charge,
                Component.nullToEmpty("Enderite Respawn anchor has wrong amount of charge"));
        }

        ctx.useBlock(anchor_pos, player);
        if (EnderiteRespawnAnchor.isEnd(ctx.getLevel())) {
            // End -> Check if there
            ctx.assertBlockPresent(EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(), anchor_pos);
            ctx.assertBlockPresent(Blocks.DIRT, dirt_pos);
        } else {
            // Not End -> Check if exploded
            ctx.assertBlockNotPresent(EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(), anchor_pos);
            ctx.assertBlockNotPresent(Blocks.DIRT, dirt_pos);
        }
        ctx.succeed();
    }

    public static void voidDeathWithEnderiteTest(GameTestHelper ctx) {
        Vec3 relative_pos = new Vec3(0, 0, 0);
        Vec3 absolute_pos = ctx.absoluteVec(relative_pos);

        ItemEntity ie = ctx.spawnItem(EnderiteMod.ENDERITE_HELMET.get(),
            ctx.relativeVec(new Vec3(absolute_pos.x(), ctx.getLevel().getMinY() - 64, absolute_pos.z())));

        ctx.runAfterDelay(1, () -> {
            ctx.assertEntityPresent(
                EntityTypes.ITEM,
                ctx.relativePos(new BlockPos((int) absolute_pos.x(), ctx.getLevel().getMinY() + 10, (int) absolute_pos.z())),
                2);
            ie.kill(ctx.getLevel());
            ctx.succeed();
        });
    }

    public static void enderiteArmorTrimsTest(GameTestHelper ctx) {
        BlockPos pos1 = ctx.absolutePos(new BlockPos(-1, 0, -1));
        BlockPos pos2 = ctx.absolutePos(new BlockPos(4, 46, 1));

        List<ArmorStand> stands = ctx.getLevel().getEntitiesOfClass(ArmorStand.class, AABB.encapsulatingFullBlocks(pos1, pos2));
        stands.forEach(Entity::discard);

        BlockPos pos = ctx.absolutePos(new BlockPos(0, 3, 0));
        ArrayList<Entity> armor_stands = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pos = pos.relative(Direction.Axis.X, 1);
            Map<EquipmentSlot, Item> equip_map = new HashMap<>();
            Identifier material_id = Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderite");
            switch (i) {
                case 0: {
                    equip_map.put(EquipmentSlot.HEAD, EnderiteMod.ENDERITE_HELMET.get());
                    equip_map.put(EquipmentSlot.CHEST, EnderiteMod.ENDERITE_CHESTPLATE.get());
                    equip_map.put(EquipmentSlot.LEGS, EnderiteMod.ENDERITE_LEGGINGS.get());
                    equip_map.put(EquipmentSlot.FEET, EnderiteMod.ENDERITE_BOOTS.get());
                    break;
                }
                case 1: {
                    equip_map.put(EquipmentSlot.CHEST, EnderiteMod.ENDERITE_ELYTRA.get());
                    material_id = TrimMaterials.GOLD.identifier();
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
            Holder<TrimMaterial> material = ctx.getLevel().registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL).get(material_id).get();
            int idx = 0;
            for (Holder<TrimPattern> pattern : ctx.getLevel().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN).asHolderIdMap()) {
                BlockPos new_pos = pos.above(idx);
                idx += 2;
                ArmorStand e = new ArmorStand(ctx.getLevel(), new_pos.getX(), new_pos.getY(), new_pos.getZ());
                int equip_idx = ctx.getLevel().getRandom().nextInt(equip_map.keySet().toArray().length);
                EquipmentSlot slot = equip_map.keySet().stream().toList().get(equip_idx);
                for (Map.Entry<EquipmentSlot, Item> entry : equip_map.entrySet()) {
                    ItemStack stack = entry.getValue().getDefaultInstance();
                    stack.set(DataComponents.TRIM, new ArmorTrim(material, pattern));
                    e.setItemSlot(entry.getKey(), stack);

                    if (entry.getKey().equals(slot)) {
                        e.setItemSlot(EquipmentSlot.MAINHAND, stack);
                        e.setShowArms(true);
                    }
                }
                e.setNoGravity(true);
                e.setNoBasePlate(true);
                armor_stands.add(e);
            }
        }
        ctx.getLevel().addWorldGenChunkEntities(armor_stands.stream());
        ctx.succeed();
    }

    public static void enderiteElytraTrimRecipeTest(GameTestHelper ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponents.TRIM);
            if (component != null) {
                String trim = component.material().getRegisteredName();
                ctx.assertValueEqual(trim, "minecraft:gold", Component.nullToEmpty("Wrong trim was applied."));
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No Trim Component in stack" + stack.toString()));
            }
        });
    }

    public static void endermanEnderiteTest(GameTestHelper ctx) {
        ServerLevel world = ctx.getLevel();
        BlockPos pos = ctx.absolutePos(new BlockPos(0, 1, 0));

        Enderman enderman = new Enderman(EntityTypes.ENDERMAN, world);
        enderman.setPos(Vec3.atBottomCenterOf(pos));
        world.addFreshEntity(enderman);

        Player player = ctx.makeMockPlayer(GameType.CREATIVE);

        AbstractArrow persistentProjectileEntity2 = ((ArrowItem) Items.ARROW)
            .createArrow(world, Items.ARROW.getDefaultInstance(), player, EnderiteTools.ENDERITE_BOW.get().getDefaultInstance());
        persistentProjectileEntity2.setCustomName(Component.literal("Enderite Arrow"));
        DamageSource source2 = world.damageSources().arrow(persistentProjectileEntity2, player);
        int damage = 10;
        enderman.hurtServer(world, source2, damage);
        ctx.assertValueEqual(enderman.getHealth(), enderman.getMaxHealth() - damage, Component.nullToEmpty("Enderman not damaged from enderite arrow!"));

        AbstractArrow persistentProjectileEntity = ((ArrowItem) Items.ARROW)
            .createArrow(world, Items.ARROW.getDefaultInstance(), player, Items.BOW.getDefaultInstance());
        DamageSource source = world.damageSources().arrow(persistentProjectileEntity, player);
        enderman.hurtServer(world, source, damage);
        ctx.assertValueEqual(enderman.getHealth(), enderman.getMaxHealth() - damage, Component.nullToEmpty("Enderman damaged from default arrow!"));

        enderman.discard();
        ctx.succeed();
    }

    public static void enderiteSmeltingTest(GameTestHelper ctx) {
        BlockPos pos = new BlockPos(0, 1, 0);

        ctx.runAfterDelay(15, () -> {
            BlockEntity be = ctx.getBlockEntity(pos, BlastFurnaceBlockEntity.class);
            if (be instanceof BlastFurnaceBlockEntity bfbe) {
                ItemStack stack = bfbe.getItem(2);
                ctx.assertTrue(stack.is(EnderiteMod.ENDERITE_SCRAP.get()), Component.nullToEmpty("No scrap produced"));
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No Chest Block Entity found"));
            }
            ctx.succeed();
        });
    }

    public static void enderiteDispenserShearsTest(GameTestHelper ctx) {
        ServerLevel world = ctx.getLevel();
        BlockPos pos = ctx.absolutePos(new BlockPos(0, 1, 0));
        BlockPos button_pos = new BlockPos(1, 1, 1);

        ctx.pressButton(button_pos);
        ctx.runAfterDelay(3, () -> {
            ctx.setBlock(button_pos, Blocks.OAK_BUTTON);
            ctx.pressButton(button_pos);
        });
        ctx.runAfterDelay(5, () -> {
            LivingEntity entity = world.getNearestEntity(Sheep.class, TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ(), AABB.encapsulatingFullBlocks(pos, pos));
            if (entity instanceof Sheep sheep) {
                ctx.assertTrue(sheep.isSheared(), Component.nullToEmpty("Sheep is not sheared!"));
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No sheep spawned!"));
            }
            ctx.succeed();
        });
    }

    public static void enderiteDispenserShulkerboxTest(GameTestHelper ctx) {
        BlockPos pos = new BlockPos(0, 1, 0);
        BlockPos button_pos = new BlockPos(1, 1, 1);

        ctx.pressButton(button_pos);
        ctx.runAfterDelay(15, () -> {
            ctx.assertBlockPresent(EnderiteMod.ENDERITE_SHULKER_BOX.get(), pos);
            ctx.succeed();
        });
    }

    public static void enderiteToolInteractionsTest(GameTestHelper ctx) {
        Player player = ctx.makeMockPlayer(GameType.SURVIVAL);
        BlockPos pos = new BlockPos(1, 1, 1);
        BlockPos absolutePos = ctx.absolutePos(pos);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.UP, absolutePos, false);
        ctx.setBlock(pos.above(), Blocks.AIR);

        ctx.setBlock(pos, Blocks.OAK_LOG);
        player.setItemSlot(EquipmentSlot.MAINHAND, EnderiteTools.ENDERITE_AXE.get().getDefaultInstance());
        ctx.useBlock(pos, player, hit);
        ctx.assertBlockPresent(Blocks.STRIPPED_OAK_LOG, pos);

        ctx.setBlock(pos, Blocks.DIRT);
        player.setItemSlot(EquipmentSlot.MAINHAND, EnderiteTools.ENDERITE_HOE.get().getDefaultInstance());
        ctx.useBlock(pos, player, hit);
        ctx.assertBlockPresent(Blocks.FARMLAND, pos);

        ctx.setBlock(pos, Blocks.GRASS_BLOCK);
        player.setItemSlot(EquipmentSlot.MAINHAND, EnderiteTools.ENDERITE_SHOVEL.get().getDefaultInstance());
        ctx.useBlock(pos, player, hit);
        ctx.assertBlockPresent(Blocks.DIRT_PATH, pos);

        // Vanilla teleportation now finds supporting blocks through the blocks_motion tag.
        for (Block floor : List.of(EnderiteMod.ENDERITE_BLOCK.get(), EnderiteMod.ENDERITE_ORE.get(),
                EnderiteMod.CRACKED_ENDERITE_ORE.get(), EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(),
                EnderiteMod.ENDERITE_SHULKER_BOX.get())) {
            ctx.setBlock(pos, floor);
            ctx.assertTrue(ctx.getBlockState(pos).is(BlockTags.ENTITIES_CAN_TELEPORT_TO),
                Component.literal("Missing vanilla teleport-floor tag for " + floor));
            ctx.assertTrue(player.randomTeleport(absolutePos.getX() + 0.5, absolutePos.getY() + 1,
                    absolutePos.getZ() + 0.5, false, BlockTags.CONSUMABLE_DOES_NOT_TELEPORT_TO),
                Component.literal("Vanilla teleportation must accept " + floor + " as a floor"));
        }
        ctx.succeed();
    }

    public static void enderiteShearsLeavesTest(GameTestHelper ctx) {
        var leaves = ctx.getLevel().registryAccess().lookupOrThrow(Registries.BLOCK).getOrThrow(BlockTags.LEAVES);
        ctx.assertTrue(leaves.size() > 0, Component.literal("The leaves tag must not be empty"));
        Player player = ctx.makeMockPlayer(GameType.SURVIVAL);
        ItemStack shears = EnderiteTools.ENDERITE_SHEAR.get().getDefaultInstance();
        player.setItemSlot(EquipmentSlot.MAINHAND, shears);
        BlockPos pos = ctx.absolutePos(new BlockPos(1, 1, 1));
        for (var leaf : leaves) {
            Block block = leaf.value();
            List<ItemStack> drops = Block.getDrops(block.defaultBlockState(), ctx.getLevel(), pos, null, player, shears);
            int leafCount = drops.stream().filter(stack -> stack.is(block.asItem())).mapToInt(ItemStack::getCount).sum();
            ctx.assertValueEqual(leafCount, 1,
                Component.literal("Enderite shears must drop exactly one " + leaf.unwrapKey().orElseThrow().identifier()
                    + "; got " + drops));
        }
        ctx.succeed();
    }

    public static void enderiteShulkerboxDropsTest(GameTestHelper ctx) {
        BlockPos pos = new BlockPos(1, 1, 1);
        ctx.setBlock(pos, EnderiteMod.ENDERITE_SHULKER_BOX.get());
        EnderiteShulkerBoxBlockEntity box = ctx.getBlockEntity(pos, EnderiteShulkerBoxBlockEntity.class);
        ItemStack namedBox = EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.get().getDefaultInstance();
        namedBox.set(DataComponents.CUSTOM_NAME, Component.literal("Loot test"));
        box.applyComponentsFromItemStack(namedBox);
        box.setItem(0, new ItemStack(Items.DIRT, 32));
        ctx.getLevel().destroyBlock(ctx.absolutePos(pos), true);
        List<ItemEntity> drops = ctx.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(ctx.absolutePos(pos)).inflate(1));
        ctx.assertValueEqual(drops.size(), 1, Component.literal("Shulker box must drop exactly one item"));
        ItemStack dropped = drops.getFirst().getItem();
        ctx.assertTrue(dropped.is(EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.get()), Component.literal("Wrong shulker drop"));
        ItemStack contents = dropped.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne();
        ctx.assertTrue(contents.is(Items.DIRT) && contents.getCount() == 32, Component.literal("Shulker loot lost stored items"));
        ctx.assertValueEqual(dropped.get(DataComponents.CUSTOM_NAME), Component.literal("Loot test"),
            Component.literal("Shulker loot lost its custom name"));
        ctx.succeed();
    }

    public static void enderiteItemNoGravityTest(GameTestHelper ctx) {
        ItemEntity enderite = ctx.spawnItem(EnderiteMod.ENDERITE_INGOT.get(), 0.5F, 3.0F, 0.5F);
        ItemEntity control = ctx.spawnItem(Items.DIRT, 1.5F, 3.0F, 0.5F);

        ctx.runAfterDelay(20, () -> {
            ctx.assertTrue(enderite.isAlive(), Component.literal("Enderite item unexpectedly disappeared"));
            ctx.assertTrue(enderite.isNoGravity(), Component.literal("Enderite item still has gravity"));
            ctx.assertFalse(control.isNoGravity(), Component.literal("Control item unexpectedly has no gravity"));
            ctx.assertTrue(
                enderite.getY() > control.getY() + 0.5D,
                Component.literal("Enderite item did not remain above the falling control item")
            );
            ctx.succeed();
        });
    }

    public static void enderiteItemFireproofTest(GameTestHelper ctx) {
        ItemEntity enderite = ctx.spawnItem(EnderiteMod.ENDERITE_INGOT.get(), 0.5F, 1.1F, 0.5F);
        ItemEntity control = ctx.spawnItem(Items.DIRT, 1.5F, 1.1F, 0.5F);

        ctx.runAfterDelay(40, () -> {
            ctx.assertTrue(enderite.isAlive(), Component.literal("Enderite item did not survive lava"));
            ctx.assertFalse(control.isAlive(), Component.literal("Control item unexpectedly survived lava"));
            ctx.succeed();
        });
    }

    public static void enderiteDispenserShulkerboxContentsTest(GameTestHelper ctx) {
        BlockPos dispenserPos = new BlockPos(1, 1, 0);
        DispenserBlockEntity dispenser = ctx.getBlockEntity(dispenserPos, DispenserBlockEntity.class);
        ItemStack shulker = EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.get().getDefaultInstance();
        shulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(
            Items.DIAMOND.getDefaultInstance().copyWithCount(3),
            Items.DIRT.getDefaultInstance().copyWithCount(17)
        )));
        dispenser.setItem(0, shulker);

        ctx.pressButton(1, 1, 1);
        ctx.runAfterDelay(15, () -> {
            EnderiteShulkerBoxBlockEntity placed = ctx.getBlockEntity(
                new BlockPos(0, 1, 0),
                EnderiteShulkerBoxBlockEntity.class
            );
            ctx.assertTrue(placed.getItem(0).is(Items.DIAMOND), Component.literal("First shulker-box item was not preserved"));
            ctx.assertValueEqual(placed.getItem(0).getCount(), 3, Component.literal("Diamond count was not preserved"));
            ctx.assertTrue(placed.getItem(1).is(Items.DIRT), Component.literal("Second shulker-box item was not preserved"));
            ctx.assertValueEqual(placed.getItem(1).getCount(), 17, Component.literal("Dirt count was not preserved"));
            ctx.succeed();
        });
    }

    public static void enderiteShulkerboxHopperTest(GameTestHelper ctx) {
        EnderiteShulkerBoxBlockEntity shulker = ctx.getBlockEntity(
            new BlockPos(0, 3, 0),
            EnderiteShulkerBoxBlockEntity.class
        );

        ctx.assertTrue(
            shulker.canPlaceItemThroughFace(0, Items.DIAMOND.getDefaultInstance(), Direction.UP),
            Component.literal("Enderite shulker box rejected an ordinary item")
        );
        ctx.assertFalse(
            shulker.canPlaceItemThroughFace(0, Items.SHULKER_BOX.getDefaultInstance(), Direction.UP),
            Component.literal("Enderite shulker box accepted a vanilla shulker box")
        );
        ctx.assertFalse(
            shulker.canPlaceItemThroughFace(0, EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.get().getDefaultInstance(), Direction.UP),
            Component.literal("Enderite shulker box accepted another Enderite shulker box")
        );

        shulker.setItem(0, Items.DIAMOND.getDefaultInstance().copyWithCount(2));
        ctx.runAfterDelay(20, () -> {
            ChestBlockEntity chest = ctx.getBlockEntity(new BlockPos(0, 1, 0), ChestBlockEntity.class);
            ctx.assertTrue(chest.getItem(0).is(Items.DIAMOND), Component.literal("Hopper did not extract from Enderite shulker box"));
            ctx.assertTrue(chest.getItem(0).getCount() > 0, Component.literal("Extracted diamond stack was empty"));
            ctx.succeed();
        });
    }

    public static void creeperExplodeEnderiteOreTest(GameTestHelper ctx) {
        Creeper creeper = ctx.spawnWithNoFreeWill(EntityTypes.CREEPER, new BlockPos(1, 1, 1));
        creeper.ignite();
        ctx.succeedWhenBlockPresent(EnderiteMod.CRACKED_ENDERITE_ORE.get(), new BlockPos(1, 1, 2));
    }

    public static void enderiteRegularFurnaceRejectsOreTest(GameTestHelper ctx) {
        FurnaceBlockEntity furnace = ctx.getBlockEntity(new BlockPos(0, 1, 0), FurnaceBlockEntity.class);
        furnace.setItem(0, EnderiteMod.CRACKED_ENDERITE_ORE_ITEM.get().getDefaultInstance());
        furnace.setItem(1, Items.COAL.getDefaultInstance());

        ctx.runAfterDelay(450, () -> {
            ctx.assertTrue(furnace.getItem(0).is(EnderiteMod.CRACKED_ENDERITE_ORE_ITEM.get()), Component.literal("Regular furnace consumed cracked Enderite ore"));
            ctx.assertTrue(furnace.getItem(2).isEmpty(), Component.literal("Regular furnace smelted a blasting-only recipe"));
            ctx.succeed();
        });
    }

    public static void enderiteOreGenerationTest(GameTestHelper ctx) {
        ServerLevel end = ctx.getLevel().getServer().getLevel(Level.END);
        ctx.assertTrue(end != null, Component.literal("The End dimension is missing"));
        var biomes = end.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(BiomeTags.IS_END);
        ctx.assertTrue(biomes.size() > 0, Component.literal("No End biomes are registered"));
        for (var biome : biomes) {
            for (String name : List.of("ore_enderite_large", "ore_enderite_small")) {
                var feature = ResourceKey.create(Registries.PLACED_FEATURE,
                    Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, name));
                long count = biome.value().getGenerationSettings().features().stream()
                    .flatMap(features -> features.stream()).filter(placed -> placed.is(feature)).count();
                ctx.assertTrue(count == 1, Component.literal(
                    biome.unwrapKey().orElseThrow().identifier() + " must contain exactly one " + name + ", found " + count));
            }
        }
        // Exercise End decoration as well as registration, catching feature-order cycles.
        end.getChunk(80, 80);
        ctx.succeed();
    }

    // HELPER
    public static void crafterRecipeTest(GameTestHelper ctx, Consumer<ItemStack> stackVerifier) {
        ctx.pressButton(1, 1, 1);
        ctx.runAfterDelay(15, () -> {
            BlockEntity be = ctx.getBlockEntity(new BlockPos(0, 1, 0), ChestBlockEntity.class);
            if (be instanceof ChestBlockEntity cbe) {
                ItemStack stack = cbe.getItem(0);
                stackVerifier.accept(stack);
            } else {
                ctx.assertTrue(false, Component.nullToEmpty("No Chest Block Entity found"));
            }
            ctx.succeed();
        });
    }
}
