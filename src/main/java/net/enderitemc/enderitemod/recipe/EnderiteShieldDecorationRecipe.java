package net.enderitemc.enderitemod.recipe;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.item.EnderiteShield;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteShieldDecorationRecipe extends CustomRecipe {
    public EnderiteShieldDecorationRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingContainer craftingInventory, Level world) {
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

    public ItemStack assemble(CraftingContainer craftingInventory) {
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
            CompoundTag compoundTag = itemStack.getTagElement("BlockEntityTag");
            CompoundTag compoundTag2 = compoundTag == null ? new CompoundTag() : compoundTag.copy();
            compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
            itemStack2.addTagElement("BlockEntityTag", compoundTag2);
            return itemStack2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_SHIELD_DECORATION_RECIPE.get();
    }
}
