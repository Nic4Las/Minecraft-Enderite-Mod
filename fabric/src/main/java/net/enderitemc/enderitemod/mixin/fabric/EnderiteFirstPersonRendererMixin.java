package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class EnderiteFirstPersonRendererMixin {
    @WrapOperation(method = "submitArmWithItem",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
    private boolean enderitemod$useVanillaCrossbowAnimation(ItemStack stack, Object item, Operation<Boolean> original) {
        return (item == Items.CROSSBOW && stack.is(EnderiteTools.ENDERITE_CROSSBOW.get()))
            || original.call(stack, item);
    }
}
