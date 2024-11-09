package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.authlib.GameProfile;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class EnderiteFOVMixin extends PlayerEntity {

    public EnderiteFOVMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(at=@At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z"), method = "getFovMultiplier(ZF)F")
    public void getFovMultiplier(boolean firstPerson, float fovEffectScale, CallbackInfoReturnable<Float> cir, @Local(ordinal = 1) LocalFloatRef f) {
        if (this.isUsingItem()) {
            if (this.getActiveItem().isOf(EnderiteTools.ENDERITE_BOW.get())) {
                float h = Math.min(EnderiteBow.getPullProgress(this.getItemUseTime()), 1.0F);
                f.set(f.get() * (1.0F - MathHelper.square(h) * 0.15F));
            }
        }
    }
}
