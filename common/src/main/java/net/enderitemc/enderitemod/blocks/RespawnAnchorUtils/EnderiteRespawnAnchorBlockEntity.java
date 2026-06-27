package net.enderitemc.enderitemod.blocks.RespawnAnchorUtils;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.RespawnAnchorBlock.CHARGE;

public class EnderiteRespawnAnchorBlockEntity extends BlockEntity {
    public int charge;

    public EnderiteRespawnAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(), pos, state);
        charge = state.getValue(CHARGE);
    }

    public boolean isCharged() {
        return this.charge > 0;
    }

    public boolean shouldRenderPortal() {
        return !this.hasLevel() || !this.getLevel().getBlockState(worldPosition.above()).isSolid();
    }
}
