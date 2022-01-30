package net.enderitemc.enderitemod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.enderitemc.enderitemod.tileEntity.EnderiteShulkerBoxTileEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderiteShulkerBoxTileEntityRenderer implements BlockEntityRenderer<EnderiteShulkerBoxTileEntity> {
    private final ShulkerModel<?> model;

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "textures/entity/shulker/enderite_shulker.png");
    private static final RenderType ENDERITE_SHULKER;

    public EnderiteShulkerBoxTileEntityRenderer(BlockEntityRendererProvider.Context p_i226013_2_) {
        this.model = new ShulkerModel(p_i226013_2_.bakeLayer(ModelLayers.SHULKER));
    }

    public void render(EnderiteShulkerBoxTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn,
            MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = Direction.UP;
        if (tileEntityIn.hasLevel()) {
            BlockState blockstate = tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos());
            if (blockstate.getBlock() instanceof ShulkerBoxBlock) {
                direction = blockstate.getValue(ShulkerBoxBlock.FACING);
            }
        }

        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        float f = 0.9995F;
        matrixStackIn.scale(0.9995F, 0.9995F, 0.9995F);
        matrixStackIn.mulPose(direction.getRotation());
        matrixStackIn.scale(1.0F, -1.0F, -1.0F);
        matrixStackIn.translate(0.0D, -1.0D, 0.0D);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(ENDERITE_SHULKER);
        ModelPart modelpart = this.model.getLid();
        modelpart.setPos(0.0F, 24.0F - tileEntityIn.getProgress(partialTicks) * 0.5F * 16.0F, 0.0F);
        modelpart.yRot = 270.0F * tileEntityIn.getProgress(partialTicks) * ((float)Math.PI / 180F);
        //matrixStackIn.translate(0.0D, (double) (-tileEntityIn.getProgress(partialTicks) * 0.5F), 0.0D);
        //matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F * tileEntityIn.getProgress(partialTicks)));
        //this.model.getLid().render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }

    static {
        ENDERITE_SHULKER = RenderType.entityCutoutNoCull(TEXTURE);
    }

}