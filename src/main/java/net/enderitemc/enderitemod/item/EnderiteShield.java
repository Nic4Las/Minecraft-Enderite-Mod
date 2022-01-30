package net.enderitemc.enderitemod.item;

import java.util.List;
import java.util.function.Consumer;

import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.NonNullLazy;

import net.minecraft.world.item.Item.Properties;

public class EnderiteShield extends ShieldItem {

    public EnderiteShield(Properties builder) {
        super(builder);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> EnderiteShieldRenderer.INSTANCE);

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public int getEnchantmentValue() {
        return EnderiteMaterial.ENDERITE.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return EnderiteMaterial.ENDERITE.getRepairIngredient().test(ingredient);
    }

    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return stack.getItem() instanceof EnderiteShield;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction)
    {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, tooltip, flagIn);
        if (itemStack.getOrCreateTag().contains("teleport_charge")) {
            String charge = itemStack.getTag().get("teleport_charge").toString();
            tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new ChatFormatting[] { ChatFormatting.DARK_AQUA })
                    .append(new TextComponent(": " + charge)));
        } else {
            tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.charge")
                    .withStyle(new ChatFormatting[] { ChatFormatting.DARK_AQUA })
                    .append(new TextComponent(":0")));
        }

        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.tooltip1")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_sword.tooltip2")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
        tooltip.add(new TranslatableComponent("item.enderitemod.enderite_shield.tooltip3")
                .withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }));
    }

}