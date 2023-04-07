package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.LegacySmithingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;

@Mixin(LegacySmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordOutputMixin extends ForgingScreenHandler {

    public SmithingEnderiteSwordOutputMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "canTakeOutput")
    private void alwaysTakeable(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
        // If output is enderite sword, you can always take it out
        // used to make the enderpearl charging work
        if (this.output.getStack(0).getItem() == EnderiteMod.ENDERITE_SWORD
                || this.output.getStack(0).getItem() == EnderiteMod.ENDERITE_SHIELD) {
            info.setReturnValue(true);
        }
    }
}