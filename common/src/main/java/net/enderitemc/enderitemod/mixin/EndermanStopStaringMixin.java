package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.mob.EndermanEntity;

@Mixin(EndermanEntity.class)
public abstract class EndermanStopStaringMixin extends Entity {

    protected EndermanStopStaringMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "isPlayerStaring")
    private void dropItem(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        // Implements enderite helmet ability to calm enderman (like carved pumpkin)
        ItemStack itemStack = (ItemStack) player.getInventory().armor.get(3);
        if (itemStack.getItem() == EnderiteMod.ENDERITE_HELMET.get()) {
            info.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damageThem(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source.getSource() != null && source.getSource().getCustomName() != null) {
            if (source instanceof ProjectileDamageSource
                    && source.getSource().getCustomName().getString().equals("Enderite Arrow")) {
                source.getSource().remove(RemovalReason.DISCARDED);
                info.setReturnValue(super.damage(source, amount));
            }
        }
    }
}