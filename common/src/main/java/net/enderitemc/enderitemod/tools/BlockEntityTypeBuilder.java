package net.enderitemc.enderitemod.tools;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public final class BlockEntityTypeBuilder {
    private BlockEntityTypeBuilder() {
    }

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityType<T> create(
        BlockEntityType.BlockEntityFactory<? extends T> blockEntityFactory, Block... blocks
    ) {
        throw new AssertionError();
    }
}
