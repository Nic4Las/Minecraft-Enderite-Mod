package net.enderitemc.enderitemod.block;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EnderiteOre extends Block {

    public EnderiteOre() {
        super(Properties.create(Material.IRON, MaterialColor.BLACK).sound(SoundType.STONE)
                .hardnessAndResistance(-1.0f, 9.0F).noDrops());
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) {
        world.setBlockState(pos, Registration.CRACKED_ENDERITE_ORE.get().getDefaultState());
    }
}
