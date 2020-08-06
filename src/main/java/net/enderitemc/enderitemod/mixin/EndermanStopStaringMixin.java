package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.monster.EndermanEntity;

@Mixin(EndermanEntity.class)
public abstract class EndermanStopStaringMixin extends Entity {

    protected EndermanStopStaringMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "shouldAttackPlayer")
    private void dropItem(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        // Implements enderite helmet ability to calm enderman (like carved pumpkin)
        ItemStack itemStack = (ItemStack) player.inventory.armorInventory.get(3);
        if (itemStack.getItem() == Registration.ENDERITE_HELMET.get()) {
            info.setReturnValue(false);
        }
    }
}