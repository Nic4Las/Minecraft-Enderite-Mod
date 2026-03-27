package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorRenderer;
import net.enderitemc.enderitemod.renderer.RendererRegistries;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

public class EnderiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EnderiteMod.clientInit();

        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
            EnderiteShulkerBoxBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(),
            EnderiteRespawnAnchorRenderer::new);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceLoader.registerBuiltinPack(
                    Identifier.of(MOD_ID, "alternative_textures_amber3562"),
                    container,
                    Text.of("Alternative Enderitemod Textures (by Amber3562)"),
                    PackActivationType.NORMAL
            );
        });

        // Model render predicates are now done in json
        // Item model states/properties
        RendererRegistries.init();
    }

}