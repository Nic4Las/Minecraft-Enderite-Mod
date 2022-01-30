package net.enderitemc.enderitemod.block;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EnderiteOre extends Block {

    public EnderiteOre() {
        super(Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).sound(SoundType.STONE).strength(-1.0f, 9.0F)
                .noDrops());
    }

    @Override
    public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
        world.setBlockAndUpdate(pos, Registration.CRACKED_ENDERITE_ORE.get().defaultBlockState());
    }
}
