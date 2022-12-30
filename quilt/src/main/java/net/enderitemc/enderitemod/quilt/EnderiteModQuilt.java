package net.enderitemc.enderitemod.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.enderitemc.enderitemod.fabriclike.EnderiteModFabricLike;

public class EnderiteModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        EnderiteModFabricLike.init();
    }
}
