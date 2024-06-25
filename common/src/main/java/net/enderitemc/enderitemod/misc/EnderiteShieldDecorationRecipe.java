package net.enderitemc.enderitemod.misc;

import com.mojang.serialization.DynamicOps;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class EnderiteShieldDecorationRecipe extends SpecialCraftingRecipe {
    public EnderiteShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
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
                    NbtCompound compoundTag = new NbtCompound();
                    NbtCompound compoundTag2 = itemStack3.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.of(compoundTag)).copyNbt();
                    if(compoundTag2.contains("Base")) {
                        int color = compoundTag2.getInt("Base");
                        itemStack3.set(DataComponentTypes.BASE_COLOR, DyeColor.byId(color));
                    }
                    if(compoundTag2.contains("Patterns")) {
                        NbtElement nbtList = compoundTag2.get("Patterns");
                        itemStack3.set(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.CODEC.parse(NbtOps.INSTANCE, nbtList).getOrThrow());
                    }
                    if (!itemStack.isEmpty()) {
                        return false;
                    }
                    BannerPatternsComponent bannerPatternsComponent = itemStack3.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
                    if (!bannerPatternsComponent.layers().isEmpty()) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }

    public ItemStack craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
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
        itemStack2.set(DataComponentTypes.BASE_COLOR, ((BannerItem)itemStack.getItem()).getColor());
        return itemStack2;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return EnderiteTools.ENDERITE_SHIELD_DECORATION_RECIPE.get();
    }
}