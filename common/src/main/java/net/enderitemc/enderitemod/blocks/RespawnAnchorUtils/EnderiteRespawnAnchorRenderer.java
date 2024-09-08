package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;

public class EnderiteRespawnAnchorRenderer<T extends EnderiteRespawnAnchorBlockEntity>
        implements BlockEntityRenderer<T> {

    public EnderiteRespawnAnchorRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        if(entity.isCharged() && entity.shouldRenderPortal()) {
            this.renderSides(entity, matrix4f, vertexConsumers.getBuffer(this.getLayer()));
        }
    }

    private void renderSides(T entity, Matrix4f matrix, VertexConsumer vertexConsumer) {
        float f = this.getSideOffset();
        float g = this.getTopYOffset();
        this.renderSide(entity, matrix, vertexConsumer, f, 1.0f-f, g, g, 1.0f-f, 1.0f-f, f, f, Direction.UP);
    }

    private void renderSide(T entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction side) {
        vertices.vertex(model, x1, y1, z1);
        vertices.vertex(model, x2, y1, z2);
        vertices.vertex(model, x2, y2, z3);
        vertices.vertex(model, x1, y2, z4);
    }

    protected float getTopYOffset() {
        return 1.001f;
    }

    protected float getSideOffset() {
        return 0.125f;
    }

    protected RenderLayer getLayer() {
        return RenderLayer.getEndPortal();
    }
}
