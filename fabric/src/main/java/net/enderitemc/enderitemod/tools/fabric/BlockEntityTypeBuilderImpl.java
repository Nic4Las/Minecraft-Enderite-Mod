package net.enderitemc.enderitemod.tools.fabric;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public final class BlockEntityTypeBuilderImpl {
    private BlockEntityTypeBuilderImpl() {
    }

    public static <T extends BlockEntity> BlockEntityType<T> create(
        BlockEntityType.BlockEntityFactory<? extends T> blockEntityFactory, Block... blocks
    ) {
        return FabricBlockEntityTypeBuilder.create((FabricBlockEntityTypeBuilder.Factory<T>) blockEntityFactory::create, blocks).build();
    }
}
