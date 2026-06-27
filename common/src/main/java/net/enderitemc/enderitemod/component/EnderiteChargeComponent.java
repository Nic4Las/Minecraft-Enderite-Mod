package net.enderitemc.enderitemod.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public class EnderiteChargeComponent extends Number implements TooltipProvider {
    public static Codec<EnderiteChargeComponent> CODEC = ExtraCodecs.NON_NEGATIVE_INT.xmap(
        EnderiteChargeComponent::new,
        EnderiteChargeComponent::charge
    );

    public static StreamCodec<ByteBuf, EnderiteChargeComponent> PACKET_CODEC = new StreamCodec<ByteBuf, EnderiteChargeComponent>() {
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

    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        int charge = components.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
        textConsumer.accept(Component.translatable("item.enderitemod.enderite_sword.charge")
                .withStyle(new ChatFormatting[]{ChatFormatting.DARK_AQUA}).append(Component.literal(": " + charge)));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
