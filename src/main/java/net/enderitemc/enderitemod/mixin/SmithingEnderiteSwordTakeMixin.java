package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordTakeMixin extends ForgingScreenHandler {

    @Shadow
    public abstract void decrementStack(int i);

    public SmithingEnderiteSwordTakeMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    private void superDecrement(int i, int amount) {
        ItemStack itemStack = this.input.getStack(i);
        itemStack.decrement(amount);
        this.input.setStack(i, itemStack);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "onTakeOutput")
    private void nowTake(PlayerEntity player, ItemStack itemStack, CallbackInfo info) {
        // Take all pearls
        ItemStack sword = this.input.getStack(1);
        ItemStack pearls = this.input.getStack(2);
        if ((sword.getItem() == EnderiteMod.ENDERITE_SWORD
                || sword.getItem() == EnderiteMod.ENDERITE_SHIELD)
                && pearls.getItem() == Items.ENDER_PEARL) {

            int amountToSubstract = pearls.getCount();
            // Read the charge of sword
            if (sword.getNbt().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                int allowableSubstract = 64
                        - Integer.parseInt(sword.getNbt().get("teleport_charge").asString());
                amountToSubstract = Math.min(allowableSubstract, amountToSubstract);
            }

            this.superDecrement(2, amountToSubstract - 1);
        }
    }
}