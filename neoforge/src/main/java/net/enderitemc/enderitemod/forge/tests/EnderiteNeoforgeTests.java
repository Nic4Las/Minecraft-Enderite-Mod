package net.enderitemc.enderitemod.forge.tests;

import net.enderitemc.enderitemod.tests.EnderiteTests;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

public final class EnderiteNeoforgeTests {
    private static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS = DeferredRegister.create(
        BuiltInRegistries.TEST_FUNCTION,
        MOD_ID
    );

    static {
        TEST_FUNCTIONS.register("explode_enderite_ore_test", () -> EnderiteTests::explodeEnderiteOreTest);
        // now smithing: TEST_FUNCTIONS.register("enderite_shulkerbox_recipe_test", () -> EnderiteTests::enderiteShulkerboxRecipeTest);
        TEST_FUNCTIONS.register("enderite_elytra_recipe_test", () -> EnderiteTests::enderiteElytraRecipeTest);
        TEST_FUNCTIONS.register("enderite_shield_deco_recipe_test", () -> EnderiteTests::enderiteShieldDecoRecipeTest);
        TEST_FUNCTIONS.register("enderite_respawn_anchor_test", () -> EnderiteTests::enderiteRespawnAnchorTest);
        TEST_FUNCTIONS.register("void_death_with_enderite_test", () -> EnderiteTests::voidDeathWithEnderiteTest);
        TEST_FUNCTIONS.register("enderite_armor_trims_test", () -> EnderiteTests::enderiteArmorTrimsTest);
        TEST_FUNCTIONS.register("enderite_elytra_trim_recipe_test", () -> EnderiteTests::enderiteElytraTrimRecipeTest);
        TEST_FUNCTIONS.register("enderman_enderite_test", () -> EnderiteTests::endermanEnderiteTest);
        TEST_FUNCTIONS.register("enderite_smelting_test", () -> EnderiteTests::enderiteSmeltingTest);
        TEST_FUNCTIONS.register("enderite_dispenser_shears_test", () -> EnderiteTests::enderiteDispenserShearsTest);
        TEST_FUNCTIONS.register("enderite_dispenser_shulkerbox_test", () -> EnderiteTests::enderiteDispenserShulkerboxTest);
        TEST_FUNCTIONS.register("enderite_item_no_gravity_test", () -> EnderiteTests::enderiteItemNoGravityTest);
        TEST_FUNCTIONS.register("enderite_item_fireproof_test", () -> EnderiteTests::enderiteItemFireproofTest);
        TEST_FUNCTIONS.register("enderite_dispenser_shulkerbox_contents_test", () -> EnderiteTests::enderiteDispenserShulkerboxContentsTest);
        TEST_FUNCTIONS.register("enderite_shulkerbox_hopper_test", () -> EnderiteTests::enderiteShulkerboxHopperTest);
        TEST_FUNCTIONS.register("creeper_explode_enderite_ore_test", () -> EnderiteTests::creeperExplodeEnderiteOreTest);
        TEST_FUNCTIONS.register("enderite_regular_furnace_rejects_ore_test", () -> EnderiteTests::enderiteRegularFurnaceRejectsOreTest);
    }

    private EnderiteNeoforgeTests() {
    }

    public static void init(IEventBus modBus) {
        TEST_FUNCTIONS.register(modBus);
    }
}
