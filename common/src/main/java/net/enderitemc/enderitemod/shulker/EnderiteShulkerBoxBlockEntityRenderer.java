package net.enderitemc.enderitemod.shulker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;

public class EnderiteShulkerBoxBlockEntityRenderer implements BlockEntityRenderer<EnderiteShulkerBoxBlockEntity, ShulkerBoxRenderState> {
    private final SpriteGetter sprites;
    private final ShulkerBoxBlockModel model;
    private static final Identifier ENDERITE_SHULKER_TEXTURE = Identifier.parse("entity/shulker/enderite_shulker");

    public EnderiteShulkerBoxBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.sprites = ctx.sprites();
        this.model = new ShulkerBoxBlockModel(ctx.bakeLayer(ModelLayers.SHULKER_BOX));
    }

    /*
    SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE,
            Identifier.of("entity/shulker/enderite_shulker"));
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers,
            RenderLayer::getEntityCutoutNoCull);
        this.model.render(matrixStack, vertexConsumer, light, overlay);
     */

    @Override
    public ShulkerBoxRenderState createRenderState() {
        return new ShulkerBoxRenderState();
    }

    @Override
    public void extractRenderState(
        EnderiteShulkerBoxBlockEntity shulkerBoxBlockEntity,
        ShulkerBoxRenderState shulkerBoxBlockEntityRenderState,
        float f,
        Vec3 vec3d,
        @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand
    ) {
        BlockEntityRenderer.super.extractRenderState(shulkerBoxBlockEntity, shulkerBoxBlockEntityRenderState, f, vec3d, crumblingOverlayCommand);
        shulkerBoxBlockEntityRenderState.direction = shulkerBoxBlockEntity.getBlockState().getValueOrElse(ShulkerBoxBlock.FACING, Direction.UP);
        shulkerBoxBlockEntityRenderState.progress = shulkerBoxBlockEntity.getAnimationProgress(f);
    }

    public void submit(
        ShulkerBoxRenderState shulkerBoxBlockEntityRenderState,
        PoseStack matrixStack,
        SubmitNodeCollector orderedRenderCommandQueue,
        CameraRenderState cameraRenderState
    ) {
        SpriteId spriteIdentifier = new SpriteId(
            Sheets.SHULKER_SHEET,
            ENDERITE_SHULKER_TEXTURE
        );

        this.render(
            matrixStack,
            orderedRenderCommandQueue,
            shulkerBoxBlockEntityRenderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            shulkerBoxBlockEntityRenderState.direction,
            shulkerBoxBlockEntityRenderState.progress,
            shulkerBoxBlockEntityRenderState.breakProgress,
            spriteIdentifier,
            0
        );
    }

    public void render(
        PoseStack matrices,
        SubmitNodeCollector queue,
        int light,
        int overlay,
        Direction facing,
        float openness,
        @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
        SpriteId spriteId,
        int i
    ) {
        matrices.pushPose();
        this.setTransforms(matrices, facing);
        this.model.setupAnim(openness);
        queue.submitModel(
            this.model,
            openness,
            matrices,
            spriteId.renderType(this.model::renderType),
            light,
            overlay,
            -1,
            this.sprites.get(spriteId),
            i,
            crumblingOverlay
        );
        matrices.popPose();
    }

    private void setTransforms(PoseStack matrices, Direction facing) {
        matrices.translate(0.5F, 0.5F, 0.5F);
        float f = 0.9995F;
        matrices.scale(0.9995F, 0.9995F, 0.9995F);
        matrices.mulPose(facing.getRotation());
        matrices.scale(1.0F, -1.0F, -1.0F);
        matrices.translate(0.0F, -1.0F, 0.0F);
    }

    public void collectVertices(Direction facing, float openness, Consumer<Vector3fc> vertices) {
        PoseStack matrixStack = new PoseStack();
        this.setTransforms(matrixStack, facing);
        this.model.setupAnim(openness);
        this.model.root().getExtentsForGui(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    static class ShulkerBoxBlockModel extends Model<Float> {
        private final ModelPart lid;

        public ShulkerBoxBlockModel(ModelPart root) {
            super(root, RenderTypes::entityCutout);
            this.lid = root.getChild("lid");
        }

        @Override
        public void setupAnim(Float progress) {
            super.setupAnim(progress);
            this.lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
            this.lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
        }
    }

}
