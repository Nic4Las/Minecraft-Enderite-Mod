package net.enderitemc.enderitemod.forge.tools;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EnderiteElytraChestplate extends ArmorItem {
    public EnderiteElytraChestplate(ArmorMaterial materialIn, ArmorItem.Type type, Settings properties) {
        super(materialIn, type, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 10;
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
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        EquipmentSlot equipmentslottype = EquipmentSlot.CHEST;
        ItemStack itemstack1 = playerIn.getEquippedStack(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.equipStack(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return TypedActionResult.success(itemstack, worldIn.isClient());
        } else {
            return TypedActionResult.fail(itemstack);
        }
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.getWorld().isClient() && (flightTicks + 1) % 20 == 0) {
            stack.damage(1, entity, e -> ((LivingEntity) e).sendEquipmentBreakStatus(EquipmentSlot.CHEST));
        }
        return isUsable(stack);
    }
}
