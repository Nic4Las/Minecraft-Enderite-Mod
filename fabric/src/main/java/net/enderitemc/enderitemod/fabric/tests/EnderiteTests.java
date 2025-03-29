package net.enderitemc.enderitemod.fabric.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

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
    public static void explodeEnderiteOreTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.explodeEnderiteOreTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shulkerbox_recipe")
    public static void enderiteShulkerboxRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShulkerboxRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_recipe")
    public static void enderiteElytraRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_shield_deco_recipe")
    public static void enderiteShieldDecoRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteShieldDecoRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_respawn_anchor")
    public static void enderiteRespawnAnchorTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteRespawnAnchorTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "void_death_with_enderite")
    public static void voidDeathWithEnderiteTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.voidDeathWithEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_armor_trims")
    public static void enderiteArmorTrimsTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteArmorTrimsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_elytra_trim_recipe")
    public static void enderiteElytraTrimRecipeTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteElytraTrimRecipeTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderman_enderite")
    public static void endermanEnderiteTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.endermanEnderiteTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_smelting")
    public static void enderiteSmeltingTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteSmeltingTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shears")
    public static void enderiteDispenserShearsTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShearsTest(ctx);
    }

    @GameTest(structure = TMPL_PRE + "enderite_dispenser_shulkerbox")
    public static void enderiteDispenserShulkerboxTest(TestContext ctx) {
        net.enderitemc.enderitemod.tests.EnderiteTests.enderiteDispenserShulkerboxTest(ctx);
    }
}
