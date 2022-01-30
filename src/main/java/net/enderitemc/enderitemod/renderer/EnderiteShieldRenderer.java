package net.enderitemc.enderitemod.renderer;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;

import net.enderitemc.enderitemod.model.EnderiteShieldModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class EnderiteShieldRenderer extends BlockEntityWithoutLevelRenderer {

        public static final EnderiteShieldRenderer INSTANCE = new EnderiteShieldRenderer();

        public EnderiteShieldRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
                super(p_172550_, p_172551_);
                this.entityModelSet = p_172551_;
                //shieldModel = new EnderiteShieldModel(p_172551_.bakeLayer(ModelLayers.SHIELD));
        }

        public EnderiteShieldRenderer() {
                super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                this.entityModelSet = Minecraft.getInstance().getEntityModels();
                //shieldModel = new EnderiteShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SHIELD));
        }

        private final EntityModelSet entityModelSet;
        private EnderiteShieldModel shieldModel;
        public static final ResourceLocation LOCATION_ENDERITE_SHIELD_BASE_NO_PATTERN = new ResourceLocation(
                        "enderitemod:entity/enderite_shield_base");

        private static final Logger LOGGER = LogManager.getLogger();

        @Override
        public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType,
                        PoseStack matrices, MultiBufferSource bufferIn, int light, int overlay) {
                if(this.shieldModel==null) {
                        this.shieldModel = new EnderiteShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
                        return;
                }
                                matrices.pushPose();
                boolean bl = stack.getTagElement("BlockEntityTag") != null;
                // LOGGER.info("Shield renderer");

                if (!bl) {

                        matrices.mulPose(Vector3f.XP.rotationDegrees(180));
                        VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBufferDirect(bufferIn,
                                        shieldModel.renderType(new ResourceLocation(
                                                        "enderitemod:textures/entity/enderite_shield_base_nopattern.png")),
                                        false, stack.hasFoil());
                        shieldModel.renderToBuffer(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                } else {
                        matrices.mulPose(Vector3f.XP.rotationDegrees(180));
                        Material spriteIdentifier = new Material(TextureAtlas.LOCATION_BLOCKS,
                                        new ResourceLocation("enderitemod:entity/enderite_shield_base"));

                        VertexConsumer vertexConsumer = spriteIdentifier.sprite()
                                        .wrap(ItemRenderer.getFoilBufferDirect(bufferIn, shieldModel.renderType(
                                                        ModelBakery.NO_PATTERN_SHIELD.atlasLocation()),
                                                        true, stack.hasFoil()));
                        shieldModel.handle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F,
                                        1.0F);
                        List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(
                                        ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
                        BannerRenderer.renderPatterns(matrices, bufferIn, light, overlay,
                                        shieldModel.plate(), spriteIdentifier, false, list, stack.hasFoil());

                }

                // Mandatory call after GL calls
                matrices.popPose();
        }

        @Override
        public void onResourceManagerReload(ResourceManager p_172555_) {
                this.shieldModel = new EnderiteShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        }
}