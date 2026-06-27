package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Sheep.class)
public abstract class EnderiteShearsSheepMixin {
    @WrapOperation(
        method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean enderitemod$acceptEnderiteShears(
        ItemStack stack,
        Object checkedItem,
        Operation<Boolean> original
    ) {
        return original.call(stack, checkedItem)
            || checkedItem == Items.SHEARS && stack.is(EnderiteTools.ENDERITE_SHEAR.get());
    }
}
