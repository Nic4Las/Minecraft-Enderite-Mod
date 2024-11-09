package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.block.RespawnAnchorBlock.CHARGES;

public class EnderiteRespawnAnchorBlockEntity extends BlockEntity {
    public int charge;

    public EnderiteRespawnAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(), pos, state);
        charge = state.get(CHARGES);
    }

    public boolean isCharged() {
        return this.charge > 0;
    }

    public boolean shouldRenderPortal() {
        return !this.hasWorld() || !this.getWorld().getBlockState(pos.up()).isSolid();
    }
}
