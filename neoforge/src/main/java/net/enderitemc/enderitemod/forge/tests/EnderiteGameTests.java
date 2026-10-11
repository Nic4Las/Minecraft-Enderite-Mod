package net.enderitemc.enderitemod.forge.tests;

import java.util.List;
import java.util.function.Consumer;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tests.EnderiteTests;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.block.Rotation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.gametest.GameTestHooks;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = EnderiteMod.MOD_ID)
public final class EnderiteGameTests {
    private record Test(String name, int maxTicks, Consumer<GameTestHelper> function) {
        Identifier id() {
            return Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, name);
        }

        Identifier structure() {
            return Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderitetests." + name);
        }
    }

    private static final List<Test> TESTS = List.of(
        new Test("enderite_ore_generation", 200, EnderiteTests::enderiteOreGenerationTest),
        new Test("enderite_tool_interactions", 20, EnderiteTests::enderiteToolInteractionsTest),
        new Test("enderite_shears_leaves", 20, EnderiteTests::enderiteShearsLeavesTest),
        new Test("enderite_shulkerbox_drops", 20, EnderiteTests::enderiteShulkerboxDropsTest),
        new Test("explode_enderite_ore", 120, EnderiteTests::explodeEnderiteOreTest),
        new Test("enderite_shulkerbox_recipe", 20, EnderiteTests::enderiteShulkerboxRecipeTest),
        new Test("enderite_elytra_recipe", 20, EnderiteTests::enderiteElytraRecipeTest),
        new Test("enderite_shield_deco_recipe", 20, EnderiteTests::enderiteShieldDecoRecipeTest),
        new Test("enderite_respawn_anchor", 20, EnderiteTests::enderiteRespawnAnchorTest),
        new Test("void_death_with_enderite", 20, EnderiteTests::voidDeathWithEnderiteTest),
        new Test("enderite_armor_trims", 20, EnderiteTests::enderiteArmorTrimsTest),
        new Test("enderite_elytra_trim_recipe", 20, EnderiteTests::enderiteElytraTrimRecipeTest),
        new Test("enderman_enderite", 20, EnderiteTests::endermanEnderiteTest),
        new Test("enderite_smelting", 20, EnderiteTests::enderiteSmeltingTest),
        new Test("enderite_dispenser_shears", 20, EnderiteTests::enderiteDispenserShearsTest),
        new Test("enderite_dispenser_shulkerbox", 20, EnderiteTests::enderiteDispenserShulkerboxTest),
        new Test("creeper_explode_enderite_ore", 100, EnderiteTests::creeperExplodeEnderiteOreTest),
        new Test("enderite_dispenser_shulkerbox_contents", 100, EnderiteTests::enderiteDispenserShulkerboxContentsTest),
        new Test("enderite_item_fireproof", 100, EnderiteTests::enderiteItemFireproofTest),
        new Test("enderite_item_no_gravity", 100, EnderiteTests::enderiteItemNoGravityTest),
        new Test("enderite_regular_furnace_rejects_ore", 500, EnderiteTests::enderiteRegularFurnaceRejectsOreTest),
        new Test("enderite_shulkerbox_hopper", 100, EnderiteTests::enderiteShulkerboxHopperTest)
    );

    @SubscribeEvent
    public static void registerFunctions(RegisterEvent event) {
        if (GameTestHooks.isGametestEnabled()) {
            event.register(Registries.TEST_FUNCTION, registry ->
                TESTS.forEach(test -> registry.register(test.id(), test.function())));
        }
    }

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(
            Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "default"), new TestEnvironmentDefinition.AllOf(List.of()));
        var mobGriefingRules = GameRuleMap.of();
        mobGriefingRules.set(GameRules.MOB_GRIEFING, true);
        var mobGriefing = event.registerEnvironment(
            Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "creeper_test"),
            new TestEnvironmentDefinition.SetGameRules(mobGriefingRules));
        for (Test test : TESTS) {
            event.registerTest(test.id(), new FunctionGameTestInstance(
                ResourceKey.create(Registries.TEST_FUNCTION, test.id()),
                new TestData<>(test.name().equals("creeper_explode_enderite_ore") ? mobGriefing : environment, Level.OVERWORLD, test.structure(), test.maxTicks(), 0,
                    true, Rotation.NONE, false, 1, 1, false, 1)));
        }
    }
}
