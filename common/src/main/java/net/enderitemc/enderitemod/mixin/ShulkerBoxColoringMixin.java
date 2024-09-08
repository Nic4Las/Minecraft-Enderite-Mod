package net.enderitemc.enderitemod.mixin;

import net.minecraft.recipe.input.CraftingRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.world.World;

@Mixin(ShulkerBoxColoringRecipe.class)
public abstract class ShulkerBoxColoringMixin extends SpecialCraftingRecipe {

	protected ShulkerBoxColoringMixin(CraftingRecipeCategory category) {
		super(category);
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "matches*")
	private void dropItem(CraftingRecipeInput craftingInventory, World world, CallbackInfoReturnable<Boolean> info) {
		// If one item is EnderiteShulkerBox abort color recipe
		for (int k = 0; k < craftingInventory.getSize(); ++k) {
			ItemStack itemStack = craftingInventory.getStackInSlot(k);
			if (!itemStack.isEmpty()) {
				if (Block.getBlockFromItem(itemStack.getItem()) instanceof EnderiteShulkerBoxBlock) {
					info.setReturnValue(false);
				}
			}
		}
	}
}