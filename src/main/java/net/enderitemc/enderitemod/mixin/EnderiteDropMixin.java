package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;

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
	public abstract ItemStack getStack();

	protected EnderiteDropMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void dropItem(CallbackInfo info) {
		// If enderite item entity has gravity, turn it off
		if (!hasNoGravity() && !world.isClient && !getStack().isEmpty()
				&& getStack().getItem().isIn(EnderiteTag.ENDERITE_ITEM)) {
			setNoGravity(true);
		}
		// Slow down enderite item y velocity (to stop vertical spread)
		if (getStack().getItem().isIn(EnderiteTag.ENDERITE_ITEM)) {
			this.setVelocity(this.getVelocity().multiply(1.0D, 0.96D, 1.0D));
		}
	}
}
