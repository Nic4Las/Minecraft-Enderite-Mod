package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private static final Identifier ELYTRA_SKIN = new Identifier("textures/entity/enderite_elytra.png");

    @Shadow
    private final ElytraEntityModel<T> elytra;

    public ElytraFeatureRendererMixin(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.elytra = new ElytraEntityModel<>(loader.getModelPart(EntityModelLayers.SHIELD));
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void renderer(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity,
            float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        // If player is wearing enderite elytra, render it
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (itemStack.isIn(EnderiteTag.ENDERITE_ELYTRA)) {
            AbstractClientPlayerEntity abstractClientPlayerEntity;
            SkinTextures skinTextures;
            Identifier identifier4 = livingEntity instanceof AbstractClientPlayerEntity ? ((skinTextures = (abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity).getSkinTextures()).elytraTexture() != null ? skinTextures.elytraTexture() : (skinTextures.capeTexture() != null && abstractClientPlayerEntity.isPartVisible(PlayerModelPart.CAPE) ? skinTextures.capeTexture() : ELYTRA_SKIN)) : ELYTRA_SKIN;

            matrixStack.push();
            matrixStack.translate(0.0D, 0.0D, 0.125D);
            this.getContextModel().copyStateTo(this.elytra);
            this.elytra.setAngles(livingEntity, f, g, j, k, l);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider,
                    this.elytra.getLayer(identifier4), false, itemStack.hasGlint());
            this.elytra.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
    }
}