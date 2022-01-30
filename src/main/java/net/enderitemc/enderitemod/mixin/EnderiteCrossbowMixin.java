package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.item.EnderiteCrossbow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;



@Mixin(CrossbowItem.class)
public abstract class EnderiteCrossbowMixin {

    @Inject(at = @At("RETURN"), method = "getArrow")
    private static void renameThem(Level level, LivingEntity entity, ItemStack crossbow, ItemStack arrow, CallbackInfoReturnable<AbstractArrow> info) {
        if(crossbow.getItem() instanceof EnderiteCrossbow) {
            //info.getReturnValue().setCustomName(Component.nullToEmpty("Enderite Arrow"));
            info.getReturnValue().getPersistentData().putBoolean("IsEnderiteArrow", true);
        }
    }
}