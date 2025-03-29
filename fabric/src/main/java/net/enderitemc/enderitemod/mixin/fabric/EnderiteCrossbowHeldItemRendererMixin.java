package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class EnderiteCrossbowHeldItemRendererMixin {

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light);

    @Shadow
    protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow
    protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Inject(
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"),
        cancellable = true
    )
    public void renderEnderiteCrossbow(
        AbstractClientPlayerEntity player,
        float tickDelta,
        float pitch,
        Hand hand,
        float swingProgress,
        ItemStack item,
        float equipProgress,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo info
    ) {
        if (item.isOf(EnderiteTools.ENDERITE_CROSSBOW.get()) && !player.isUsingSpyglass()) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();

            boolean bl2 = EnderiteCrossbow.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int i = bl3 ? 1 : -1;
            if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
                this.applyEquipOffset(matrices, arm, equipProgress);
                matrices.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-11.935F));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 65.3F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * -9.785F));
                float f = (float) item.getMaxUseTime(player) - ((float) player.getItemUseTimeLeft() - tickDelta + 1.0F);
                float g = f / (float) EnderiteCrossbow.getPullTime(item, player);
                if (g > 1.0F) {
                    g = 1.0F;
                }

                if (g > 0.1F) {
                    float h = MathHelper.sin((f - 0.1F) * 1.3F);
                    float j = g - 0.1F;
                    float k = h * j;
                    matrices.translate(k * 0.0F, k * 0.004F, k * 0.0F);
                }

                matrices.translate(g * 0.0F, g * 0.0F, g * 0.04F);
                matrices.scale(1.0F, 1.0F, 1.0F + g * 0.2F);
                matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((float) i * 45.0F));
            } else {
                float fx = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float gx = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) (Math.PI * 2));
                float h = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                matrices.translate((float) i * fx, gx, h);
                this.applyEquipOffset(matrices, arm, equipProgress);
                this.applySwingOffset(matrices, arm, swingProgress);
                if (bl2 && swingProgress < 0.001F && bl) {
                    matrices.translate((float) i * -0.641864F, 0.0F, 0.0F);
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(
                player,
                item,
                bl3 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                matrices,
                vertexConsumers,
                light
            );

            matrices.pop();
            info.cancel();
        }

    }

    @Inject(
        at = @At("HEAD"),
        method = "isChargedCrossbow(Lnet/minecraft/item/ItemStack;)Z",
        cancellable = true)
    private static void chargedEnderiteCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (stack.isOf(EnderiteTools.ENDERITE_CROSSBOW.get()) && EnderiteCrossbow.isCharged(stack)) {
            info.setReturnValue(true);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"),
        method = "getHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;")
    private static boolean isBowOrCrossbow(ItemStack instance, Item item, Operation<Boolean> original) {
        if (item == Items.BOW && instance.isOf(EnderiteTools.ENDERITE_BOW.get())) {
            return true;
        } else if (item == Items.CROSSBOW && instance.isOf(EnderiteTools.ENDERITE_CROSSBOW.get())) {
            return true;
        } else {
            return original.call(instance, item);
        }
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"),
        method = "getUsingItemHandRenderType(Lnet/minecraft/client/network/ClientPlayerEntity;)Lnet/minecraft/client/render/item/HeldItemRenderer$HandRenderType;")
    private static boolean isBowOrCrossbowUsing(ItemStack instance, Item item, Operation<Boolean> original) {
        return isBowOrCrossbow(instance, item, original);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime(Lnet/minecraft/entity/LivingEntity;)I"),
        method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private int changeBowTime(ItemStack instance, LivingEntity user, Operation<Integer> original) {
        if (instance.isOf(EnderiteTools.ENDERITE_BOW.get())) {
            int maxTime = original.call(instance, user);
            int useTime = (maxTime - user.getItemUseTimeLeft());
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
                    int dt = -triangleWave(useTime - thr, p);
                    return maxTime - dt;
                } else {
                    return maxTime - triangleWave((int) (EnderiteMod.CONFIG.tools.enderiteBowChargeTime - thr), p);
                }
            }
        } else {
            return original.call(instance, user);
        }
    }

    @Unique
    private static int triangleWave(int x, float p) {
        float h_p = p / 2;
        if (x % p <= h_p) return (int) (Math.floor((-x + 3 * p / 4) / p) * p);
        return (int) ((-x % p) - x + p);
    }
}
