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
        super(Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).sound(SoundType.STONE).strength(-1.0f, 9.0F)
                .noDrops());
    }

    @Override
    public void wasExploded(World world, BlockPos pos, Explosion explosion) {
        world.setBlockAndUpdate(pos, Registration.CRACKED_ENDERITE_ORE.get().defaultBlockState());
    }
}
