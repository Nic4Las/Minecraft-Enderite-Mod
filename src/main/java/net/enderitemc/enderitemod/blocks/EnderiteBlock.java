package net.enderitemc.enderitemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class EnderiteBlock extends Block {

    public EnderiteBlock(Material material) {
        super(FabricBlockSettings.of(material, MapColor.BLACK).requiresTool().strength(66.0F, 1200.0F)
                .sounds(BlockSoundGroup.NETHERITE));

    }

}