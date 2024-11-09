package net.enderitemc.enderitemod.fabriclike.misc;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;

public class EnderiteShieldRenderer implements DynamicItemRenderer {

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer.INSTANCE.render(stack, mode, matrices, vertexConsumers, light, overlay);
    }
}