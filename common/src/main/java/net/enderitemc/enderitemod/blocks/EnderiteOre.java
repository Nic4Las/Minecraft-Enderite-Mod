package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;


public class EnderiteOre extends Block {

    public EnderiteOre(AbstractBlock.Settings settings) {
        super(settings.mapColor(MapColor.BLACK).sounds(BlockSoundGroup.STONE)
            .strength(-1.0f, 9.0F).dropsNothing());
    }

    @Override
    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        world.setBlockState(pos, EnderiteMod.CRACKED_ENDERITE_ORE.get().getDefaultState());
    }

}