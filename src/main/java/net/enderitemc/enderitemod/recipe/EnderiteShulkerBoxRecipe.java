package net.enderitemc.enderitemod.recipe;

import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteShulkerBoxRecipe extends CustomRecipe {
    public EnderiteShulkerBoxRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingContainer craftingInventory, Level world) {
        int i = 0;
        int j = 0;
        int l = 0;

        for (int k = 0; k < craftingInventory.getContainerSize(); ++k) {
            ItemStack itemStack = craftingInventory.getItem(k);
            if (!itemStack.isEmpty()) {
                if (!(itemStack.getItem() == Registration.ENDERITE_INGOT.get()
                        || itemStack.is(EnderiteTag.CRAFTABLE_SHULKER_BOXES))) {
                    return false;
                }
                if (k == 4 && itemStack.is(EnderiteTag.CRAFTABLE_SHULKER_BOXES)) {
                    ++i;
                }
                if (itemStack.getItem() == Registration.ENDERITE_INGOT.get()
                        && (k == 1 || k == 3 || k == 5 || k == 7)) {
                    ++j;
                }
                if (itemStack.getItem() == Registration.ENDERITE_INGOT.get()) {
                    ++l;
                }
                if (l > 4 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 4;
    }

    public ItemStack assemble(CraftingContainer craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack2 = craftingInventory.getItem(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (item.getDefaultInstance().is(EnderiteTag.CRAFTABLE_SHULKER_BOXES)) {
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
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 9;
    }

    public RecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_SHULKER_BOX_RECIPE.get();
    }
}
