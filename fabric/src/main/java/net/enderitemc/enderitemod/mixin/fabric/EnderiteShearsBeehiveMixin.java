package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(BeehiveBlock.class)
public abstract class EnderiteShearsBeehiveMixin {
    @WrapOperation(
        method = "useItemOn",
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
