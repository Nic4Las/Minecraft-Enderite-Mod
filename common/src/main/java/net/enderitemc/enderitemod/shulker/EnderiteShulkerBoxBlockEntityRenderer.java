package net.enderitemc.enderitemod.shulker;

import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class EnderiteShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<EnderiteShulkerBoxBlockEntity> {
    private final ShulkerEntityModel<?> model;
    private static final Identifier TEXTURE = Identifier.of("textures/entity/shulker/enderite_shulker.png");
    private static final RenderLayer ENDERITE_SHULKER;

    public EnderiteShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new ShulkerEntityModel<>(ctx.getLayerModelPart(EntityModelLayers.SHULKER));
    }

    public void render(EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasWorld()) {
            BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
            if (blockState.getBlock() instanceof EnderiteShulkerBoxBlock) {
                direction = (Direction) blockState.get(EnderiteShulkerBoxBlock.FACING);
            }
        }

        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
                Identifier.of("entity/shulker/enderite_shulker"));
        matrixStack.push();
        matrixStack.translate(0.5f, 0.5f, 0.5f);
        float g = 0.9995f;
        matrixStack.scale(0.9995f, 0.9995f, 0.9995f);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0f, -1.0f, -1.0f);
        matrixStack.translate(0.0f, -1.0f, 0.0f);
        ModelPart modelPart = this.model.getLid();
        modelPart.setPivot(0.0f, 24.0f - shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5f * 16.0f, 0.0f);
        modelPart.yaw = 270.0f * shulkerBoxBlockEntity.getAnimationProgress(f) * ((float) Math.PI / 180);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider,
                RenderLayer::getEntityCutoutNoCull);
        this.model.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
    }

    static {
        ENDERITE_SHULKER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    }

}