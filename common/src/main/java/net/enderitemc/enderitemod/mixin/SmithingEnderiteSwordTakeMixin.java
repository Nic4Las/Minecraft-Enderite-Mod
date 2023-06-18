package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.LegacySmithingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LegacySmithingScreenHandler.class)
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
        if ((this.input.getStack(0).getItem() == EnderiteMod.ENDERITE_SWORD.get()
                || this.input.getStack(0).getItem() == EnderiteMod.ENDERITE_SHIELD.get())
                && this.input.getStack(1).getItem() == Items.ENDER_PEARL) {

            int amountToSubstract = this.input.getStack(1).getCount();
            // Read the charge of sword
            if (this.input.getStack(0).getNbt().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                int allowableSubstract = 64
                        - Integer.parseInt(this.input.getStack(0).getNbt().get("teleport_charge").asString());
                amountToSubstract = Math.min(allowableSubstract, amountToSubstract);
            }

            this.superDecrement(1, amountToSubstract - 1);
        }
    }
}