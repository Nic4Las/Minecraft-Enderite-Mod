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
    public int getItemEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairMaterial().test(ingredient);
    }

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return stack.getItem() instanceof EnderiteShield;
    }

    @Override
    public void addInformation(ItemStack itemStack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getOrCreateTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .func_240701_a_(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .func_230529_a_(new StringTextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.charge")
                    .func_240701_a_(new TextFormatting[] { TextFormatting.DARK_AQUA })
                    .func_230529_a_(new StringTextComponent(":0")));
        }

        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip1")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_sword.tooltip2")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
        tooltip.add(new TranslationTextComponent("item.enderitemod.enderite_shield.tooltip3")
                .func_240701_a_(new TextFormatting[] { TextFormatting.GRAY, TextFormatting.ITALIC }));
    }

}