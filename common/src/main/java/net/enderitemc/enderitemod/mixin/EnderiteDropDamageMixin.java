package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class EnderiteDropDamageMixin extends Entity {

	@Shadow
	public abstract ItemStack getStack();

	protected EnderiteDropDamageMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void damageItem(CallbackInfo info) {
		// Items in the void get telported up and will live on (because they float)
		if (this.getY() < this.getWorld().getBottomY()) {
			int i = EnchantmentHelper.getLevel(EnderiteMod.VOID_FLOATING_ENCHANTMENT.get(), getStack());
			if(i==0) {
				NbtCompound nbt = getStack().getNbt();
				if(nbt!=null) {
					NbtCompound trim_nbt = nbt.getCompound("Trim");
					if(trim_nbt!=null) {
						if(trim_nbt.getString("material").equals("enderitemod:enderite")) {
							i = 1;
						}
					}
				}
			}
			boolean survives = false;
			float r = random.nextFloat();
			if (r < i / 3.0) {
				survives = true;
			}
			if ((getStack().isIn(EnderiteTag.ENDERITE_ITEM)) || survives) {
				this.unsetRemoved(); //this.removed = false;
				// ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), 10,
				// this.getZ(), getStack());
				this.requestTeleport(this.getX(), this.getWorld().getBottomY()+10, this.getZ());
				this.setVelocity(0, 0, 0);
				this.setNoGravity(true);
				this.setGlowing(true);
			}
		}
	}
}