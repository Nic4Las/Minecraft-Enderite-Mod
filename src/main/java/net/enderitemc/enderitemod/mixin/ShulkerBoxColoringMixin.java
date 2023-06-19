package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxColoringRecipe.class)
public abstract class ShulkerBoxColoringMixin extends SpecialCraftingRecipe {

	protected ShulkerBoxColoringMixin(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "matches")
	private void dropItem(RecipeInputInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> info) {
		// If one item is EnderiteShulkerBox abort color recipe
		for (int k = 0; k < craftingInventory.size(); ++k) {
			ItemStack itemStack = craftingInventory.getStack(k);
			if (!itemStack.isEmpty()) {
				if (Block.getBlockFromItem(itemStack.getItem()) instanceof EnderiteShulkerBoxBlock) {
					info.setReturnValue(false);
				}
			}
		}
	}
}
