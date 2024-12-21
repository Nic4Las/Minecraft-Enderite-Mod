package net.enderitemc.enderitemod.forge.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.neoforged.neoforge.gametest.GameTestHolder;

@GameTestHolder(EnderiteMod.MOD_ID)
public class EnderiteTests {

    @GameTest(templateName = "explode_enderite_ore")
    public static void explodeEnderiteOreTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.explodeEnderiteOreTest(ctx);
    }

    @GameTest(templateName = "enderite_shulkerbox_recipe")
    public static void enderiteShulkerboxRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxRecipeTest(ctx);
    }

    @GameTest(templateName = "enderite_elytra_recipe")
    public static void enderiteElytraRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraRecipeTest(ctx);
    }

    @GameTest(templateName = "enderite_shield_deco_recipe")
    public static void enderiteShieldDecoRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShieldDecoRecipeTest(ctx);
    }

    @GameTest(templateName = "enderite_respawn_anchor")
    public static void enderiteRespawnAnchorTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteRespawnAnchorTest(ctx);
    }

    @GameTest(templateName = "void_death_with_enderite")
    public static void voidDeathWithEnderiteTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.voidDeathWithEnderiteTest(ctx);
    }

    @GameTest(templateName = "enderite_armor_trims")
    public static void eneriteArmorTrimsTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteArmorTrimsTest(ctx);
    }

    @GameTest(templateName = "enderite_elytra_trim_recipe")
    public static void enderiteElytraTrimRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraTrimRecipeTest(ctx);
    }

    @GameTest(templateName = "enderman_enderite")
    public static void endermanEnderiteTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.endermanEnderiteTest(ctx);
    }

    @GameTest(templateName = "enderite_smelting")
    public static void enderiteSmeltingTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteSmeltingTest(ctx);
    }

    @GameTest(templateName = "enderite_dispenser_shears")
    public static void enderiteDispenserShearsTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShearsTest(ctx);
    }

    @GameTest(templateName = "enderite_dispenser_shulkerbox")
    public static void enderiteDispenserShulkerboxTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShulkerboxTest(ctx);
    }
}
