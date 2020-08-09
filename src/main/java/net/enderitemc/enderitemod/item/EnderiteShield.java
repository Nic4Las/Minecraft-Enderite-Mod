package net.enderitemc.enderitemod.item;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

public class EnderiteShield extends ShieldItem {

    public EnderiteShield(Properties builder) {
        super(builder);
    }

    @Override
    public int getItemEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairMaterial().test(ingredient);
    }

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return stack.getItem() instanceof EnderiteShield;
    }

}