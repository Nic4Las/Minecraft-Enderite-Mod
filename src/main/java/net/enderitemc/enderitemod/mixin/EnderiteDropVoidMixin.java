package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.enchantment.EnchantmentHelper;
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
public abstract class EnderiteDropVoidMixin extends Entity {

	@Shadow
	public abstract ItemStack getItem();

	protected EnderiteDropVoidMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("TAIL"), method = "tick")
	private void damageItem(CallbackInfo info) {
		// Items in the void get telported up and will live on (because they float)
		if (this.getPosY() < 0.0D) {
			int i = EnchantmentHelper.getEnchantmentLevel(Registration.VOID_FLOATING.get(), getItem());
			boolean survives = false;
			float r = (float) Math.random();
			if (r < i / 3.0) {
				survives = true;
			}
			if (getItem().getItem().isIn(EnderiteTag.ENDERITE_ITEM) || survives) {
				setNoGravity(true);
				revive();
				setGlowing(true);
				setPosition(getPosX(), 5, getPosZ());
				setVelocity(0, 0, 0);
			}
		}
	}
}
