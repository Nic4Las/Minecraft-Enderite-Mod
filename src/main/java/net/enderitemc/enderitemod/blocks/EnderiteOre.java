package net.enderitemc.enderitemod.blocks;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class EnderiteOre extends Block {

    public EnderiteOre() {
        super(FabricBlockSettings.of(Material.METAL, MapColor.BLACK).sounds(BlockSoundGroup.STONE)
                .strength(-1.0f, 9.0F).dropsNothing());
        // setDefaultState(getStateManager().getDefaultState().with(CRACKED, false));
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        // this.replaceBlock(world.getBlockState(pos),
        // EnderiteMod.CRACKED_ENDERITE_ORE.getDefaultState(), world, pos, 0,
        // 512);
        world.setBlockState(pos, EnderiteMod.CRACKED_ENDERITE_ORE.getDefaultState());
    }

}