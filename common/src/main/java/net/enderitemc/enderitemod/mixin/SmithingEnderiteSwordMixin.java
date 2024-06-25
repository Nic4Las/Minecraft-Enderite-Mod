package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordMixin extends ForgingScreenHandler {

    @Final
    @Shadow
    private World world;

    public SmithingEnderiteSwordMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
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
}