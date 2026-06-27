package net.enderitemc.enderitemod.misc;

import dev.architectury.event.events.common.LootEvent;
import dev.architectury.event.events.common.LootEvent.LootTableModificationContext;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public abstract class EnderiteUpgradeSmithingTemplate {
    public static void registerLoottables() {
        LootEvent.MODIFY_LOOT_TABLE.register((baseTable, table, builtin) -> {
            Identifier id = baseTable.identifier();

            tryBuildLootTable(id, table, BuiltInLootTables.END_CITY_TREASURE.identifier());
        });
    }

    public static void tryBuildLootTable(Identifier id, LootTableModificationContext table, Identifier name) {
        if (name.equals(id)) {
            LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(EnderiteMod.CONFIG.general.enderiteUpgradeTemplateChance))
                .add(LootItem.lootTableItem(EnderiteMod.ENDERITE_UPGRADE_SMITHING_TEMPLATE.get().asItem()));
            table.addPool(pool);
        }
    }
}