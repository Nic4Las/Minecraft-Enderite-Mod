package net.enderitemc.enderitemod;

import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class EnderiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY,
                EnderiteShulkerBoxBlockEntityRenderer::new);
    }
}