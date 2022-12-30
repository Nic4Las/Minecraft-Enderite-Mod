package net.enderitemc.enderitemod.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public abstract class EnderiteShearsSheepMixin extends LivingEntity {

    protected EnderiteShearsSheepMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    abstract boolean isShearable();

    @Shadow
    abstract void sheared(SoundCategory cat);

    @Inject(at = @At(value = "HEAD"), method = "interactMob", cancellable = true)
    private void onShear(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() == EnderiteMod.ENDERITE_SHEAR.get()) {
            if (!this.world.isClient && this.isShearable()) {
                this.sheared(SoundCategory.PLAYERS);
                itemStack.damage(1, (LivingEntity) player, (Consumer<LivingEntity>) ((playerEntity) -> {
                    playerEntity.sendToolBreakStatus(hand);
                }));
                info.setReturnValue(ActionResult.SUCCESS);
            } else {
                info.setReturnValue(ActionResult.CONSUME);
            }
        }
    }
}