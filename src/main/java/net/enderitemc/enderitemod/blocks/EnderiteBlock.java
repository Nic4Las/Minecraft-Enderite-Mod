package net.enderitemc.enderitemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public class EnderiteBlock extends Block {

    public EnderiteBlock(Material material) {
        super(FabricBlockSettings.of(material, MaterialColor.BLACK).requiresTool().strength(66.0F, 1200.0F)
                .sounds(BlockSoundGroup.NETHERITE).breakByTool(FabricToolTags.PICKAXES, 4));

    }

}