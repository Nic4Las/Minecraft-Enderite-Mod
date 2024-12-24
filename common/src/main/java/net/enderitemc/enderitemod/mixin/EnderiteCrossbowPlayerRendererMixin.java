package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class EnderiteCrossbowPlayerRendererMixin {

    @Inject(at = @At("HEAD"), method = "getArmPose(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", cancellable = true)
    private static void getArmPose(PlayerEntity player, ItemStack stack, Hand hand, CallbackInfoReturnable<ArmPose> cir) {
        if (!player.handSwinging
            && stack.isOf(EnderiteTools.ENDERITE_CROSSBOW.get())
            && EnderiteCrossbow.isCharged(stack)) {
            cir.setReturnValue(ArmPose.CROSSBOW_HOLD);
        }
    }

}
