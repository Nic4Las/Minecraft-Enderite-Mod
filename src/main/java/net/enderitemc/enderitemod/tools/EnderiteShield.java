package net.enderitemc.enderitemod.tools;

import java.util.List;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class EnderiteShield extends ShieldItem {

    public EnderiteShield(Settings settings) {
        super(settings);
    }

    @Override
    public int getEnchantability() {
        return EnderiteMaterial.ENDERITE.getEnchantability();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (itemStack.getNbt().contains("teleport_charge")) {
            String charge = itemStack.getNbt().get("teleport_charge").toString();
            tooltip.add(Text.translatable("item.enderitemod.enderite_sword.charge")
                    .formatted(new Formatting[] { Formatting.DARK_AQUA }).append(Text.literal(": " + charge)));
        } else {
            tooltip.add(Text.translatable("item.enderitemod.enderite_sword.charge")
                    .formatted(new Formatting[] { Formatting.DARK_AQUA }).append(Text.literal(": 0")));
        }
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip1")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));
        tooltip.add(Text.translatable("item.enderitemod.enderite_sword.tooltip2")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));
        tooltip.add(Text.translatable("item.enderitemod.enderite_shield.tooltip3")
                .formatted(new Formatting[] { Formatting.GRAY, Formatting.ITALIC }));

    }

}