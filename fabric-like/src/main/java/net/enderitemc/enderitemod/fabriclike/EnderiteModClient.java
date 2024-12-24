package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorRenderer;
import net.enderitemc.enderitemod.renderer.RendererRegistries;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class EnderiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
            EnderiteShulkerBoxBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(),
            EnderiteRespawnAnchorRenderer::new);

        // Model render predicates are now done in json
        // Item model states/properties
        RendererRegistries.init();
    }

}