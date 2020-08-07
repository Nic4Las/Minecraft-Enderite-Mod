package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;

@OnlyIn(Dist.CLIENT)
@Mixin(ElytraLayer.class)
public abstract class ElytraFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends LayerRenderer<T, M> {

    private static final ResourceLocation ELYTRA_SKIN = new ResourceLocation("textures/entity/enderite_elytra.png");

    @Shadow
    private final ElytraModel<T> modelElytra = new ElytraModel<>();

    public ElytraFeatureRendererMixin(IEntityRenderer<T, M> rendererIn) {
        super(rendererIn);
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void renderer(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
            T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch, CallbackInfo info) {
        // If player is wearing enderite elytra, render it
        ItemStack itemStack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if (itemStack.getItem() == Registration.ENDERITE_ELYTRA.get()) {
            ResourceLocation resourcelocation;
            if (entitylivingbaseIn instanceof AbstractClientPlayerEntity) {
                AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) entitylivingbaseIn;
                if (abstractclientplayerentity.isPlayerInfoSet()
                        && abstractclientplayerentity.getLocationElytra() != null) {
                    resourcelocation = abstractclientplayerentity.getLocationElytra();
                } else if (abstractclientplayerentity.hasPlayerInfo()
                        && abstractclientplayerentity.getLocationCape() != null
                        && abstractclientplayerentity.isWearing(PlayerModelPart.CAPE)) {
                    resourcelocation = abstractclientplayerentity.getLocationCape();
                } else {
                    resourcelocation = ELYTRA_SKIN;
                }
            } else {
                resourcelocation = ELYTRA_SKIN;
            }

            matrixStackIn.push();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            this.getEntityModel().copyModelAttributesTo(this.modelElytra);
            this.modelElytra.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                    headPitch);
            IVertexBuilder ivertexbuilder = ItemRenderer.func_239391_c_(bufferIn,
                    this.modelElytra.getRenderType(resourcelocation), false, itemStack.hasEffect());
            this.modelElytra.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F,
                    1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }
}