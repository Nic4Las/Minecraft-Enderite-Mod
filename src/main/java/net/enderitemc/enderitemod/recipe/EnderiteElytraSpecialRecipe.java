package net.enderitemc.enderitemod.recipe;

import java.util.Map;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteElytraSpecialRecipe extends CustomRecipe {
    public EnderiteElytraSpecialRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingContainer craftingInventory, Level world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() == Registration.ENDERITE_CHESTPLATE.get()) {
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

    public ItemStack assemble(CraftingContainer craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
            ItemStack itemStack3 = craftingInventory.getItem(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() == Registration.ENDERITE_CHESTPLATE.get()) {
                    itemStack = itemStack3;
                } else if (itemStack3.getItem() instanceof ElytraItem) {
                    itemStack2 = itemStack3.copy();
                }
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        } else {
            ItemStack stackO = new ItemStack(Registration.ENDERITE_ELYTRA.get());
            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemStack);
            Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(itemStack2);

            for (Map.Entry<Enchantment, Integer> entry2 : map2.entrySet()) {
                map1.merge(entry2.getKey(), entry2.getValue(), (v1, v2) -> v1 < v2 ? v2 : v1);
            }

            EnchantmentHelper.setEnchantments(map1, stackO);

            return stackO;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_EYLTRA_SPECIAL_RECIPE.get();
    }

}
