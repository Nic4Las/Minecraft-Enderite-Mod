package net.enderitemc.enderitemod.recipe;

import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteShulkerBoxRecipe extends SpecialRecipe {
    public EnderiteShulkerBoxRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingInventory craftingInventory, World world) {
        int i = 0;
        int j = 0;

        for (int k = 0; k < craftingInventory.getSizeInventory(); ++k) {
            ItemStack itemStack = craftingInventory.getStackInSlot(k);
            if (!itemStack.isEmpty()) {
                if (!(itemStack.getItem() == Registration.ENDERITE_INGOT.get()
                        || itemStack.getItem().isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES))) {
                    return false;
                }
                if (k == 4 && itemStack.getItem().isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES)) {
                    ++i;
                }
                if (itemStack.getItem() == Registration.ENDERITE_INGOT.get()
                        && (k == 1 || k == 3 || k == 5 || k == 7)) {
                    ++j;
                }
                if (j > 4 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 4;
    }

    public ItemStack getCraftingResult(CraftingInventory craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStackInSlot(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (item.isIn(EnderiteTag.CRAFTABLE_SHULKER_BOXES)) {
                    itemStack = itemStack2;
                }
            }
        }

        ItemStack itemStack3 = EnderiteShulkerBox.getStack();
        if (itemStack.hasTag()) {
            itemStack3.setTag(itemStack.getTag().copy());
        }

        return itemStack3;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canFit(int width, int height) {
        return width * height >= 9;
    }

    public IRecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_SHULKER_BOX_RECIPE.get();
    }
}
