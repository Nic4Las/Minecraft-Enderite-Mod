package net.enderitemc.enderitemod.fabriclike.tools;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EnderiteElytraSeperated extends ElytraItem implements FabricElytraItem {

    public EnderiteElytraSeperated(net.minecraft.item.Item.Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public EquipmentSlot getSlotType() {
        return EquipmentSlot.CHEST;
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1;
    }

    @Override
    public int getEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }

}