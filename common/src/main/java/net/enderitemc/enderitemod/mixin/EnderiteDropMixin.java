package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class EnderiteDropMixin extends Entity {

    @Shadow
    public abstract ItemStack getItem();

    protected EnderiteDropMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void enderitemod$dropItem(CallbackInfo info) {
        // If enderite item entity has gravity, turn it off
        if (!isNoGravity() && !level().isClientSide() && !getItem().isEmpty()
            && (getItem().is(EnderiteTag.ENDERITE_ITEM))) {
            setNoGravity(true);
        }
        // Slow down enderite item y velocity (to stop vertical spread)
        if ((getItem().is(EnderiteTag.ENDERITE_ITEM))) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.96D, 1.0D));
        }
    }
}
