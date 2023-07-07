package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
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
        if (this.output.getStack(0).isOf(EnderiteMod.ENDERITE_SWORD.get())
                || this.output.getStack(0).isOf(EnderiteMod.ENDERITE_SHIELD.get())) {
            info.setReturnValue(true);
        }
    }
}