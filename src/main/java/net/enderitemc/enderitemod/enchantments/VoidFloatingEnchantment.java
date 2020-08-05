package net.enderitemc.enderitemod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class VoidFloatingEnchantment extends Enchantment {

    public final EnderiteEnchantmentTarget type;

    public VoidFloatingEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.BREAKABLE,
                new EquipmentSlot[] { EquipmentSlot.MAINHAND });
        this.type = EnderiteEnchantmentTarget.NOT_ENDERITE;
    }

    @Override
    public int getMinPower(int level) {
        return 5 * level + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return this.type.isAcceptableItem(stack.getItem());
    }
}