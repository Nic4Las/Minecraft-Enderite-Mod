package net.enderitemc.enderitemod.events;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class GenerationEvent {

    private static final ArrayList<ConfiguredFeature<?, ?>> enderOres = new ArrayList<ConfiguredFeature<?, ?>>();

    public static void registerOres() {
        int count = EnderiteModConfig.ENDERITE_COUNT.get();
        int bottomOffset = EnderiteModConfig.ENDERITE_BOTTOM_OFFSET.get();
        int topOffset = EnderiteModConfig.ENDERITE_TOP_OFFSET.get();
        int maximum = EnderiteModConfig.ENDERITE_MAXIMUM.get();
        enderOres.add(register("end_enderite_ore", Feature.ORE
                .configured(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE),
                        Registration.ENDERITE_ORE.get().defaultBlockState(), 3))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(bottomOffset, topOffset, maximum)))
                .squared() // spawn height
                .count(count))); // spawn frequency per chunks
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.Category.THEEND)) {
            for (ConfiguredFeature<?, ?> ore : enderOres) {
                if (ore != null)
                    generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
            }
        }
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name,
            ConfiguredFeature<FC, ?> configureFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(EnderiteMod.MOD_ID + ":" + name), configureFeature);
    }
}
