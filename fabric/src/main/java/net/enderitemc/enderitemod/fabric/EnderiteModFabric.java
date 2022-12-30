package net.enderitemc.enderitemod.fabric;

import net.enderitemc.enderitemod.fabriclike.EnderiteModFabricLike;
import net.fabricmc.api.ModInitializer;

public class EnderiteModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EnderiteModFabricLike.init();
    }
}
