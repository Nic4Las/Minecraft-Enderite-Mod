package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class EnderiteShearsSheepMixin extends LivingEntity {

    protected EnderiteShearsSheepMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isShearable();

    @Shadow
    public abstract void sheared(ServerWorld world, SoundCategory shearedSoundCategory, ItemStack shears);

    @Inject(at = @At(value = "HEAD"), method = "interactMob", cancellable = true)
    private void onShear(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(EnderiteTools.ENDERITE_SHEAR.get())) {
            if (this.getWorld() instanceof ServerWorld serverWorld && this.isShearable()) {
                this.sheared(serverWorld, SoundCategory.PLAYERS, itemStack);
                itemStack.damage(1, player, SheepEntity.getSlotForHand(hand));
                info.setReturnValue(ActionResult.SUCCESS);
            } else {
                info.setReturnValue(ActionResult.CONSUME);
            }
        }
    }
}