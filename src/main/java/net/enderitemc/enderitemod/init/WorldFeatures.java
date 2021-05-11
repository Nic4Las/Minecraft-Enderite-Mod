package net.enderitemc.enderitemod.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.world.gen.BiomeHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldFeatures {
        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                RuleTest fillerBlockType = new BlockMatchRuleTest(Blocks.END_STONE);

                BlockState state = ForgeRegistries.BLOCKS
                                .getValue(new ResourceLocation(EnderiteMod.MOD_ID, "enderite_ore")).defaultBlockState();

                // CountRangeConfig countConfig = new CountRangeConfig(3, 12, 12, 48);
                int count = EnderiteModConfig.ENDERITE_COUNT.get();
                int bottomOffset = EnderiteModConfig.ENDERITE_BOTTOM_OFFSET.get();
                int topOffset = EnderiteModConfig.ENDERITE_TOP_OFFSET.get();
                int maximum = EnderiteModConfig.ENDERITE_MAXIMUM.get();
                int veinSize = EnderiteModConfig.ENDERITE_VEIN_SIZE.get() + 2;
                LOGGER.debug("ENDERITE ORE GENERATION SETTINGS: " + count + ", " + bottomOffset + ", " + topOffset
                                + ", " + maximum);
                TopSolidRangeConfig countConfig = new TopSolidRangeConfig(bottomOffset, topOffset, maximum);

                ConfiguredFeature<?, ?> enderite_cf = Feature.ORE
                                .configured(new OreFeatureConfig(fillerBlockType, state, veinSize))
                                .decorated(Placement.RANGE.configured(countConfig)).squared().count(count);

                Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                                new ResourceLocation(EnderiteMod.MOD_ID, "enderite_cf"), enderite_cf);
                WorldGenRegistries.BIOME.forEach(biome -> {
                        if (biome.getBiomeCategory() == Biome.Category.THEEND) {
                                BiomeHandler handler = new BiomeHandler(biome);
                                handler.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, enderite_cf);
                        }
                });
        }

}
