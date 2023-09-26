package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

public class EnderiteShulkerBoxRecipe extends SpecialCraftingRecipe {

    public EnderiteShulkerBoxRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inv, World world) {
        int i = 0;
        int j = 0;
        int l = 0;

        for (int k = 0; k < inv.size(); ++k) {
            ItemStack itemStack = inv.getStack(k);
            if (!itemStack.isEmpty()) {
                if (!(itemStack.isOf(EnderiteMod.ENDERITE_INGOT.get())
                        || (itemStack.isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES)))) {
                    return false;
                }
                if (k == 4 && (itemStack.isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES))) {
                    ++i;
                }
                if (itemStack.isOf(EnderiteMod.ENDERITE_INGOT.get()) && (k == 1 || k == 3 || k == 5 || k == 7)) {
                    ++j;
                }
                if (itemStack.isOf(EnderiteMod.ENDERITE_INGOT.get())) {
                    ++l;
                }
                if (l > 4 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 4;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack2 = inv.getStack(i);
            if (!itemStack2.isEmpty()) {
                if ((itemStack2.isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES))) {
                    itemStack = itemStack2;
                }
            }
        }

        ItemStack itemStack3 = EnderiteShulkerBoxBlock.getItemStack();
        if (itemStack.hasNbt()) {
            itemStack3.setNbt(itemStack.getNbt().copy());
        }

        return itemStack3;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnderiteMod.ENDERITE_SHULKER_BOX_RECIPE.get();
    }

}