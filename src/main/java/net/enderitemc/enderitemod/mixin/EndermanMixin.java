package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;

@Mixin(EnderMan.class)
public abstract class EndermanMixin extends Entity {

    protected EndermanMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void damageThem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source instanceof IndirectEntityDamageSource && source.getDirectEntity() != null) {
            if(source.getDirectEntity().getPersistentData().contains("IsEnderiteArrow")) {
                if (source.getDirectEntity().getPersistentData().getBoolean("IsEnderiteArrow")) {
                    source.getDirectEntity().remove(RemovalReason.DISCARDED);
                    info.setReturnValue(super.hurt(source, amount));
                }
            }
        }
    }
}