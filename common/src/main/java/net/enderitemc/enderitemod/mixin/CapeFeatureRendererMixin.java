package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
	@Inject(at = @At("HEAD"), method = "render*", cancellable = true)
	public void injectCapeRenderCheck(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        ItemStack itemStack = abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.isIn(EnderiteTag.ENDERITE_ELYTRA)) {
            ci.cancel();
        }
	}
}
