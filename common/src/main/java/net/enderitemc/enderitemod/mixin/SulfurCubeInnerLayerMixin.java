package net.enderitemc.enderitemod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.enderitemc.enderitemod.misc.EnderiteSulfurCubeRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SulfurCubeInnerLayer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SulfurCubeInnerLayer.class)
public class SulfurCubeInnerLayerMixin {
    @Unique
    private static final float ENDERITEMOD_CRACKED_ORE_SCALE = 15.0F / 16.0F;

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
        ),
        method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;FF)V"
    )
    private void enderitemod$scaleCrackedEnderiteOre(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int lightCoords,
        SulfurCubeRenderState state,
        float yRot,
        float xRot,
        CallbackInfo ci
    ) {
        if (((EnderiteSulfurCubeRenderState) state).enderitemod$isCrackedEnderiteOre()) {
            poseStack.translate(0.5F, 0.5F, 0.5F);
            poseStack.scale(ENDERITEMOD_CRACKED_ORE_SCALE, ENDERITEMOD_CRACKED_ORE_SCALE, ENDERITEMOD_CRACKED_ORE_SCALE);
            poseStack.translate(-0.5F, -0.5F, -0.5F);
        }
    }
}
