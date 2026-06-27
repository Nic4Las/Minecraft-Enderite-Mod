package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class EnderiteRespawnAnchorRenderState extends BlockEntityRenderState {
    public boolean shouldRenderPortal = false;
}
