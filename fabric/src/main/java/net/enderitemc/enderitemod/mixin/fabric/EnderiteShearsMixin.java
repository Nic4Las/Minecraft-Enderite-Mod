package net.enderitemc.enderitemod.mixin.fabric;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class EnderiteShearsMixin {

    @Shadow
    public abstract boolean isOf(Item item);

    @WrapMethod(method = "isOf")
    private boolean isOfWrap(Item item, Operation<Boolean> original) {
        if (item == Items.SHEARS && this.isOf(EnderiteTools.ENDERITE_SHEAR.get())) {
            return true;
        }
        return original.call(item);
    }

}
