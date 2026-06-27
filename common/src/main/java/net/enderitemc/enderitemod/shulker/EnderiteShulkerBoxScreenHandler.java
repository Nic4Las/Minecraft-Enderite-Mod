package net.enderitemc.enderitemod.shulker;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class EnderiteShulkerBoxScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public EnderiteShulkerBoxScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(45));
    }

    public EnderiteShulkerBoxScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(MenuType.GENERIC_9x5, syncId);
        checkContainerSize(inventory, 45);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

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

    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        boolean bl = true;
        if (slot instanceof EnderiteShulkerSlot) {
            bl = this.acceptItems(stack);
        }
        return super.canTakeItemForPickAll(stack, slot) && bl;
    }

    public boolean acceptItems(ItemStack stack) {
        boolean bl1 = stack.getItem() == EnderiteShulkerBoxBlock.getItemStack().getItem();
        boolean bl2 = Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock;
        return !bl1 && !bl2;
    }

    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }
}
