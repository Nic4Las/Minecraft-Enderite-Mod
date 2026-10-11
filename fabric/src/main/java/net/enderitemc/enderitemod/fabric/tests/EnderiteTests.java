package net.enderitemc.enderitemod.fabric.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EnderiteTests {
    public static final String TMPL_MI = EnderiteMod.MOD_ID + ":";
    public static final String TMPL_NS = "enderitetests.";
    public static final String TMPL_PRE = TMPL_MI + TMPL_NS;

    @GameTest(structure = TMPL_PRE + "enderite_ore_generation", maxTicks = 200)
    public void enderiteOreGenerationTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteOreGenerationTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_tool_interactions")
    public void enderiteToolInteractionsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteToolInteractionsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shears_leaves")
    public void enderiteShearsLeavesTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShearsLeavesTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shulkerbox_drops")
    public void enderiteShulkerboxDropsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxDropsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "explode_enderite_ore", maxTicks = 120)
    public void explodeEnderiteOreTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.explodeEnderiteOreTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shulkerbox_recipe")
    public void enderiteShulkerboxRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_recipe")
    public void enderiteElytraRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shield_deco_recipe")
    public void enderiteShieldDecoRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShieldDecoRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_respawn_anchor")
    public void enderiteRespawnAnchorTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteRespawnAnchorTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "void_death_with_enderite")
    public void voidDeathWithEnderiteTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.voidDeathWithEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_armor_trims")
    public void enderiteArmorTrimsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteArmorTrimsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_trim_recipe")
    public void enderiteElytraTrimRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraTrimRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderman_enderite")
    public void endermanEnderiteTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.endermanEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_smelting")
    public void enderiteSmeltingTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteSmeltingTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shears")
    public void enderiteDispenserShearsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShearsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shulkerbox")
    public void enderiteDispenserShulkerboxTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShulkerboxTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "creeper_explode_enderite_ore", maxTicks = 100, environment = "enderitemod:mob_griefing")
    public void creeperExplodeEnderiteOreTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.creeperExplodeEnderiteOreTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shulkerbox_contents", maxTicks = 100)
    public void enderiteDispenserShulkerboxContentsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShulkerboxContentsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_item_fireproof", maxTicks = 100)
    public void enderiteItemFireproofTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteItemFireproofTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_item_no_gravity", maxTicks = 100)
    public void enderiteItemNoGravityTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteItemNoGravityTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_regular_furnace_rejects_ore", maxTicks = 500)
    public void enderiteRegularFurnaceRejectsOreTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteRegularFurnaceRejectsOreTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shulkerbox_hopper", maxTicks = 100)
    public void enderiteShulkerboxHopperTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxHopperTest(ctx);
    }
}
