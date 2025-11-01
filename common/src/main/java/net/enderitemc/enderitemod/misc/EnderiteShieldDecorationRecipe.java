package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class EnderiteShieldDecorationRecipe extends SpecialCraftingRecipe {
    public EnderiteShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        if (craftingRecipeInput.getStackCount() != 2) {
            return false;
        } else {
            boolean bl = false;
            boolean bl2 = false;

            for (int i = 0; i < craftingRecipeInput.size(); i++) {
                ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() instanceof BannerItem) {
                        if (bl2) {
                            return false;
                        }

                        bl2 = true;
                    } else {
                        if (!(itemStack.getItem() instanceof EnderiteShield)) {
                            return false;
                        }

                        if (bl) {
                            return false;
                        }

                        BannerPatternsComponent bannerPatternsComponent = itemStack.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
                        if (!bannerPatternsComponent.layers().isEmpty()) {
                            return false;
                        }

                        bl = true;
                    }
                }
            }

            return bl && bl2;
        }
    }

    public ItemStack craft(CraftingRecipeInput craftingInventory, RegistryWrapper.WrapperLookup registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStackInSlot(i);
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
        }
        itemStack2.set(DataComponentTypes.BANNER_PATTERNS, itemStack.get(DataComponentTypes.BANNER_PATTERNS));
        itemStack2.set(DataComponentTypes.BASE_COLOR, ((BannerItem) itemStack.getItem()).getColor());
        return itemStack2;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return EnderiteTools.ENDERITE_SHIELD_DECORATION_RECIPE.get();
    }
}