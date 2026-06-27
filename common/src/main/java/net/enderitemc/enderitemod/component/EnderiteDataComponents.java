package net.enderitemc.enderitemod.component;

import dev.architectury.registry.registries.RegistrySupplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;

import static net.enderitemc.enderitemod.EnderiteMod.DATA_COMPONENT_TYPES;


public class EnderiteDataComponents {
    public static final RegistrySupplier<DataComponentType<EnderiteChargeComponent>> TELEPORT_CHARGE = register("teleport_charge", (builder) -> {
        return builder.persistent(EnderiteChargeComponent.CODEC).networkSynchronized(EnderiteChargeComponent.PACKET_CODEC);
    });

    public static final RegistrySupplier<DataComponentType<EnderiteTooltipComponent>> ENDERITE_TOOLTIP = register("enderite_tooltip", (builder) -> {
        return builder.persistent(EnderiteTooltipComponent.CODEC).networkSynchronized(EnderiteTooltipComponent.PACKET_CODEC);
    });


    private static <T> RegistrySupplier<DataComponentType<T>> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(id, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void init() {
        DATA_COMPONENT_TYPES.register();
    }
}
