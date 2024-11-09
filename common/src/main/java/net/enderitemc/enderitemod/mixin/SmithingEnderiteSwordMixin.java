package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordMixin extends ForgingScreenHandler {

    @Shadow
    public abstract void decrementStack(int i);

    @Final
    @Shadow
    private World world;

    public SmithingEnderiteSwordMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context, ForgingSlotsManager.builder().build());
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "updateResult")
    private void update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.input.getStack(1);
        ItemStack pearls1 = this.input.getStack(0);
        ItemStack pearls2 = this.input.getStack(2);
        if ((sword.isOf(EnderiteTools.ENDERITE_SWORD.get()) || sword.isOf(EnderiteTools.ENDERITE_SHIELD.get()))
            && (pearls1.isOf(Items.ENDER_PEARL) || pearls1.isEmpty()) && (pearls2.isOf(Items.ENDER_PEARL))) {
            ItemStack newSword = sword.copy();

            // If new sword, basic charge is enderpearl count
            int pearls = pearls1.getCount() + pearls2.getCount();

            // Read the charge of sword
            int teleport_charge = sword.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0);

            // Clamp teleport charge
            if (teleport_charge < EnderiteMod.CONFIG.tools.maxTeleportCharge) {
                // Charge is old charge + amount of enderpearls
                teleport_charge = Math.min(teleport_charge + pearls, EnderiteMod.CONFIG.tools.maxTeleportCharge);
                // Copy the same sword and put charge into it
                newSword.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), teleport_charge);
                this.output.setStack(0, newSword);
            } else {
                this.output.clear();
            }
        }
    }

    @Unique
    private void superDecrement(int i, int amount) {
        ItemStack itemStack = this.input.getStack(i);
        if (!itemStack.isEmpty()) {
            itemStack.decrement(amount);
            this.input.setStack(i, itemStack);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "onTakeOutput")
    private void nowTake(PlayerEntity player, ItemStack itemStack, CallbackInfo info) {
        // Take all pearls
        ItemStack sword = this.input.getStack(1);
        ItemStack pearls1 = this.input.getStack(0);
        ItemStack pearls2 = this.input.getStack(2);
        if ((sword.isOf(EnderiteTools.ENDERITE_SWORD.get())
            || sword.isOf(EnderiteTools.ENDERITE_SHIELD.get()))
            && (pearls1.isOf(Items.ENDER_PEARL) || pearls1.isEmpty())
            && pearls2.isOf(Items.ENDER_PEARL)) {

            int amountToSubstract = pearls1.getCount() + pearls2.getCount();
            // Read the charge of sword

            // Charge is old charge + amount of enderpearls
            int tp_charge = sword.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0);
            int allowableSubstract = EnderiteMod.CONFIG.tools.maxTeleportCharge - tp_charge;
            amountToSubstract = Math.min(allowableSubstract, amountToSubstract);

            int half = amountToSubstract / 2;
            int subtract1 = Math.min(half, pearls1.getCount());
            int subtract2 = Math.min(amountToSubstract - subtract1, pearls2.getCount());
            subtract1 = Math.min(amountToSubstract - subtract2, pearls1.getCount());
            this.superDecrement(0, subtract1 - 1);
            this.superDecrement(2, subtract2 - 1);
        }
    }
}