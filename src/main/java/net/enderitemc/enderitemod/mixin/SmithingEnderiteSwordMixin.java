package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordMixin extends ForgingScreenHandler {

    public SmithingEnderiteSwordMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "updateResult")
    private void update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.input.getStack(0);
        ItemStack pearls = this.input.getStack(1);
        if ((sword.getItem() == EnderiteMod.ENDERITE_SWORD || sword.getItem() == EnderiteMod.ENDERITE_SHIELD)
                && pearls.getItem() == Items.ENDER_PEARL) {
            // If new sword, basic charge is enderpearl count
            int teleport_charge = pearls.getCount();
            // Read the charge of sword
            if (sword.getTag().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                teleport_charge = Integer.parseInt(sword.getTag().get("teleport_charge").asString())
                        + pearls.getCount();
            }
            if (teleport_charge > 64) {
                teleport_charge = 64;
            }
            // Copy the same sword and put charge into it
            ItemStack newSword = sword.copy();
            newSword.getTag().putInt("teleport_charge", teleport_charge);
            this.output.setStack(0, newSword);
        }
    }
}