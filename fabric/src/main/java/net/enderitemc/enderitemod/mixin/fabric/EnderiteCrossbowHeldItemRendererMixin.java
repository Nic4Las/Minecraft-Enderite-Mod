package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class EnderiteCrossbowHeldItemRendererMixin {
    // Let vanilla select the hands and animate our bows using their configured charge times.
    @WrapOperation(method = {"evaluateWhichHandsToRender", "isChargedCrossbow"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private static boolean enderitemod$isBowOrCrossbow(ItemStack stack, Object item, Operation<Boolean> original) {
        return (item == Items.BOW && stack.is(EnderiteTools.ENDERITE_BOW.get()))
            || (item == Items.CROSSBOW && stack.is(EnderiteTools.ENDERITE_CROSSBOW.get()))
            || original.call(stack, item);
    }

    @WrapOperation(method = "extractRenderState",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;getChargeDuration(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int enderitemod$chargeDuration(ItemStack stack, LivingEntity user, Operation<Integer> original) {
        return stack.is(EnderiteTools.ENDERITE_CROSSBOW.get())
            ? EnderiteCrossbow.getChargeDuration(stack, user) : original.call(stack, user);
    }

    @WrapOperation(
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"),
        method = "extractRenderState")
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
