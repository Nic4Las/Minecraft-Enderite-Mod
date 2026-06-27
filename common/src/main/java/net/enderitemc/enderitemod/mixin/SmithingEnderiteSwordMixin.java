package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingEnderiteSwordMixin extends ItemCombinerMenu {

    @Shadow
    public abstract void shrinkStackInSlot(int i);

    @Final
    @Shadow
    private Level level;

    public SmithingEnderiteSwordMixin(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(MenuType.SMITHING, syncId, playerInventory, context, ItemCombinerMenuSlotDefinition.create().build());
    }

    @Inject(at = @At("TAIL"), method = "createResult")
    private void enderitemod$update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.inputSlots.getItem(1);
        ItemStack pearls1 = this.inputSlots.getItem(0);
        ItemStack pearls2 = this.inputSlots.getItem(2);
        if ((sword.is(EnderiteTools.ENDERITE_SWORD.get()) || sword.is(EnderiteTools.ENDERITE_SHIELD.get()))
            && (pearls1.is(Items.ENDER_PEARL) || pearls1.isEmpty()) && (pearls2.is(Items.ENDER_PEARL))) {
            ItemStack newSword = sword.copy();

            // If new sword, basic charge is enderpearl count
            int pearls = pearls1.getCount() + pearls2.getCount();

            // Read the charge of sword
            int teleport_charge = sword.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();

            // Clamp teleport charge
            if (teleport_charge < EnderiteMod.CONFIG.tools.maxTeleportCharge) {
                // Charge is old charge + amount of enderpearls
                teleport_charge = Math.min(teleport_charge + pearls, EnderiteMod.CONFIG.tools.maxTeleportCharge);
                // Copy the same sword and put charge into it
                newSword.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(teleport_charge));
                this.resultSlots.setItem(0, newSword);
            } else {
                this.resultSlots.clearContent();
            }
        }
    }

    @Unique
    private void enderitemod$superDecrement(int i, int amount) {
        ItemStack itemStack = this.inputSlots.getItem(i);
        if (!itemStack.isEmpty()) {
            itemStack.shrink(amount);
            this.inputSlots.setItem(i, itemStack);
        }
    }

    @Inject(at = @At("HEAD"), method = "onTake")
    private void enderitemod$nowTake(Player player, ItemStack itemStack, CallbackInfo info) {
        // Take all pearls
        ItemStack sword = this.inputSlots.getItem(1);
        ItemStack pearls1 = this.inputSlots.getItem(0);
        ItemStack pearls2 = this.inputSlots.getItem(2);
        if ((sword.is(EnderiteTools.ENDERITE_SWORD.get())
            || sword.is(EnderiteTools.ENDERITE_SHIELD.get()))
            && (pearls1.is(Items.ENDER_PEARL) || pearls1.isEmpty())
            && pearls2.is(Items.ENDER_PEARL)) {

            int amountToSubstract = pearls1.getCount() + pearls2.getCount();
            // Read the charge of sword

            // Charge is old charge + amount of enderpearls
            int tp_charge = sword.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
            int allowableSubstract = EnderiteMod.CONFIG.tools.maxTeleportCharge - tp_charge;
            amountToSubstract = Math.min(allowableSubstract, amountToSubstract);

            int half = amountToSubstract / 2;
            int subtract1 = Math.min(half, pearls1.getCount());
            int subtract2 = Math.min(amountToSubstract - subtract1, pearls2.getCount());
            subtract1 = Math.min(amountToSubstract - subtract2, pearls1.getCount());
            this.enderitemod$superDecrement(0, subtract1 - 1);
            this.enderitemod$superDecrement(2, subtract2 - 1);
        }
    }
}
