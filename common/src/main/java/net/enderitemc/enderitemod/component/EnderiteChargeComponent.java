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
import net.minecraft.util.dynamic.Codecs;

import java.util.function.Consumer;

public class EnderiteChargeComponent extends Number implements TooltipAppender {
    public static Codec<EnderiteChargeComponent> CODEC = Codecs.NON_NEGATIVE_INT.xmap(
        EnderiteChargeComponent::new,
        EnderiteChargeComponent::charge
    );

    public static PacketCodec<ByteBuf, EnderiteChargeComponent> PACKET_CODEC = new PacketCodec<ByteBuf, EnderiteChargeComponent>() {
        public EnderiteChargeComponent decode(ByteBuf byteBuf) {
            return new EnderiteChargeComponent(byteBuf.readInt());
        }

        public void encode(ByteBuf byteBuf, EnderiteChargeComponent boolean_) {
            byteBuf.writeInt(boolean_.charge);
        }
    };

    private final int charge;

    public EnderiteChargeComponent(int charge) {
        this.charge = charge;
    }

    public static EnderiteChargeComponent of(int charge) {
        return new EnderiteChargeComponent(charge);
    }

    public int charge() {
        return charge;
    }

    public int intValue() {
        return charge;
    }

    @Override
    public long longValue() {
        return charge;
    }

    @Override
    public float floatValue() {
        return charge;
    }

    @Override
    public double doubleValue() {
        return charge;
    }

    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        int charge = components.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
        textConsumer.accept(Text.translatable("item.enderitemod.enderite_sword.charge")
                .formatted(new Formatting[]{Formatting.DARK_AQUA}).append(Text.literal(": " + charge)));
    }
}
