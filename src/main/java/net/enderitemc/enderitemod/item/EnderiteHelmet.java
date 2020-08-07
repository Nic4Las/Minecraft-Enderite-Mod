package net.enderitemc.enderitemod.item;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public class EnderiteHelmet extends ArmorItem {

    public EnderiteHelmet(IArmorMaterial materialIn, EquipmentSlotType slot, Properties p_i48534_3_) {
        super(materialIn, slot, p_i48534_3_);
    }

    @Override
    public boolean isEnderMask(ItemStack stack, PlayerEntity player, EndermanEntity endermanEntity) {
        return true;
    }

}