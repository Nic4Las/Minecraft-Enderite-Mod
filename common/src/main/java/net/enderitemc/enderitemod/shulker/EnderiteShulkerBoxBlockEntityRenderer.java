package net.enderitemc.enderitemod.shulker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.ShulkerBoxBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public class EnderiteShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<EnderiteShulkerBoxBlockEntity, ShulkerBoxBlockEntityRenderState> {
    private final SpriteHolder materials;
    private final ShulkerBoxBlockModel model;
    private static final Identifier ENDERITE_SHULKER_TEXTURE = Identifier.of("entity/shulker/enderite_shulker");

    public EnderiteShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.materials = ctx.spriteHolder();
        this.model = new ShulkerBoxBlockModel(ctx.getLayerModelPart(EntityModelLayers.SHULKER_BOX));
    }

    /*
    SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
            Identifier.of("entity/shulker/enderite_shulker"));
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers,
            RenderLayer::getEntityCutoutNoCull);
        this.model.render(matrixStack, vertexConsumer, light, overlay);
     */

    @Override
    public ShulkerBoxBlockEntityRenderState createRenderState() {
        return new ShulkerBoxBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(
        EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity,
        ShulkerBoxBlockEntityRenderState shulkerBoxBlockEntityRenderState,
        float f,
        Vec3d vec3d,
        @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.updateRenderState(shulkerBoxBlockEntity, shulkerBoxBlockEntityRenderState, f, vec3d, crumblingOverlayCommand);
        shulkerBoxBlockEntityRenderState.facing = shulkerBoxBlockEntity.getCachedState().get(ShulkerBoxBlock.FACING, Direction.UP);
        shulkerBoxBlockEntityRenderState.animationProgress = shulkerBoxBlockEntity.getAnimationProgress(f);
    }

    public void render(
        ShulkerBoxBlockEntityRenderState shulkerBoxBlockEntityRenderState,
        MatrixStack matrixStack,
        OrderedRenderCommandQueue orderedRenderCommandQueue,
        CameraRenderState cameraRenderState
    ) {
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(
            TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
            ENDERITE_SHULKER_TEXTURE
        );

        this.render(
            matrixStack,
            orderedRenderCommandQueue,
            shulkerBoxBlockEntityRenderState.lightmapCoordinates,
            OverlayTexture.DEFAULT_UV,
            shulkerBoxBlockEntityRenderState.facing,
            shulkerBoxBlockEntityRenderState.animationProgress,
            shulkerBoxBlockEntityRenderState.crumblingOverlay,
            spriteIdentifier,
            0
        );
    }

    public void render(
        MatrixStack matrices,
        OrderedRenderCommandQueue queue,
        int light,
        int overlay,
        Direction facing,
        float openness,
        @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay,
        SpriteIdentifier spriteId,
        int i
    ) {
        matrices.push();
        this.setTransforms(matrices, facing, openness);
        queue.submitModel(
            this.model,
            openness,
            matrices,
            spriteId.getRenderLayer(this.model::getLayer),
            light,
            overlay,
            -1,
            this.materials.getSprite(spriteId),
            i,
            crumblingOverlay
        );
        matrices.pop();
    }

    private void setTransforms(MatrixStack matrices, Direction facing, float openness) {
        matrices.translate(0.5F, 0.5F, 0.5F);
        float f = 0.9995F;
        matrices.scale(0.9995F, 0.9995F, 0.9995F);
        matrices.multiply(facing.getRotationQuaternion());
        matrices.scale(1.0F, -1.0F, -1.0F);
        matrices.translate(0.0F, -1.0F, 0.0F);
        this.model.setAngles(openness);
    }

    public void collectVertices(Direction facing, float openness, Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        this.setTransforms(matrixStack, facing, openness);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    static class ShulkerBoxBlockModel extends Model<Float> {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderLayer::getEntityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        public void setAngles(Float float_) {
            super.setAngles(float_);
            this.lid.setOrigin(0.0F, 24.0F - float_ * 0.5F * 16.0F, 0.0F);
            this.lid.yaw = 270.0F * float_ * (float) (Math.PI / 180.0);
        }
    }

}