package net.enderitemc.enderitemod.misc;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class EnderiteElytraSpecialRecipe extends SpecialCraftingRecipe {
    public EnderiteElytraSpecialRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    public boolean matches(CraftingRecipeInput craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStackInSlot(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.isOf(EnderiteMod.ENDERITE_CHESTPLATE.get())) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (!(itemStack3.getItem() instanceof ElytraItem)) {
                        return false;
                    }

                    if (!itemStack.isEmpty()) {
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

    @Override
    public ItemStack craft(CraftingRecipeInput craftingInventory, RegistryWrapper.WrapperLookup registryManager) {
        ItemStack chestplate_stack = ItemStack.EMPTY;
        ItemStack elytra_stack = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStackInSlot(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.isOf(EnderiteMod.ENDERITE_CHESTPLATE.get())) {
                    chestplate_stack = itemStack3;
                } else if (itemStack3.getItem() instanceof ElytraItem) {
                    elytra_stack = itemStack3.copy();
                }
            }
        }

        if (elytra_stack.isEmpty()) {
            return elytra_stack;
        } else {
            ItemStack result_stack = new ItemStack(EnderiteMod.ENDERITE_ELYTRA.get());
            ItemEnchantmentsComponent map1 = EnchantmentHelper.getEnchantments(chestplate_stack);
            ItemEnchantmentsComponent map2 = EnchantmentHelper.getEnchantments(elytra_stack);

            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(map1);
            // Merge new enchantment with old, if same level: level up, else: take higher level
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : map2.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> enchant = entry2.getKey();
                int level1 = entry2.getIntValue();
                int level2 = builder.getLevel(enchant);
                int level = level1 == level2 ? Math.min(level1 + 1, enchant.value().getMaxLevel()) : Math.max(level1, level2);
                builder.set(enchant, level);
            }
            EnchantmentHelper.set(result_stack, builder.build());

            ArmorTrim trim = chestplate_stack.getOrDefault(DataComponentTypes.TRIM, null);
            if(trim != null) {
                result_stack.set(DataComponentTypes.TRIM, trim);
            }

            return result_stack;
        }
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return EnderiteMod.ENDERITE_EYLTRA_SPECIAL_RECIPE.get();
    }

}