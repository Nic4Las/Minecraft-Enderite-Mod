package net.enderitemc.enderitemod.tools;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

public class EnderiteShears extends ShearsItem {

    public EnderiteShears(Settings settings) {
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

    public static void tryBuildLootTable(RegistryKey<LootTable> key, LootTableModificationContext context, Block block) {
        if (block.getLootTableKey().isPresent() && block.getLootTableKey().get().equals(key)) {
            LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(MatchToolLootCondition
                    .builder(ItemPredicate.Builder.create().items(Registries.ITEM, EnderiteTools.ENDERITE_SHEAR.get())))
                .with(ItemEntry.builder(block.asItem()));
            context.addPool(pool);
        }
    }

}
