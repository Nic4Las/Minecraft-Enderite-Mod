package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.block.EnderiteShulkerBox;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShulkerBoxColoring;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxColoring.class)
public abstract class ShulkerBoxColoringMixin extends CustomRecipe {

	protected ShulkerBoxColoringMixin(ResourceLocation identifier) {
		super(identifier);
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "matches")
	private void dropItem(CraftingContainer craftingInventory, Level world, CallbackInfoReturnable<Boolean> info) {
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
