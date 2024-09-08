package net.enderitemc.enderitemod.misc;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class EnderiteElytraFeatureRender<T extends LivingEntity, M extends EntityModel<T>>
                extends FeatureRenderer<T, M> {
        private static final Identifier SKIN = Identifier.of("textures/entity/enderite_elytra.png");
        private final ElytraEntityModel<T> elytra;

        public EnderiteElytraFeatureRender(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
                super(context);
                this.elytra = new ElytraEntityModel<>(loader.getModelPart(EntityModelLayers.ELYTRA));
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
                        T livingEntity,
                        float f, float g, float h, float j, float k, float l) {
                ItemStack itemStack = ((LivingEntity) livingEntity).getEquippedStack(EquipmentSlot.CHEST);
                if (!itemStack.isIn(EnderiteTag.ENDERITE_ELYTRA)) {
                        return;
                }
                
                AbstractClientPlayerEntity abstractClientPlayerEntity;
                SkinTextures skinTextures;
                Identifier identifier = livingEntity instanceof AbstractClientPlayerEntity ? ((skinTextures = (abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity).getSkinTextures()).elytraTexture() != null ? skinTextures.elytraTexture() : (skinTextures.capeTexture() != null && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE) ? skinTextures.capeTexture() : SKIN)) : SKIN;
                
                matrixStack.push();
                matrixStack.translate(0.0, 0.0, 0.125);
                this.getContextModel().copyStateTo(this.elytra);
                this.elytra.setAngles(livingEntity, f, g, j, k, l);
                VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(
                        vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), itemStack.hasGlint()
                );
                this.elytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
        }

}
