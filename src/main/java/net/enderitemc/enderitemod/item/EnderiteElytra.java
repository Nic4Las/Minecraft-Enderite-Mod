package net.enderitemc.enderitemod.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EnderiteElytra extends ArmorItem {
    public EnderiteElytra(IArmorMaterial materialIn, EquipmentSlotType slot, Properties properties) {
        super(materialIn, slot, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.PHANTOM_MEMBRANE;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when
     * this item is used on a Block, see {@link #onItemUse}.
     */
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
        } else {
            return ActionResult.fail(itemstack);
        }
    }
}
