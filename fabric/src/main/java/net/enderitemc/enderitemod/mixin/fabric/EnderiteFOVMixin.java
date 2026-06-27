package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.authlib.GameProfile;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class EnderiteFOVMixin extends Player {

    public EnderiteFOVMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z"), method = "getFieldOfViewModifier(ZF)F")
    public void enderitemod$getFovMultiplier(boolean firstPerson, float fovEffectScale, CallbackInfoReturnable<Float> cir, @Local(ordinal = 1) LocalFloatRef f) {
        if (this.isUsingItem()) {
            if (this.getUseItem().is(EnderiteTools.ENDERITE_BOW.get())) {
                float h = Math.min(EnderiteBow.getPowerForTime(this.getTicksUsingItem()), 1.0F);
                f.set(f.get() * (1.0F - Mth.square(h) * 0.15F));
            }
        }
    }
}
