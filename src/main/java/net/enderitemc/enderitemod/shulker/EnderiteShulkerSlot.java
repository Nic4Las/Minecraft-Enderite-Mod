package net.enderitemc.enderitemod.shulker;

import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.ShulkerBoxSlot;

public class EnderiteShulkerSlot extends ShulkerBoxSlot {

    public EnderiteShulkerSlot(Inventory inventory, int i, int j, int k) {
        super(inventory, i, j, k);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock) && super.canInsert(stack);
    }

    @Override
    public void setStack(ItemStack stack) {
        if (Block.getBlockFromItem(stack.getItem()) instanceof EnderiteShulkerBoxBlock) {
            this.inventory.markDirty();
        }
        super.setStack(stack);
    }

}