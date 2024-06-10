package net.enderitemc.enderitemod.enchantments;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class VoidFloatingEnchantment extends Enchantment {

    public final EnderiteEnchantmentTarget type;

    public VoidFloatingEnchantment() {
        super(Enchantment.properties(
                ItemTags.DURABILITY_ENCHANTABLE,
                2,
                3,
                Enchantment.leveledCost(15, 5),
                Enchantment.leveledCost(20, 5),
                4,
                EquipmentSlot.MAINHAND));
        this.type = EnderiteEnchantmentTarget.NOT_ENDERITE;
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
        return EnderiteMod.CONFIG.general.allowVoidFloatingEnchantment && this.type.isAcceptableItem(stack.getItem());
    }
}