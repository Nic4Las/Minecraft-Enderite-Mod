package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class EnderiteRespawnAnchorRenderer<T extends EnderiteRespawnAnchorBlockEntity>
    implements BlockEntityRenderer<T, EnderiteRespawnAnchorRenderState> {

    public EnderiteRespawnAnchorRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public EnderiteRespawnAnchorRenderState createRenderState() {
        return new EnderiteRespawnAnchorRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, EnderiteRespawnAnchorRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.shouldRenderPortal = blockEntity.shouldRenderPortal() && blockEntity.isCharged();
    }

    @Override
    public void submit(EnderiteRespawnAnchorRenderState state, PoseStack matrixStack, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state.shouldRenderPortal && state.breakProgress == null) {
            queue.submitCustomGeometry(
                matrixStack,
                this.getLayer(),
                this::renderSides
            );
        }
    }

    private void renderSides(PoseStack.Pose matricesEntry, VertexConsumer vertexConsumer) {
        Matrix4f matrix = matricesEntry.pose();
        float f = this.getSideOffset();
        float g = this.getTopYOffset();
        this.renderSide(matrix, vertexConsumer, f, 1.0f - f, g, g, 1.0f - f, 1.0f - f, f, f);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        vertices.addVertex(model, x1, y1, z1);
        vertices.addVertex(model, x2, y1, z2);
        vertices.addVertex(model, x2, y2, z3);
        vertices.addVertex(model, x1, y2, z4);
    }

    protected float getTopYOffset() {
        return 1.001f;
    }

    protected float getSideOffset() {
        return 0.125f;
    }

    protected RenderType getLayer() {
        return RenderTypes.endPortal();
    }
}
