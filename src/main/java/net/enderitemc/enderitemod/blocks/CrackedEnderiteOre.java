package net.enderitemc.enderitemod.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class CrackedEnderiteOre extends Block {

    public CrackedEnderiteOre() {
        super(FabricBlockSettings.of(Material.METAL, MapColor.BLACK).requiresTool()
                .sounds(BlockSoundGroup.ANCIENT_DEBRIS).strength(20.0f, 1200.0F));
    }

}