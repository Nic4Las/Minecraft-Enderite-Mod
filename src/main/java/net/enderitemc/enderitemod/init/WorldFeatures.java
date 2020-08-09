package net.enderitemc.enderitemod.init;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.world.gen.EnderiteOreHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldFeatures {
        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                OreFeatureConfig.FillerBlockType fillerBlockType = OreFeatureConfig.FillerBlockType.create("END_STONE",
                                "endstone", new BlockMatcher(Blocks.END_STONE));
                                
                BlockState state = ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation(EnderiteMod.MOD_ID, "enderite_ore")).getDefaultState();

                //CountRangeConfig countConfig = new CountRangeConfig(3, 12, 12, 48);
                int count = EnderiteModConfig.ENDERITE_COUNT.get();
                int bottomOffset = EnderiteModConfig.ENDERITE_BOTTOM_OFFSET.get();
                int topOffset = EnderiteModConfig.ENDERITE_TOP_OFFSET.get();
                int maximum = EnderiteModConfig.ENDERITE_MAXIMUM.get();
                LOGGER.debug("ENDERITE ORE GENERATION SETTINGS: "+count+", "+bottomOffset+", "+topOffset+", "+maximum);
                CountRangeConfig countConfig = new CountRangeConfig(count, bottomOffset, topOffset, maximum);
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
