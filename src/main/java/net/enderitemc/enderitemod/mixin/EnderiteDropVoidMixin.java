package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
public abstract class EnderiteDropVoidMixin extends Entity {

	@Shadow
	public abstract ItemStack getItem();

	protected EnderiteDropVoidMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void damageItem(CallbackInfo info) {
		// Items in the void get telported up and will live on (because they float)
		if (this.getY() < this.getLevel().getMinBuildHeight()) {
			int i = EnchantmentHelper.getItemEnchantmentLevel(Registration.VOID_FLOATING.get(), getItem());
			// Mute NullPointerException if tag not present
			try {
				i += getItem().getTag().getCompound("tic_volatile_data").getInt("tconstruct:void_floating");
			} catch (NullPointerException e) {
			}
			i = i > 3 ? 3 : i;
			boolean survives = false;
			float r = (float) Math.random();
			if (r < i / 3.0) {
				survives = true;
			}
			if (getItem().is(EnderiteTag.ENDERITE_ITEM) || survives) {
				setNoGravity(true);
				revive();
				setGlowingTag(true);
				setPos(getX(), this.getLevel().getMinBuildHeight()+5, getZ());
				setDeltaMovement(0, 0, 0);
			}
		}
	}
}
