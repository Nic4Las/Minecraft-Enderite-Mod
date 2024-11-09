package net.enderitemc.enderitemod.misc;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public abstract class EnderiteUpgradeSmithingTemplate {
    public static void registerLoottables() {
        LootEvent.MODIFY_LOOT_TABLE.register((baseTable, table, builtin) -> {
            Identifier id = baseTable.getValue();

            tryBuildLootTable(id, table, LootTables.END_CITY_TREASURE_CHEST.getValue());
        });
    }

    public static void tryBuildLootTable(Identifier id, LootTableModificationContext table, Identifier name) {
        if (name.equals(id)) {
            LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(EnderiteMod.CONFIG.general.enderiteUpgradeTemplateChance))
                .with(ItemEntry.builder(EnderiteMod.ENDERITE_UPGRADE_SMITHING_TEMPLATE.get().asItem()));
            table.addPool(pool);
        }
    }
}