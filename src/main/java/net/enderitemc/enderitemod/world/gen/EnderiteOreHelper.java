package net.enderitemc.enderitemod.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnderiteOreHelper extends OreFeatureConfig {
    public final Predicate<BlockState> blockPredicate;

    public EnderiteOreHelper(FillerBlockType target, BlockState state, int size) {
        super(FillerBlockType.create("END_STONE","endstone", new BlockMatcher(Blocks.END_STONE)), state, size);
        this.blockPredicate = state1 -> state1 != null && state1.equals(Blocks.END_STONE.getDefaultState());
    }
}
