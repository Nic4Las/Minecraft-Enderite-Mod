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
        public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType,
                        MatrixStack matrices, IRenderTypeBuffer bufferIn, int light, int overlay) {
                matrices.push();
                boolean bl = stack.getChildTag("BlockEntityTag") != null;
                // LOGGER.info("Shield renderer");

                if (!bl) {

                        matrices.rotate(Vector3f.XP.rotationDegrees(180));
                        IVertexBuilder vertexConsumer2 = ItemRenderer.func_239391_c_(bufferIn,
                                        shieldModel.getRenderType(new ResourceLocation(
                                                        "enderitemod:textures/entity/enderite_shield_base_nopattern.png")),
                                        false, stack.hasEffect());
                        shieldModel.render(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                } else {
                        matrices.rotate(Vector3f.XP.rotationDegrees(180));
                        RenderMaterial spriteIdentifier = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
                                        new ResourceLocation("enderitemod:entity/enderite_shield_base"));

                        IVertexBuilder vertexConsumer = spriteIdentifier.getSprite()
                                        .wrapBuffer(ItemRenderer.func_239391_c_(bufferIn, shieldModel.getRenderType(
                                                        ModelBakery.LOCATION_SHIELD_NO_PATTERN.getAtlasLocation()),
                                                        true, stack.hasEffect()));
                        shieldModel.func_228294_b_().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F,
                                        1.0F);
                        List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.func_230138_a_(
                                        ShieldItem.getColor(stack), BannerTileEntity.func_230139_a_(stack));
                        BannerTileEntityRenderer.func_241717_a_(matrices, bufferIn, light, overlay,
                                        shieldModel.func_228293_a_(), spriteIdentifier, false, list, stack.hasEffect());

                }

                // Mandatory call after GL calls
                matrices.pop();
        }
}