package net.enderitemc.enderitemod.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;

public class CrackedEnderiteOre extends Block {

    public CrackedEnderiteOre() {
        super(FabricBlockSettings.of(Material.METAL, MaterialColor.BLACK).requiresTool()
                .sounds(BlockSoundGroup.ANCIENT_DEBRIS).breakByTool(FabricToolTags.PICKAXES, 4)
                .strength(20.0f, 1200.0F));
    }

}