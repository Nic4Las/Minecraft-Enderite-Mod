package net.enderitemc.enderitemod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderiteEndermanMixin extends LivingEntity {

    protected EnderiteEndermanMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    private void enderitemod$damageThem(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source.getDirectEntity() != null && source.getDirectEntity().getCustomName() != null) {
            if (source.is(DamageTypeTags.IS_PROJECTILE)
                && source.getDirectEntity().getCustomName().getString().equals("Enderite Arrow")) {
                source.getDirectEntity().remove(RemovalReason.DISCARDED);
                info.setReturnValue(super.hurtServer(world, source, amount));
            }
        }
    }
}
