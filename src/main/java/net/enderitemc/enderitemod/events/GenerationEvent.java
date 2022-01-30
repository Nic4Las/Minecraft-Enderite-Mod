package net.enderitemc.enderitemod.events;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class GenerationEvent {

    //private static final ArrayList<ConfiguredFeature<?, ?>> enderOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<PlacedFeature> enderOres = new ArrayList<PlacedFeature>();

    public static void registerOres() {
        int veinsize = EnderiteModConfig.ENDERITE_VEIN_SIZE.get()+2;
        int count = EnderiteModConfig.ENDERITE_COUNT.get();
        int bottomOffset = EnderiteModConfig.ENDERITE_BOTTOM_OFFSET.get();
        int topOffset = EnderiteModConfig.ENDERITE_TOP_OFFSET.get();
        int maximum = EnderiteModConfig.ENDERITE_MAXIMUM.get();
        ConfiguredFeature<?,?> configFeature = /*register("end_enderite_ore", */Feature.ORE
                .configured(new OreConfiguration(new TagMatchTest(Tags.Blocks.END_STONES),
                        Registration.ENDERITE_ORE.get().defaultBlockState(), veinsize));
                // .m_7679_(FeatureDecorator.f_70692_.m_70720_(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.absolute(bottomOffset), VerticalAnchor.absolute(maximum-topOffset)))))
                // .m_64152_() // spawn height
                // .m_64158_(count))); // spawn frequency per chunks
        enderOres.add(registerPlacedFeature("end_enderite_ore",configFeature,CountPlacement.of(count),InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(bottomOffset), VerticalAnchor.absolute(maximum-topOffset)), BiomeFilter.biome()));
        
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            for (PlacedFeature ore : enderOres) {
                if (ore != null)
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
            }
        }
    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name,
            ConfiguredFeature<FC, ?> configureFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new ResourceLocation(EnderiteMod.MOD_ID + ":" + name), configureFeature);
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> PlacedFeature registerPlacedFeature(String registryName,
               ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
        PlacedFeature placed = BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(EnderiteMod.MOD_ID + ":" + registryName), feature)
               .placed(placementModifiers);
        return PlacementUtils.register(registryName, placed);
    }
}
