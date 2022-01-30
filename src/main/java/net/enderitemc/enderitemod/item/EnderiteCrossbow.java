package net.enderitemc.enderitemod.item;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;

public class EnderiteCrossbow extends CrossbowItem {

    public EnderiteCrossbow(Properties propertiesIn) {
        super(propertiesIn);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return super.getUseDuration(stack) + 10;
    }

    public static int getChargeTime(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 35 : 35 - 7 * i;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

}