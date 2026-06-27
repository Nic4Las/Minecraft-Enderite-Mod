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

    @GameTest(structure = TMPL_PRE + "explode_enderite_ore")
    public static void explodeEnderiteOreTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.explodeEnderiteOreTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shulkerbox_recipe")
    public static void enderiteShulkerboxRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_recipe")
    public static void enderiteElytraRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shield_deco_recipe")
    public static void enderiteShieldDecoRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShieldDecoRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_respawn_anchor")
    public static void enderiteRespawnAnchorTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteRespawnAnchorTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "void_death_with_enderite")
    public static void voidDeathWithEnderiteTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.voidDeathWithEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_armor_trims")
    public static void enderiteArmorTrimsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteArmorTrimsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_trim_recipe")
    public static void enderiteElytraTrimRecipeTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraTrimRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderman_enderite")
    public static void endermanEnderiteTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.endermanEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_smelting")
    public static void enderiteSmeltingTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteSmeltingTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shears")
    public static void enderiteDispenserShearsTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShearsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shulkerbox")
    public static void enderiteDispenserShulkerboxTest(GameTestHelper ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShulkerboxTest(ctx);
    }
}
