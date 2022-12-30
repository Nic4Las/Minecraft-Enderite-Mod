package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public class MobEntityMixin {

    @Inject(at = @At("HEAD"), method = "getPreferredEquipmentSlot", cancellable = true)
    private static void addPrefered(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        Item item = stack.getItem();
        if (item == EnderiteMod.ENDERITE_ELYTRA_SEPERATED.get()) {
            info.setReturnValue(EquipmentSlot.CHEST);
        }
        if (item == EnderiteMod.ENDERITE_SHIELD.get()) {
            info.setReturnValue(EquipmentSlot.OFFHAND);
        }
    }
}