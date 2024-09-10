package net.enderitemc.enderitemod.forge.tests;

import com.mojang.authlib.GameProfile;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.function.Consumer;

@GameTestHolder(EnderiteMod.MOD_ID)
public class EnderiteTests {

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "explode_enderite_ore")
    public static void explodeEnderiteOreTest(TestContext ctx) {
        ctx.spawnEntity(EntityType.TNT, new BlockPos(1, 2, 0));
        ctx.waitAndRun(99, () -> ctx.checkBlock(new BlockPos(1, 2, 1),
                (Block block) -> block.equals(EnderiteMod.CRACKED_ENDERITE_ORE.get()),
                "TnT didn't crack the enderite ore"
        ));
        ctx.expectBlockAtEnd(EnderiteMod.CRACKED_ENDERITE_ORE.get(), new BlockPos(1, 2, 1));
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "enderite_shulkerbox_recipe")
    public static void enderiteShulkerboxRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.CONTAINER);
            if (component != null) {
                ctx.assertTrue(component.getStackInSlot(0).isOf(Items.DIRT),
                        "No items found in Enderite Shulkerbox item");
            } else {
                ctx.assertTrue(false, "No Container Component in stack" + stack.toString());
            }
        });
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "enderite_elytra_recipe")
    public static void enderiteElytraRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.UNBREAKING);
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get())==3,
                        "Enchantment wrong on Enderite Elytra");
                var enchant1 = ctx.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.PROTECTION);
                ctx.assertTrue(enchant1.isPresent() && component.getLevel(enchant1.get())==1,
                        "Enchantment wrong on Enderite Elytra");
            } else {
                ctx.assertTrue(false, "No Container Component in stack" + stack.toString());
            }
        });
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "enderite_shield_deco_recipe")
    public static void enderiteShieldDecoRecipeTest(TestContext ctx) {
        crafterRecipeTest(ctx, (ItemStack stack) -> {
            var component = stack.get(DataComponentTypes.ENCHANTMENTS);
            if (component != null) {
                var enchant0 = ctx.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.UNBREAKING);
                ctx.assertTrue(enchant0.isPresent() && component.getLevel(enchant0.get())==3,
                        "Enchantment wrong on decorated Enderite Shield");
            } else {
                ctx.assertTrue(false, "No Container Component in stack" + stack.toString());
            }
        });
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "enderite_respawn_anchor")
    public static void enderiteRespawnAnchorTest(TestContext ctx) {
        BlockPos anchor_pos = new BlockPos(0,2,0);
        BlockPos dirt_pos = new BlockPos(1,2,0);

        // Check block entity
        BlockEntity be = ctx.getBlockEntity(anchor_pos);
        ctx.assertTrue(be instanceof EnderiteRespawnAnchorBlockEntity,
                "Enderite Respawn Anchor Block Entity is missing");
        ctx.expectBlock(Blocks.DIRT, dirt_pos);

        // Check charges
        PlayerEntity player = ctx.createMockPlayer(GameMode.SURVIVAL);
        player.setPosition(ctx.getAbsolute(Vec3d.ZERO));
        player.getInventory().insertStack(Items.ENDER_PEARL.getDefaultStack().copyWithCount(4));
        for(int expected_charge=1; expected_charge<=4; expected_charge++) {
            ctx.useBlock(anchor_pos, player);
            ctx.useStackOnBlock(player, Items.ENDER_PEARL.getDefaultStack(), anchor_pos, Direction.NORTH);
            ctx.assertEquals(ctx.getBlockState(anchor_pos).get(EnderiteRespawnAnchor.CHARGES),
                    expected_charge,
                    "Enderite Respawn anchor has wrong amount of charge");
        }

        // Overworld -> Check if exploded
        ctx.useBlock(anchor_pos, player);
        ctx.dontExpectBlock(EnderiteMod.ENDERITE_RESPAWN_ANCHOR.get(), anchor_pos);
        ctx.dontExpectBlock(Blocks.DIRT, dirt_pos);
        ctx.complete();
    }

    @PrefixGameTestTemplate(false)
    @GameTest(templateName = "void_death_with_enderite")
    public static void voidDeathWithEnderiteTest(TestContext ctx) {
        Vec3d relative_pos = new Vec3d(0, 1, 0);
        Vec3d absolute_pos = ctx.getAbsolute(relative_pos);

        ItemEntity ie = ctx.spawnItem(EnderiteMod.ENDERITE_HELMET.get(),
                ctx.getRelative(new Vec3d(absolute_pos.getX(),ctx.getWorld().getBottomY()-64,absolute_pos.getZ())));

        ctx.waitAndRun(1, () -> {
            ctx.expectEntityAround(
                    EntityType.ITEM,
                    ctx.getRelativePos(new BlockPos((int)absolute_pos.getX(), ctx.getWorld().getBottomY()+10, (int)absolute_pos.getZ())),
                    2);
            ie.kill();
            ctx.complete();
        });
    }


    // HELPER
    public static void crafterRecipeTest(TestContext ctx, Consumer<ItemStack> stackVerifier) {
        ctx.pushButton(1,2,1);
        ctx.waitAndRun(15, () -> {
            BlockEntity be = ctx.getBlockEntity(new BlockPos(0,2,0));
            if(be instanceof ChestBlockEntity cbe) {
                ItemStack stack = cbe.getStack(0);
                stackVerifier.accept(stack);
            } else {
                ctx.assertTrue(false, "No Chest Block Entity found");
            }
            ctx.complete();
        });
    }

    public static PlayerEntity createMockPlayer(TestContext ctx, GameMode gameMode) {
        return new PlayerEntity(ctx.getWorld(), BlockPos.ORIGIN, 0.0F, new GameProfile(UUID.randomUUID(), "test-mock-player")) {
            @Override
            public boolean isSpectator() {
                return gameMode == GameMode.SPECTATOR;
            }

            @Override
            public boolean isCreative() {
                return gameMode.isCreative();
            }
        };
    }
}
