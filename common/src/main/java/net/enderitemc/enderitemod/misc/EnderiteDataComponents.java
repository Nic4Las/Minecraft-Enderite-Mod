package net.enderitemc.enderitemod.misc;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.function.UnaryOperator;

import static net.enderitemc.enderitemod.EnderiteMod.DATA_COMPONENT_TYPES;


public class EnderiteDataComponents {
    public static final RegistrySupplier<ComponentType<Integer>> TELEPORT_CHARGE = register("teleport_charge", (builder) -> {
        return builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT);
    });

    private static <T> RegistrySupplier<ComponentType<T>> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(id, () -> builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {
        DATA_COMPONENT_TYPES.register();
    }
}
