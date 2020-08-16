package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SmithingTableContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTableContainer.class)
public abstract class SmithingEnderiteSwordMixin extends AbstractRepairContainer {

    @Shadow
    public abstract void func_234654_d_(int i);

    public SmithingEnderiteSwordMixin(int syncId, PlayerInventory playerInventory, IWorldPosCallable context) {
        super(ContainerType.field_234650_u_, syncId, playerInventory, context);
    }

    private void superDecrement(int i, int amount) {
        ItemStack itemStack = this.field_234643_d_.getStackInSlot(i);
        itemStack.shrink(amount);
        this.field_234643_d_.setInventorySlotContents(i, itemStack);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "updateRepairOutput")
    private void update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.field_234643_d_.getStackInSlot(0);
        ItemStack pearls = this.field_234643_d_.getStackInSlot(1);
        if ((sword.getItem() == Registration.ENDERITE_SWORD.get()
                || sword.getItem() == Registration.ENDERITE_SHIELD.get()) && pearls.getItem() == Items.ENDER_PEARL) {
            // If new sword, basic charge is enderpearl count
            int teleport_charge = pearls.getCount();
            // Read the charge of sword
            if (sword.getTag().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                teleport_charge = Integer.parseInt(sword.getTag().get("teleport_charge").toString())
                        + pearls.getCount();
            }
            if (teleport_charge > 64) {
                teleport_charge = 64;
            }
            // Copy the same sword and put charge into it
            ItemStack newSword = sword.copy();
            newSword.getTag().putInt("teleport_charge", teleport_charge);
            this.field_234642_c_.setInventorySlotContents(0, newSword);
        }
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "func_230303_b_")
    private void alwaysTakeable(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
        // If output is enderite sword, you can always take it out
        // used to make the enderpearl charging work
        if (this.field_234642_c_.getStackInSlot(0).getItem() == Registration.ENDERITE_SWORD.get()
                || this.field_234642_c_.getStackInSlot(0).getItem() == Registration.ENDERITE_SHIELD.get()) {
            info.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "func_230301_a_")
    private void nowTake(PlayerEntity player, ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
        // Take all pearls
        if ((this.field_234643_d_.getStackInSlot(0).getItem() == Registration.ENDERITE_SWORD.get()
                || this.field_234643_d_.getStackInSlot(0).getItem() == Registration.ENDERITE_SHIELD.get())
                && this.field_234643_d_.getStackInSlot(1).getItem() == Items.ENDER_PEARL) {

            int amountToSubstract = this.field_234643_d_.getStackInSlot(1).getCount();
            // Read the charge of sword
            if (this.field_234643_d_.getStackInSlot(0).getOrCreateTag().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                int allowableSubstract = 64 - Integer
                        .parseInt(this.field_234643_d_.getStackInSlot(0).getTag().get("teleport_charge").toString());
                amountToSubstract = Math.min(allowableSubstract, amountToSubstract);
            }

            this.superDecrement(1, amountToSubstract - 1);
        }
    }
}