package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;


public class EnderiteOre extends Block {

    public EnderiteOre(BlockBehaviour.Properties settings) {
        super(settings.mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE)
            .strength(-1.0f, 9.0F).noLootTable());
    }

    @Override
    public void wasExploded(ServerLevel world, BlockPos pos, Explosion explosion) {
        world.setBlockAndUpdate(pos, EnderiteMod.CRACKED_ENDERITE_ORE.get().defaultBlockState());
    }

}