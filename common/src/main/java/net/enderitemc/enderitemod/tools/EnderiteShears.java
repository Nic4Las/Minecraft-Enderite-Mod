package net.enderitemc.enderitemod.tools;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class EnderiteShears extends ShearsItem {

    public EnderiteShears(Properties settings) {
        super(settings);
    }


    public static void registerLoottables_Fabric() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            tryBuildLootTable(key, context, Blocks.ACACIA_LEAVES);

            tryBuildLootTable(key, context, Blocks.AZALEA_LEAVES);

            tryBuildLootTable(key, context, Blocks.BIRCH_LEAVES);

            tryBuildLootTable(key, context, Blocks.CAVE_VINES);
            tryBuildLootTable(key, context, Blocks.CAVE_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.COBWEB);

            tryBuildLootTable(key, context, Blocks.DARK_OAK_LEAVES);

            tryBuildLootTable(key, context, Blocks.DEAD_BUSH);

            tryBuildLootTable(key, context, Blocks.FERN);

            tryBuildLootTable(key, context, Blocks.FLOWERING_AZALEA_LEAVES);

            tryBuildLootTable(key, context, Blocks.GLOW_LICHEN);

            tryBuildLootTable(key, context, Blocks.SHORT_GRASS);

            tryBuildLootTable(key, context, Blocks.JUNGLE_LEAVES);

            tryBuildLootTable(key, context, Blocks.LARGE_FERN);

            tryBuildLootTable(key, context, Blocks.NETHER_SPROUTS);

            tryBuildLootTable(key, context, Blocks.OAK_LEAVES);

            tryBuildLootTable(key, context, Blocks.SEAGRASS);

            tryBuildLootTable(key, context, Blocks.SPRUCE_LEAVES);

            tryBuildLootTable(key, context, Blocks.TALL_GRASS);

            tryBuildLootTable(key, context, Blocks.TALL_SEAGRASS);

            tryBuildLootTable(key, context, Blocks.TWISTING_VINES);
            tryBuildLootTable(key, context, Blocks.TWISTING_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.VINE);

            tryBuildLootTable(key, context, Blocks.WEEPING_VINES);
            tryBuildLootTable(key, context, Blocks.WEEPING_VINES_PLANT);

            tryBuildLootTable(key, context, Blocks.SMALL_DRIPLEAF);

            tryBuildLootTable(key, context, Blocks.MANGROVE_LEAVES);
            tryBuildLootTable(key, context, Blocks.HANGING_ROOTS);

            tryBuildLootTable(key, context, Blocks.CHERRY_LEAVES);

            tryBuildLootTable(key, context, Blocks.PALE_OAK_LEAVES);
            tryBuildLootTable(key, context, Blocks.PALE_HANGING_MOSS);

            tryBuildLootTable(key, context, Blocks.BUSH);
            tryBuildLootTable(key, context, Blocks.SHORT_DRY_GRASS);
            tryBuildLootTable(key, context, Blocks.TALL_DRY_GRASS);
        });
    }

    public static void tryBuildLootTable(ResourceKey<LootTable> key, LootTableModificationContext context, Block block) {
        if (block.getLootTable().isPresent() && block.getLootTable().get().equals(key)) {
            LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(MatchTool
                    .toolMatches(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, EnderiteTools.ENDERITE_SHEAR.get())))
                .add(LootItem.lootTableItem(block.asItem()));
            context.addPool(pool);
        }
    }

}
