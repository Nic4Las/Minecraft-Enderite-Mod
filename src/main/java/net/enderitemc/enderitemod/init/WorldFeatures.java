package net.enderitemc.enderitemod.init;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.world.gen.EnderiteOreHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldFeatures {
        public static void init() {
                BiomeDictionary.getTypes(Biomes.THE_END);

                OreFeatureConfig.FillerBlockType fillerBlockType = OreFeatureConfig.FillerBlockType.create("END_STONE",
                                "endstone", new BlockMatcher(Blocks.END_STONE));
                                
                BlockState state = ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation(EnderiteMod.MOD_ID, "enderite_ore")).getDefaultState();

                CountRangeConfig countConfig = new CountRangeConfig(3, 12, 12, 48);
                Biomes.THE_END.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                                .withConfiguration(new EnderiteOreHelper(fillerBlockType, state, 3))
                                .withPlacement(Placement.COUNT_RANGE.configure(countConfig)));
                Biomes.END_BARRENS.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                                .withConfiguration(new EnderiteOreHelper(fillerBlockType, state, 3))
                                .withPlacement(Placement.COUNT_RANGE.configure(countConfig)));
                Biomes.END_HIGHLANDS.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                                .withConfiguration(new EnderiteOreHelper(fillerBlockType, state, 3))
                                .withPlacement(Placement.COUNT_RANGE.configure(countConfig)));
                Biomes.END_MIDLANDS.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                                .withConfiguration(new EnderiteOreHelper(fillerBlockType, state, 3))
                                .withPlacement(Placement.COUNT_RANGE.configure(countConfig)));
                Biomes.SMALL_END_ISLANDS.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
                                .withConfiguration(new EnderiteOreHelper(fillerBlockType, state, 3))
                                .withPlacement(Placement.COUNT_RANGE.configure(countConfig)));
        }
}
