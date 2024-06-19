package net.enderitemc.enderitemod.fabriclike.misc;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class EnderiteShieldRenderer extends net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer implements DynamicItemRenderer {

        @Override
        public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
                        VertexConsumerProvider vertexConsumers, int light,
                        int overlay) {
                super.render(stack, mode, matrices, vertexConsumers, light, overlay);
        }

}