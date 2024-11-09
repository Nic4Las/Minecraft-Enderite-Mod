package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class EnderiteCrossbowPlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getArmPose(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState$HandState;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", cancellable = true)
    private static void getArmPose(PlayerEntityRenderState state, PlayerEntityRenderState.HandState handState, Hand hand, CallbackInfoReturnable<ArmPose> cir) {
        ItemStack itemStack2 = state.getMainHandStack();
        if (!handState.empty && !state.handSwinging && itemStack2.isOf(EnderiteTools.ENDERITE_CROSSBOW.get())
            && EnderiteCrossbow.isCharged(itemStack2)) {
            cir.setReturnValue(ArmPose.CROSSBOW_HOLD);
        }
    }

}
