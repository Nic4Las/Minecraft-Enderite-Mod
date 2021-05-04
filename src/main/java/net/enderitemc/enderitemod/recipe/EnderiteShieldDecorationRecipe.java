package net.enderitemc.enderitemod.recipe;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.item.EnderiteShield;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteShieldDecorationRecipe extends SpecialRecipe {
    public EnderiteShieldDecorationRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingInventory craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (!(itemStack3.getItem() instanceof EnderiteShield)) {
                        return false;
                    }

                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    if (itemStack3.getTagElement("BlockEntityTag") != null) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public ItemStack assemble(CraftingInventory craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    itemStack = itemStack3;
                } else if (itemStack3.getItem() instanceof EnderiteShield) {
                    itemStack2 = itemStack3.copy();
                }
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        } else {
            CompoundNBT compoundTag = itemStack.getTagElement("BlockEntityTag");
            CompoundNBT compoundTag2 = compoundTag == null ? new CompoundNBT() : compoundTag.copy();
            compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
            itemStack2.addTagElement("BlockEntityTag", compoundTag2);
            return itemStack2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_SHIELD_DECORATION_RECIPE.get();
    }
}
