package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class EnderiteDropMixin extends Entity {

	@Shadow
	public abstract ItemStack getItem();

	protected EnderiteDropMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void dropItem(CallbackInfo info) {
		// If enderite item entity has gravity, turn it off
		if (!hasNoGravity() && !world.isRemote() && !getItem().isEmpty()
				&& getItem().getItem().isIn(EnderiteTag.ENDERITE_ITEM)) {
			setNoGravity(true);
		}
		// Slow down enderite item y velocity (to stop vertical spread)
		if (getItem().getItem().isIn(EnderiteTag.ENDERITE_ITEM)) {
			this.setMotion(this.getMotion().mul(1.0D, 0.96D, 1.0D));
		}
	}
}
