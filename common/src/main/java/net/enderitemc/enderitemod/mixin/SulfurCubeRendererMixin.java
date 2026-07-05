package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteSulfurCubeRenderState;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SulfurCubeRenderer.class)
public class SulfurCubeRendererMixin {
    @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/monster/cubemob/SulfurCube;Lnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;F)V")
    private void enderitemod$extractCrackedEnderiteOre(SulfurCube entity, SulfurCubeRenderState state, float partialTicks, CallbackInfo ci) {
        ((EnderiteSulfurCubeRenderState) state).enderitemod$setCrackedEnderiteOre(
            EnderiteMod.CONFIG.general.sulfurCubeCrackedEnderiteOreRenderFix
                &&
            entity.getBodyArmorItem().is(EnderiteMod.CRACKED_ENDERITE_ORE_ITEM.get())
        );
    }
}
