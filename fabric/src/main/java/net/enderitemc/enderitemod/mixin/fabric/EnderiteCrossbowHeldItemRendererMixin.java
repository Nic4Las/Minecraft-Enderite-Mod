package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public abstract class EnderiteCrossbowHeldItemRendererMixin {

    @Shadow
    public abstract void renderItem(
        LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light
    );
    @Shadow
    protected abstract void applyItemArmAttackTransform(PoseStack matrices, HumanoidArm arm, float swingProgress);

    @Shadow
    protected abstract void applyItemArmTransform(PoseStack matrices, HumanoidArm arm, float equipProgress);

    @Inject(
        method = "submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"),
        cancellable = true
    )
    public void enderitemod$renderEnderiteCrossbow(
        AbstractClientPlayer player,
        float tickProgress,
        float pitch,
        InteractionHand hand,
        float swingProgress,
        ItemStack item,
        float equipProgress,
        PoseStack matrices,
        SubmitNodeCollector orderedRenderCommandQueue,
        int light,
        CallbackInfo info
    ) {
        if (item.is(EnderiteTools.ENDERITE_CROSSBOW.get()) && !player.isScoping()) {
            boolean bl = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.pushPose();

            boolean bl2 = EnderiteCrossbow.isCharged(item);
            boolean bl3 = arm == HumanoidArm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                this.applyItemArmTransform(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrices.mulPose(Axis.XP.rotationDegrees(-11.935F));
                matrices.mulPose(Axis.YP.rotationDegrees((float) i * 65.3F));
                matrices.mulPose(Axis.ZP.rotationDegrees((float) i * -9.785F));
                float f = (float) item.getUseDuration(player) - ((float) player.getUseItemRemainingTicks() - tickProgress + 1.0F);
                float g = f / (float) EnderiteCrossbow.getChargeDuration(item, player);
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = Mth.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                matrices.mulPose(Axis.YN.rotationDegrees((float) i * 45.0F));
            } else {
                float fx = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
                float gx = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * (float) (Math.PI * 2));
                float h = -0.2F * Mth.sin(swingProgress * (float) Math.PI);
                matrices.translate((float) i * fx, gx, h);
                this.applyItemArmTransform(matrices, arm, equipProgress);
                this.applyItemArmAttackTransform(matrices, arm, swingProgress);
                if (bl2 && swingProgress < 0.001F && bl) {
                    matrices.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    matrices.mulPose(Axis.YP.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(
                player,
                item,
                bl3 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                matrices,
                orderedRenderCommandQueue,
                light
            );

            matrices.popPose();
            info.cancel();
        }

    }

    @Inject(
        at = @At("HEAD"),
        method = "isChargedCrossbow(Lnet/minecraft/world/item/ItemStack;)Z",
        cancellable = true)
    private static void enderitemod$chargedEnderiteCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (stack.is(EnderiteTools.ENDERITE_CROSSBOW.get()) && EnderiteCrossbow.isCharged(stack)) {
            info.setReturnValue(true);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"),
        method = "evaluateWhichHandsToRender(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;")
    private static boolean enderitemod$isBowOrCrossbow(ItemStack instance, Object item, Operation<Boolean> original) {
        if (item == Items.BOW && instance.is(EnderiteTools.ENDERITE_BOW.get())) {
            return true;
        } else if (item == Items.CROSSBOW && instance.is(EnderiteTools.ENDERITE_CROSSBOW.get())) {
            return true;
        } else {
            return original.call(instance, item);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"),
        method = "selectionUsingItemWhileHoldingBowLike(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;")
    private static boolean enderitemod$isBowOrCrossbowUsing(ItemStack instance, Object item, Operation<Boolean> original) {
        return enderitemod$isBowOrCrossbow(instance, item, original);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"),
        method = "submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V")
    private int enderitemod$changeBowTime(ItemStack instance, LivingEntity user, Operation<Integer> original) {
        if (instance.is(EnderiteTools.ENDERITE_BOW.get())) {
            int maxTime = original.call(instance, user);
            int useTime = (maxTime - user.getUseItemRemainingTicks());
            int dx = (int) (EnderiteMod.CONFIG.tools.enderiteBowChargeTime - 20);
            if (dx <= 0) {
                // Faster than vanilla bow, fast pulling
                int dt = (int) (dx / EnderiteMod.CONFIG.tools.enderiteBowChargeTime * useTime);
                dt = Math.max(dt, dx);
                return maxTime - dt;
            } else {
                // Slower than vanilla bow, cyclic pulling
                int p = 14;
                int thr = 13;
                if (useTime < thr) {
                    return maxTime;
                } else if (useTime < EnderiteMod.CONFIG.tools.enderiteBowChargeTime) {
                    int dt = -enderitemod$triangleWave(useTime - thr, p);
                    return maxTime - dt;
                } else {
                    return maxTime - enderitemod$triangleWave((int) (EnderiteMod.CONFIG.tools.enderiteBowChargeTime - thr), p);
                }
            }
        } else {
            return original.call(instance, user);
        }
    }

    @Unique
    private static int enderitemod$triangleWave(int x, float p) {
        float h_p = p / 2;
        if (x % p <= h_p) return (int) (Math.floor((-x + 3 * p / 4) / p) * p);
        return (int) ((-x % p) - x + p);
    }
}
