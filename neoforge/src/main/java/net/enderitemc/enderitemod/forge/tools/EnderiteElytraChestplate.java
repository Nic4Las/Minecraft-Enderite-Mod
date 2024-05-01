package net.enderitemc.enderitemod.forge.tools;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EnderiteElytraChestplate extends ArmorItem {
    public EnderiteElytraChestplate(ArmorMaterial materialIn, ArmorItem.Type type, Settings properties) {
        super(materialIn, type, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 10;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.PHANTOM_MEMBRANE;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.getWorld().isClient() && (flightTicks + 1) % 20 == 0) {
            stack.damage(1, entity, e -> ((LivingEntity) e).sendEquipmentBreakStatus(EquipmentSlot.CHEST));
        }
        return isUsable(stack);
    }
}
