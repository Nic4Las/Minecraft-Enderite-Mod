package net.enderitemc.enderitemod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class EnderiteBlock extends Block {

    public EnderiteBlock(Material material) {
        super(AbstractBlock.Settings.of(material, MapColor.BLACK).requiresTool().strength(66.0F, 1200.0F)
                .sounds(BlockSoundGroup.NETHERITE));

    }

}