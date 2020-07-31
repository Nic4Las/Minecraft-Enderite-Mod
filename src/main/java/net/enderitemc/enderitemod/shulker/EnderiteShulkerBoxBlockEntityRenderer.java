package net.enderitemc.enderitemod.shulker;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class EnderiteShulkerBoxBlockEntityRenderer extends BlockEntityRenderer<EnderiteShulkerBoxBlockEntity> {
    private final ShulkerEntityModel<?> model;
    private static final Identifier TEXTURE = new Identifier("textures/entity/shulker/enderite_shulker.png");
    private static final RenderLayer ENDERITE_SHULKER;

    public EnderiteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
        this.model = new ShulkerEntityModel<>();
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

        matrixStack.push();
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        // float g = 0.9995F;
        matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        matrixStack.translate(0.0D, -1.0D, 0.0D);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(ENDERITE_SHULKER);
        this.model.getBottomShell().render(matrixStack, vertexConsumer, i, j);
        matrixStack.translate(0.0D, (double) (-shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F), 0.0D);
        matrixStack.multiply(
                Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F * shulkerBoxBlockEntity.getAnimationProgress(f)));
        this.model.getTopShell().render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
    }

    static {
        ENDERITE_SHULKER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    }

}