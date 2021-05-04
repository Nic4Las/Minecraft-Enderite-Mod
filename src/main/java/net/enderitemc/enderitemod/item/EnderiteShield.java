package net.enderitemc.enderitemod.item;

import java.util.List;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EnderiteShield extends ShieldItem {

    public EnderiteShield(Properties builder) {
        super(builder);
    }

    @Override
    public int getEnchantmentValue() {
        return EnderiteMaterial.ENDERITE.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return stack.getItem() instanceof EnderiteShield;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getOrCreateTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .append(new StringTextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .append(new StringTextComponent(":0")));
        }

        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip1")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip2")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_shield.tooltip3")
                .withStyle(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
    }

}