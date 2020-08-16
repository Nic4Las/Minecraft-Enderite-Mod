package net.enderitemc.enderitemod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class VoidFloatingEnchantment extends Enchantment {

    public final EnderiteEnchantmentTarget type;

    public VoidFloatingEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentType.BREAKABLE,
                new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
        this.type = EnderiteEnchantmentTarget.NOT_ENDERITE;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 5 * level + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean func_230309_h_() {
        return false;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return false;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return this.type.isAcceptableItem(stack.getItem());
    }
}