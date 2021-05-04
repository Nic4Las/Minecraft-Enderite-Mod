package net.enderitemc.enderitemod.recipe;

import java.util.Map;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderiteElytraSpecialRecipe extends SpecialRecipe {
    public EnderiteElytraSpecialRecipe(ResourceLocation identifier) {
        super(identifier);
    }

    public boolean matches(CraftingInventory craftingInventory, World world) {
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

    public ItemStack assemble(CraftingInventory craftingInventory) {
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
    public IRecipeSerializer<?> getSerializer() {
        return Registration.ENDERITE_EYLTRA_SPECIAL_RECIPE.get();
    }

}
