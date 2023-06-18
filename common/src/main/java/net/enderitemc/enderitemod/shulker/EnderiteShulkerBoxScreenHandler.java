package net.enderitemc.enderitemod.shulker;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class EnderiteShulkerBoxScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public EnderiteShulkerBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(45));
    }

    public EnderiteShulkerBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.GENERIC_9X5, syncId);
        checkSize(inventory, 45);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        int o;
        int n;
        for (o = 0; o < 5; ++o) {
            for (n = 0; n < 9; ++n) {
                this.addSlot(new EnderiteShulkerSlot(inventory, n + o * 9, 8 + n * 18, 18 + o * 18));
            }
        }

        for (o = 0; o < 3; ++o) {
            for (n = 0; n < 9; ++n) {
                this.addSlot(new Slot(playerInventory, n + o * 9 + 9, 8 + n * 18, 84 + o * 18));
            }
        }

        for (o = 0; o < 9; ++o) {
            this.addSlot(new Slot(playerInventory, o, 8 + o * 18, 142));
        }

    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        boolean bl = true;
        if (slot instanceof EnderiteShulkerSlot) {
            bl = this.acceptItems(stack);
        }
        return super.canInsertIntoSlot(stack, slot) && bl;
    }

    public boolean acceptItems(ItemStack stack) {
        boolean bl1 = stack.getItem() == EnderiteShulkerBoxBlock.getItemStack().getItem();
        boolean bl2 = Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock;
        return !bl1 && !bl2;
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}
