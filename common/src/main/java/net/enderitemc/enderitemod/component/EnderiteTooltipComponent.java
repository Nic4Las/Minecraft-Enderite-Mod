package net.enderitemc.enderitemod.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public record EnderiteTooltipComponent(boolean is_shield) implements TooltipAppender {

    public static Codec<EnderiteTooltipComponent> CODEC = Codec.BOOL.xmap(
        EnderiteTooltipComponent::new,
        EnderiteTooltipComponent::is_shield
    );

    public static PacketCodec<ByteBuf, EnderiteTooltipComponent> PACKET_CODEC = new PacketCodec<ByteBuf, EnderiteTooltipComponent>() {
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

    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(Text.translatable("item.enderitemod.enderite_sword.tooltip1")
            .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        textConsumer.accept(Text.translatable("item.enderitemod.enderite_sword.tooltip2")
            .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        if(is_shield) {
            textConsumer.accept(Text.translatable("item.enderitemod.enderite_shield.tooltip3")
                .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        } else {
            textConsumer.accept(Text.translatable("item.enderitemod.enderite_sword.tooltip3")
                .formatted(new Formatting[]{Formatting.GRAY, Formatting.ITALIC}));
        }

    }
}
