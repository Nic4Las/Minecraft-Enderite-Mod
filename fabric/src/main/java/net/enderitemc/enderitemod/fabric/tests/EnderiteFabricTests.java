package net.enderitemc.enderitemod.fabric.tests;

import net.enderitemc.enderitemod.tests.EnderiteTests;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

public final class EnderiteFabricTests {
    private EnderiteFabricTests() {
    }

    public static void init() {
        register("explode_enderite_ore_test", EnderiteTests::explodeEnderiteOreTest);
        register("enderite_shulkerbox_recipe_test", EnderiteTests::enderiteShulkerboxRecipeTest);
        register("enderite_elytra_recipe_test", EnderiteTests::enderiteElytraRecipeTest);
        register("enderite_shield_deco_recipe_test", EnderiteTests::enderiteShieldDecoRecipeTest);
        register("enderite_respawn_anchor_test", EnderiteTests::enderiteRespawnAnchorTest);
        register("void_death_with_enderite_test", EnderiteTests::voidDeathWithEnderiteTest);
        register("enderite_armor_trims_test", EnderiteTests::enderiteArmorTrimsTest);
        register("enderite_elytra_trim_recipe_test", EnderiteTests::enderiteElytraTrimRecipeTest);
        register("enderman_enderite_test", EnderiteTests::endermanEnderiteTest);
        register("enderite_smelting_test", EnderiteTests::enderiteSmeltingTest);
        register("enderite_dispenser_shears_test", EnderiteTests::enderiteDispenserShearsTest);
        register("enderite_dispenser_shulkerbox_test", EnderiteTests::enderiteDispenserShulkerboxTest);
        register("enderite_item_no_gravity_test", EnderiteTests::enderiteItemNoGravityTest);
        register("enderite_item_fireproof_test", EnderiteTests::enderiteItemFireproofTest);
        register("enderite_dispenser_shulkerbox_contents_test", EnderiteTests::enderiteDispenserShulkerboxContentsTest);
        register("enderite_shulkerbox_hopper_test", EnderiteTests::enderiteShulkerboxHopperTest);
        register("creeper_explode_enderite_ore_test", EnderiteTests::creeperExplodeEnderiteOreTest);
        register("enderite_regular_furnace_rejects_ore_test", EnderiteTests::enderiteRegularFurnaceRejectsOreTest);
    }

    private static void register(String name, Consumer<GameTestHelper> testFunction) {
        Registry.register(
            BuiltInRegistries.TEST_FUNCTION,
            Identifier.fromNamespaceAndPath(MOD_ID, name),
            testFunction
        );
    }
}
