package net.enderitemc.enderitemod.tools;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class EnderiteElytraChestplate extends ArmorItem implements FabricElytraItem {

    public EnderiteElytraChestplate(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 10;
    }

    @Override
    public boolean useCustomElytra(LivingEntity entity, ItemStack chestStack, boolean tickElytra) {
		if (this.isUsable(chestStack)) {
			if (tickElytra) {
				doVanillaElytraTick(entity, chestStack);
			}
			return true;
		}
		return false;
	}
}
