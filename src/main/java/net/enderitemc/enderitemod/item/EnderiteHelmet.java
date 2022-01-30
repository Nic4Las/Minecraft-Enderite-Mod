package net.enderitemc.enderitemod.item;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class EnderiteHelmet extends ArmorItem {

    public EnderiteHelmet(ArmorMaterial materialIn, EquipmentSlot slot, Properties properties) {
        super(materialIn, slot, properties);
    }

    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return true;
    }

}