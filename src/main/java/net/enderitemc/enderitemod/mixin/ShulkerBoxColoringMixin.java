package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShulkerBoxColoringRecipe;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxColoringRecipe.class)
public abstract class ShulkerBoxColoringMixin extends SpecialRecipe {

	protected ShulkerBoxColoringMixin(ResourceLocation identifier) {
		super(identifier);
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "matches")
	private void dropItem(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> info) {
		// If one item is EnderiteShulkerBox abort color recipe
		for (int k = 0; k < craftingInventory.getContainerSize(); ++k) {
			ItemStack itemStack = craftingInventory.getItem(k);
			if (!itemStack.isEmpty()) {
				if (Block.byItem(itemStack.getItem()) instanceof EnderiteShulkerBox) {
					info.setReturnValue(false);
				}
			}
		}
	}
}
