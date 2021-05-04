package net.enderitemc.enderitemod.renderer;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;

import net.enderitemc.enderitemod.model.EnderiteShieldModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class EnderiteShieldRenderer extends ItemStackTileEntityRenderer {

        private final EnderiteShieldModel shieldModel = new EnderiteShieldModel();

        public static final ResourceLocation LOCATION_ENDERITE_SHIELD_BASE_NO_PATTERN = new ResourceLocation(
                        "enderitemod:entity/enderite_shield_base");

        private static final Logger LOGGER = LogManager.getLogger();

        @Override
        public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType,
                        MatrixStack matrices, IRenderTypeBuffer bufferIn, int light, int overlay) {
                matrices.pushPose();
                boolean bl = stack.getTagElement("BlockEntityTag") != null;
                // LOGGER.info("Shield renderer");

                if (!bl) {

                        matrices.mulPose(Vector3f.XP.rotationDegrees(180));
                        IVertexBuilder vertexConsumer2 = ItemRenderer.getFoilBufferDirect(bufferIn,
                                        shieldModel.renderType(new ResourceLocation(
                                                        "enderitemod:textures/entity/enderite_shield_base_nopattern.png")),
                                        false, stack.hasFoil());
                        shieldModel.renderToBuffer(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                } else {
                        matrices.mulPose(Vector3f.XP.rotationDegrees(180));
                        RenderMaterial spriteIdentifier = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS,
                                        new ResourceLocation("enderitemod:entity/enderite_shield_base"));

                        IVertexBuilder vertexConsumer = spriteIdentifier.sprite()
                                        .wrap(ItemRenderer.getFoilBufferDirect(bufferIn, shieldModel.renderType(
                                                        ModelBakery.NO_PATTERN_SHIELD.atlasLocation()),
                                                        true, stack.hasFoil()));
                        shieldModel.handle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F,
                                        1.0F);
                        List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.createPatterns(
                                        ShieldItem.getColor(stack), BannerTileEntity.getItemPatterns(stack));
                        BannerTileEntityRenderer.renderPatterns(matrices, bufferIn, light, overlay,
                                        shieldModel.plate(), spriteIdentifier, false, list, stack.hasFoil());

                }

                // Mandatory call after GL calls
                matrices.popPose();
        }
}