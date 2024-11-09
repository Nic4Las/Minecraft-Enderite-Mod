package net.enderitemc.enderitemod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EnderiteEndermanMixin extends LivingEntity {

    protected EnderiteEndermanMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
    private void damageThem(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source.getSource() != null && source.getSource().getCustomName() != null) {
            if (source.isIn(DamageTypeTags.IS_PROJECTILE)
                && source.getSource().getCustomName().getString().equals("Enderite Arrow")) {
                source.getSource().remove(RemovalReason.DISCARDED);
                info.setReturnValue(super.damage(world, source, amount));
            }
        }
    }
}