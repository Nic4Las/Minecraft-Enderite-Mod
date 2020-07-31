package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordTakeMixin extends ForgingScreenHandler {

    @Shadow
    public abstract void method_29539(int i);

    public SmithingEnderiteSwordTakeMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    private void superDecrement(int i, int amount) {
        ItemStack itemStack = this.input.getStack(i);
        itemStack.decrement(amount);
        this.input.setStack(i, itemStack);
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "onTakeOutput")
    private void nowTake(PlayerEntity player, ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
        // Take all pearls
        if (this.input.getStack(0).getItem() == EnderiteMod.ENDERITE_SWORD
                && this.input.getStack(1).getItem() == Items.ENDER_PEARL) {
            this.superDecrement(1, this.input.getStack(1).getCount());
        }
    }
}