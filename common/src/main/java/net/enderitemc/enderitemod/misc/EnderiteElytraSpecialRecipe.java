package net.enderitemc.enderitemod.misc;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.Level;

import static net.enderitemc.enderitemod.ComponentHolderWrapper.contains;

public class EnderiteElytraSpecialRecipe extends CustomRecipe {
    public EnderiteElytraSpecialRecipe() {
        super();
    }

    public boolean matches(CraftingInput craftingInventory, Level world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.is(EnderiteMod.ENDERITE_CHESTPLATE.get())) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (!contains(itemStack3,DataComponents.GLIDER)) {
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
    public ItemStack assemble(CraftingInput craftingInventory) {
        ItemStack chestplate_stack = ItemStack.EMPTY;
        ItemStack elytra_stack = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.is(EnderiteMod.ENDERITE_CHESTPLATE.get())) {
                    chestplate_stack = itemStack3;
                } else if (contains(itemStack3,  DataComponents.GLIDER)) {
                    elytra_stack = itemStack3.copy();
                }
            }
        }

        if (elytra_stack.isEmpty()) {
            return elytra_stack;
        } else {
            ItemStack result_stack = new ItemStack(EnderiteMod.ENDERITE_ELYTRA.get());
            ItemEnchantments map1 = EnchantmentHelper.getEnchantmentsForCrafting(chestplate_stack);
            ItemEnchantments map2 = EnchantmentHelper.getEnchantmentsForCrafting(elytra_stack);

            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(map1);
            // Merge new enchantment with old, if same level: level up, else: take higher level
            for (Object2IntMap.Entry<Holder<Enchantment>> entry2 : map2.entrySet()) {
                Holder<Enchantment> enchant = entry2.getKey();
                int level1 = entry2.getIntValue();
                int level2 = builder.getLevel(enchant);
                int level = level1 == level2 ? Math.min(level1 + 1, enchant.value().getMaxLevel()) : Math.max(level1, level2);
                builder.set(enchant, level);
            }
            EnchantmentHelper.setEnchantments(result_stack, builder.toImmutable());

            ArmorTrim trim = chestplate_stack.getOrDefault(DataComponents.TRIM, elytra_stack.getOrDefault(DataComponents.TRIM, null));
            if (trim != null) {
                result_stack.set(DataComponents.TRIM, trim);
            }

            return result_stack;
        }
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return EnderiteMod.ENDERITE_EYLTRA_SPECIAL_RECIPE.get();
    }

}
