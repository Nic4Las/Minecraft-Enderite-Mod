package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class ElytraPlayerEntityMixin extends ElytraLivingEntityMixin {

    @Shadow
    public abstract boolean checkFallFlying();

    @Shadow
    public abstract void startFallFlying();

    protected ElytraPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), method = "checkFallFlying", cancellable = true)
    public void checkFallFlying(CallbackInfoReturnable<Boolean> info) {
        // Check if player is wearing usable gear to start flying
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.getItem().isIn(EnderiteTag.ENDERITE_ELYTRA)
                && itemStack.getDamage() < itemStack.getMaxDamage() - 10) {
            this.startFallFlying();
            info.setReturnValue(true);
        }
    }
}