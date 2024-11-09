package net.enderitemc.enderitemod.tools;

import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EnderiteShield extends ShieldItem {

    public EnderiteShield(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        String charge = itemStack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).toString();
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.charge")
            .formatted(new Formatting[]{Formatting.DARK_AQUA}).append(Text.literal(": " + charge)));
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip1")
            .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip2")
            .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        tooltip.add(Text.translatable("item.enderitemod.enderite_shield.tooltip3")
            .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
    }

}