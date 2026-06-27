package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class EnderiteShieldDecorationRecipe extends CustomRecipe {
    public EnderiteShieldDecorationRecipe() {
        super();
    }

    public boolean matches(CraftingInput craftingRecipeInput, Level world) {
        if (craftingRecipeInput.ingredientCount() != 2) {
            return false;
        } else {
            boolean bl = false;
            boolean bl2 = false;

            for (int i = 0; i < craftingRecipeInput.size(); i++) {
                ItemStack itemStack = craftingRecipeInput.getItem(i);
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

                        BannerPatternLayers bannerPatternsComponent = itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
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

    public ItemStack assemble(CraftingInput craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
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
        }
        itemStack2.set(DataComponents.BANNER_PATTERNS, itemStack.get(DataComponents.BANNER_PATTERNS));
        itemStack2.set(DataComponents.BASE_COLOR, ((BannerItem) itemStack.getItem()).getColor());
        return itemStack2;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return EnderiteTools.ENDERITE_SHIELD_DECORATION_RECIPE.get();
    }
}
