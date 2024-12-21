package net.enderitemc.enderitemod.tools.forge;


import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public final class BlockEntityTypeBuilderImpl {
    private BlockEntityTypeBuilderImpl() {
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(
        BlockEntityType.BlockEntityFactory<? extends T> blockEntityFactory, Block... blocks
    ) {
        return new BlockEntityType<>(blockEntityFactory, blocks);
    }
}
