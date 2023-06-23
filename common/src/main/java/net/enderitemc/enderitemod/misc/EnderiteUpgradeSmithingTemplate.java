package net.enderitemc.enderitemod.misc;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public abstract class EnderiteUpgradeSmithingTemplate {
    public static void registerLoottables() {
        LootEvent.MODIFY_LOOT_TABLE.register((lootTables, id, table, builtin) -> {

            tryBuildLootTable(id, table, LootTables.END_CITY_TREASURE_CHEST);
        });
    }

    public static void tryBuildLootTable(Identifier id, LootTableModificationContext table, Identifier name) {
        if (name.equals(id)) {
            LootPool pool = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(EnderiteMod.ENDERITE_UPGRADE_SMITHING_TEMPLATE.get().asItem()))
                    .build();
            table.addPool(pool);
        }
    }
}