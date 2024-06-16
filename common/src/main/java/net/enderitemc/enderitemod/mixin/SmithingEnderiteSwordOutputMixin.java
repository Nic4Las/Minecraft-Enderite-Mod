package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordOutputMixin extends ForgingScreenHandler {

    public SmithingEnderiteSwordOutputMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "canTakeOutput")
    private void alwaysTakeable(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
        // If output is enderite sword, you can always take it out
        // used to make the enderpearl charging work
        ItemStack inputStack = this.input.getStack(1);
        ItemStack stack = this.output.getStack(0);
        if (stack.isOf(EnderiteTools.ENDERITE_SWORD.get())
                || stack.isOf(EnderiteTools.ENDERITE_SHIELD.get())) {
            info.setReturnValue(inputStack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0) < EnderiteMod.CONFIG.tools.maxTeleportCharge);
        }
    }
}