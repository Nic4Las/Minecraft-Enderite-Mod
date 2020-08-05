package net.enderitemc.enderitemod;

import net.enderitemc.enderitemod.misc.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public class EnderiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY,
                EnderiteShulkerBoxBlockEntityRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(EnderiteMod.ENDERITE_SHIELD, new EnderiteShieldRenderer());
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlaxTexture, registry) -> {
            if (atlaxTexture.getId() == SpriteAtlasTexture.BLOCK_ATLAS_TEX) {
                registry.register(new Identifier("enderitemod:entity/enderite_shield_base"));
            }
        });
    }
}