package net.enderitemc.enderitemod.forge.tests;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.function.Consumer;
import java.util.function.Predicate;

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
}
