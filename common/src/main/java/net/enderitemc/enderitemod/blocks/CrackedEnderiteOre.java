package net.enderitemc.enderitemod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class CrackedEnderiteOre extends Block {

    public CrackedEnderiteOre() {
        super(AbstractBlock.Settings.of(Material.METAL, MapColor.BLACK).requiresTool()
                .sounds(BlockSoundGroup.ANCIENT_DEBRIS).strength(20.0f, 1200.0F));
    }

}