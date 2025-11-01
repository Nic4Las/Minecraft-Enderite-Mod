package net.enderitemc.enderitemod.component;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.component.ComponentType;

import java.util.function.UnaryOperator;

import static net.enderitemc.enderitemod.EnderiteMod.DATA_COMPONENT_TYPES;


public class EnderiteDataComponents {
    public static final RegistrySupplier<ComponentType<EnderiteChargeComponent>> TELEPORT_CHARGE = register("teleport_charge", (builder) -> {
        return builder.codec(EnderiteChargeComponent.CODEC).packetCodec(EnderiteChargeComponent.PACKET_CODEC);
    });

    public static final RegistrySupplier<ComponentType<EnderiteTooltipComponent>> ENDERITE_TOOLTIP = register("enderite_tooltip", (builder) -> {
        return builder.codec(EnderiteTooltipComponent.CODEC).packetCodec(EnderiteTooltipComponent.PACKET_CODEC);
    });


    private static <T> RegistrySupplier<ComponentType<T>> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(id, () -> builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {
        DATA_COMPONENT_TYPES.register();
    }
}
