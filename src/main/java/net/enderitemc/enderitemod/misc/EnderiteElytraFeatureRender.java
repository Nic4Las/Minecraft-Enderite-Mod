package net.enderitemc.enderitemod.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents.AllowCapeRender;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class EnderiteElytraFeatureRender<T extends LivingEntity, M extends EntityModel<T>>
extends FeatureRenderer<T, M> {
    private static final Identifier SKIN = new Identifier("textures/entity/enderite_elytra.png");
    private final ElytraEntityModel<T> elytra;

    public EnderiteElytraFeatureRender(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.elytra = new ElytraEntityModel(loader.getModelPart(EntityModelLayers.ELYTRA));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        Object abstractClientPlayerEntity;
        ItemStack itemStack = ((LivingEntity)livingEntity).getEquippedStack(EquipmentSlot.CHEST);
        if (!itemStack.isIn(EnderiteTag.ENDERITE_ELYTRA)) {
            return;
        }
        Identifier identifier = livingEntity instanceof AbstractClientPlayerEntity ? (((AbstractClientPlayerEntity)(abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity)).canRenderElytraTexture() && ((AbstractClientPlayerEntity)abstractClientPlayerEntity).getElytraTexture() != null ? ((AbstractClientPlayerEntity)abstractClientPlayerEntity).getElytraTexture() : (((AbstractClientPlayerEntity)abstractClientPlayerEntity).canRenderCapeTexture() && ((AbstractClientPlayerEntity)abstractClientPlayerEntity).getCapeTexture() != null && ((PlayerEntity)abstractClientPlayerEntity).isPartVisible(PlayerModelPart.CAPE) ? ((AbstractClientPlayerEntity)abstractClientPlayerEntity).getCapeTexture() : SKIN)) : SKIN;
        matrixStack.push();
        matrixStack.translate(0.0, 0.0, 0.125);
        ((EntityModel)this.getContextModel()).copyStateTo(this.elytra);
        this.elytra.setAngles(livingEntity, f, g, j, k, l);
        abstractClientPlayerEntity = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(identifier), false, itemStack.hasGlint());
        this.elytra.render(matrixStack, (VertexConsumer)abstractClientPlayerEntity, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
    }

}
