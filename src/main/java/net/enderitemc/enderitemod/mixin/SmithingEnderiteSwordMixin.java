package net.enderitemc.enderitemod.mixin;

import java.util.List;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingEnderiteSwordMixin extends ForgingScreenHandler {
    private final String ENDERITE_ARMOR_TRIM_UPGRADE_NAME = "Enderite Trim Armor Up";

    public SmithingEnderiteSwordMixin(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerType.SMITHING, syncId, playerInventory, context);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "updateResult")
    private void update(CallbackInfo info) {
        // Overwrites smithing screen to accept enderpearls as charge for sword
        ItemStack sword = this.input.getStack(1);
        ItemStack pearls = this.input.getStack(2);
        if ((sword.isOf(EnderiteMod.ENDERITE_SWORD) || sword.isOf(EnderiteMod.ENDERITE_SHIELD))
                && (pearls.isOf(Items.ENDER_PEARL))) {
            // If new sword, basic charge is enderpearl count
            int teleport_charge = pearls.getCount();
            // Read the charge of sword
            if (sword.getNbt().contains("teleport_charge")) {
                // Charge is old charge + amount of enderpearls
                teleport_charge = Integer.parseInt(sword.getNbt().get("teleport_charge").asString())
                        + pearls.getCount();
            }
            if (teleport_charge > 64) {
                teleport_charge = 64;
            }
            // Copy the same sword and put charge into it
            ItemStack newSword = sword.copy();
            newSword.getNbt().putInt("teleport_charge", teleport_charge);
            this.output.setStack(0, newSword);
        }
        ItemStack outputStack = this.output.getStack(0);
        if(outputStack.isIn(EnderiteTag.ENDERITE_ITEM) && outputStack.isIn(ItemTags.TRIMMABLE_ARMOR)) {
            NbtCompound nbt = outputStack.getNbt();
            if(nbt!=null) {
                NbtCompound trim_nbt = nbt.getCompound("Trim");
                if(trim_nbt!=null) {
                    if(trim_nbt.getString("material").equals("enderitemod:enderite")) {
                        trim_nbt.putString("material","enderitemod:enderite_darker");
                    }
                    nbt.put("Trim", trim_nbt);
                }
                outputStack.setNbt(nbt);
            }
        }
    }
}