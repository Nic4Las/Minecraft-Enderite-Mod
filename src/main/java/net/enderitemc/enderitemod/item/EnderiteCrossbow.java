package net.enderitemc.enderitemod.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

public class EnderiteCrossbow extends CrossbowItem {

    public EnderiteCrossbow(Properties propertiesIn) {
        super(propertiesIn);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return super.getUseDuration(stack) + 10;
    }

    public static int getChargeTime(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 35 : 35 - 7 * i;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

}