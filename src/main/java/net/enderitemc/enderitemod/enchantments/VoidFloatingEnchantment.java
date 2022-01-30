package net.enderitemc.enderitemod.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class VoidFloatingEnchantment extends Enchantment {

    public final EnderiteEnchantmentTarget type;

    public VoidFloatingEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE,
                new EquipmentSlot[] { EquipmentSlot.MAINHAND });
        this.type = EnderiteEnchantmentTarget.NOT_ENDERITE;
    }

    @Override
    public int getMinCost(int level) {
        return 5 * level + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return this.type.isAcceptableItem(stack.getItem());
    }
}