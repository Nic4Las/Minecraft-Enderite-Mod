package net.enderitemc.enderitemod.shulker;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class EnderiteShulkerSlot extends ShulkerBoxSlot {

    public EnderiteShulkerSlot(Container inventory, int i, int j, int k) {
        super(inventory, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !(Block.byItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock) && super.mayPlace(stack);
    }

    @Override
    public void setByPlayer(ItemStack stack) {
        if (Block.byItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock) {
            this.container.setChanged();
        }
        super.setByPlayer(stack);
    }

}