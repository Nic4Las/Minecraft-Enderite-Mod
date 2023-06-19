package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class EnderiteOre extends Block {

    public EnderiteOre() {
        super(FabricBlockSettings.create().mapColor(MapColor.BLACK).sounds(BlockSoundGroup.STONE)
                .strength(-1.0f, 9.0F).dropsNothing());
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        world.setBlockState(pos, EnderiteMod.CRACKED_ENDERITE_ORE.getDefaultState());
    }

}