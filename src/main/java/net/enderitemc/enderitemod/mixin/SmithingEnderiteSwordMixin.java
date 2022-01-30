package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.ContainerLevelAccess;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingMenu.class)
public abstract class SmithingEnderiteSwordMixin extends ItemCombinerMenu {

    @Shadow
    public abstract void shrinkStackInSlot(int i);

    public SmithingEnderiteSwordMixin(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(MenuType.SMITHING, syncId, playerInventory, context);
    }

    private void superDecrement(int i, int amount) {
        ItemStack itemStack = this.inputSlots.getItem(i);
        itemStack.shrink(amount);
        this.inputSlots.setItem(i, itemStack);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "createResult")
    private void update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.inputSlots.getItem(0);
        ItemStack pearls = this.inputSlots.getItem(1);
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
            this.resultSlots.setItem(0, newSword);
        }
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "mayPickup")
    private void alwaysTakeable(Player player, boolean present, CallbackInfoReturnable<Boolean> info) {
        // If output is enderite sword, you can always take it out
        // used to make the enderpearl charging work
        if (this.resultSlots.getItem(0).getItem() == Registration.ENDERITE_SWORD.get()
                || this.resultSlots.getItem(0).getItem() == Registration.ENDERITE_SHIELD.get()) {
            info.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "onTake")
    private void nowTake(Player player, ItemStack itemStack, CallbackInfo info) {
        // Take all pearls
        if ((this.inputSlots.getItem(0).getItem() == Registration.ENDERITE_SWORD.get()
                || this.inputSlots.getItem(0).getItem() == Registration.ENDERITE_SHIELD.get())
                && this.inputSlots.getItem(1).getItem() == Items.ENDER_PEARL) {

            int amountToSubstract = this.inputSlots.getItem(1).getCount();
            // Read the charge of sword
            if (this.inputSlots.getItem(0).getOrCreateTag().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                int allowableSubstract = 64
                        - Integer.parseInt(this.inputSlots.getItem(0).getTag().get("teleport_charge").toString());
                amountToSubstract = Math.min(allowableSubstract, amountToSubstract);
            }

            this.superDecrement(1, amountToSubstract - 1);
        }
    }
}