package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class EnderiteCrossbowPlayerRendererMixin {

    @Inject(at = @At("HEAD"),
        method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
        cancellable = true)
    private static void enderitemod$getArmPose(Avatar player, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<ArmPose> cir) {
        if (!player.swinging
            && stack.is(EnderiteTools.ENDERITE_CROSSBOW.get())
            && EnderiteCrossbow.isCharged(stack)) {
            cir.setReturnValue(ArmPose.CROSSBOW_HOLD);
        }
    }

}
