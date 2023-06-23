package net.enderitemc.enderitemod.forge.renderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class EnderiteShieldRenderer extends BuiltinModelItemRenderer {

        public static final EnderiteShieldRenderer INSTANCE = new EnderiteShieldRenderer();
        
        private final ShieldEntityModel shieldModel;// = Suppliers.memoize(()->new ShieldEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.SHIELD)));

        public static final SpriteIdentifier SHIELD_BASE = new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, new Identifier(EnderiteMod.MOD_ID,"entity/enderite_shield_base"));
        public static final SpriteIdentifier SHIELD_BASE_NO_PATTERN = new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, new Identifier(EnderiteMod.MOD_ID,"entity/enderite_shield_base_nopattern"));

        public EnderiteShieldRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher,
                        EntityModelLoader entityModelLoader) {
                super(blockEntityRenderDispatcher, entityModelLoader);
                shieldModel = new ShieldEntityModel(entityModelLoader.getModelPart(EntityModelLayers.SHIELD));
        }

        public EnderiteShieldRenderer() {
                this(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(),
                                MinecraftClient.getInstance().getEntityModelLoader());
                // this.entityModelSet = MinecraftClient.getInstance().getEntityModels();
                // shieldModel = new
                // EnderiteShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SHIELD));
        }

        @Override
        public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
                        VertexConsumerProvider vertexConsumers, int light,
                        int overlay) {
                boolean bl = BlockItem.getBlockEntityNbt(stack) != null;
                matrices.push();
                matrices.scale(1.0f, -1.0f, -1.0f);
                SpriteIdentifier spriteIdentifier = bl ? SHIELD_BASE : SHIELD_BASE_NO_PATTERN;
                VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.shieldModel.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
                this.shieldModel.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
                if (bl) {
                        List<Pair<RegistryEntry<BannerPattern>, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(ShieldItem.getColor(stack), BannerBlockEntity.getPatternListNbt(stack));
                        BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, this.shieldModel.getPlate(), spriteIdentifier, false, list, stack.hasGlint());
                } else {
                        this.shieldModel.getPlate().render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
                }
                matrices.pop();
        }

}