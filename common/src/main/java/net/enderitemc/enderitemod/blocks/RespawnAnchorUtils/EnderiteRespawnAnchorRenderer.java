package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class EnderiteRespawnAnchorRenderer<T extends EnderiteRespawnAnchorBlockEntity>
    implements BlockEntityRenderer<T, EnderiteRespawnAnchorRenderState> {

    public EnderiteRespawnAnchorRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public EnderiteRespawnAnchorRenderState createRenderState() {
        return new EnderiteRespawnAnchorRenderState();
    }

    @Override
    public void updateRenderState(T blockEntity, EnderiteRespawnAnchorRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.shouldRenderPortal = blockEntity.shouldRenderPortal() && blockEntity.isCharged();
    }

    @Override
    public void render(EnderiteRespawnAnchorRenderState state, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (state.shouldRenderPortal && state.crumblingOverlay == null) {
            queue.submitCustom(
                matrixStack,
                this.getLayer(),
                this::renderSides
            );
        }
    }

    private void renderSides(MatrixStack.Entry matricesEntry, VertexConsumer vertexConsumer) {
        Matrix4f matrix = matricesEntry.getPositionMatrix();
        float f = this.getSideOffset();
        float g = this.getTopYOffset();
        this.renderSide(matrix, vertexConsumer, f, 1.0f - f, g, g, 1.0f - f, 1.0f - f, f, f);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
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
