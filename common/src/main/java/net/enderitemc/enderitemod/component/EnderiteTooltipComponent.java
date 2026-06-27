package net.enderitemc.enderitemod.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record EnderiteTooltipComponent(boolean is_shield) implements TooltipProvider {

    public static Codec<EnderiteTooltipComponent> CODEC = Codec.BOOL.xmap(
        EnderiteTooltipComponent::new,
        EnderiteTooltipComponent::is_shield
    );

    public static StreamCodec<ByteBuf, EnderiteTooltipComponent> PACKET_CODEC = new StreamCodec<ByteBuf, EnderiteTooltipComponent>() {
        public EnderiteTooltipComponent decode(ByteBuf byteBuf) {
            return new EnderiteTooltipComponent(byteBuf.readBoolean());
        }

        public void encode(ByteBuf byteBuf, EnderiteTooltipComponent boolean_) {
            byteBuf.writeBoolean(boolean_.is_shield);
        }
    };

    public static EnderiteTooltipComponent ofSword() {
        return new EnderiteTooltipComponent(false);
    }

    public static EnderiteTooltipComponent ofShield() {
        return new EnderiteTooltipComponent(true);
    }

    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.enderitemod.enderite_sword.tooltip1")
            .withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
        textConsumer.accept(Component.translatable("item.enderitemod.enderite_sword.tooltip2")
            .withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
        if(is_shield) {
            textConsumer.accept(Component.translatable("item.enderitemod.enderite_shield.tooltip3")
                .withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
        } else {
            textConsumer.accept(Component.translatable("item.enderitemod.enderite_sword.tooltip3")
                .withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}));
        }

    }
}
