package net.enderitemc.enderitemod.shulker;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EnderiteShulkerBoxRecipe extends SpecialCraftingRecipe {

    public EnderiteShulkerBoxRecipe(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        int i = 0;
        int j = 0;
        int l = 0;

        for (int k = 0; k < inv.size(); ++k) {
            ItemStack itemStack = inv.getStack(k);
            if (!itemStack.isEmpty()) {
                if (!(itemStack.getItem() == EnderiteMod.ENDERITE_INGOT
                        || (EnderiteTag.CRAFTABLE_SHULKER_BOXES.contains(itemStack.getItem())))) {
                    return false;
                }
                if (k == 4 && (EnderiteTag.CRAFTABLE_SHULKER_BOXES.contains(itemStack.getItem()))) {
                    ++i;
                }
                if (itemStack.getItem() == EnderiteMod.ENDERITE_INGOT && (k == 1 || k == 3 || k == 5 || k == 7)) {
                    ++j;
                }
                if (itemStack.getItem() == EnderiteMod.ENDERITE_INGOT) {
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
    public ItemStack craft(CraftingInventory inv) {
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack2 = inv.getStack(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if ((EnderiteTag.CRAFTABLE_SHULKER_BOXES.contains(item))) {
                    itemStack = itemStack2;
                }
            }
        }

        ItemStack itemStack3 = EnderiteShulkerBoxBlock.getItemStack();
        if (itemStack.hasTag()) {
            itemStack3.setTag(itemStack.getTag().copy());
        }

        return itemStack3;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnderiteMod.ENDERITE_SHULKER_BOX_RECIPE;
    }

}