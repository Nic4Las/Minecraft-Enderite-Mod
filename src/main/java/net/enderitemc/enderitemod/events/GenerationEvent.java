package net.enderitemc.enderitemod.events;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
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
import net.minecraft.data.worldgen.features.FeatureUtils;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class GenerationEvent {

    // private static final ArrayList<ConfiguredFeature<?, ?>> enderOres = new
    // ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<Holder<PlacedFeature>> enderOres = new ArrayList<Holder<PlacedFeature>>();

    public static void registerOres() {
        int veinsize = EnderiteModConfig.ENDERITE_VEIN_SIZE.get() + 2;
        int count = EnderiteModConfig.ENDERITE_COUNT.get();
        int bottomOffset = EnderiteModConfig.ENDERITE_BOTTOM_OFFSET.get();
        int topOffset = EnderiteModConfig.ENDERITE_TOP_OFFSET.get();
        int maximum = EnderiteModConfig.ENDERITE_MAXIMUM.get();

        RuleTest END_STONE = new TagMatchTest(Tags.Blocks.END_STONES);
        OreConfiguration EnderiteOreConfig = new OreConfiguration(END_STONE,
                Registration.ENDERITE_ORE.get().defaultBlockState(), veinsize);
        Holder<ConfiguredFeature<OreConfiguration, ?>> configFeature = FeatureUtils.register("end_enderite_ore",
                Feature.ORE, EnderiteOreConfig);
        Holder<PlacedFeature> ORE_ENDERITE = PlacementUtils.register("end_enderite_ore", configFeature,
                CountPlacement.of(count), InSquarePlacement.spread(), HeightRangePlacement
                        .uniform(VerticalAnchor.absolute(bottomOffset), VerticalAnchor.absolute(maximum - topOffset)),
                BiomeFilter.biome());
        enderOres.add(ORE_ENDERITE);
        // Holder<PlacedFeature> ORE_ANCIENT_DEBRIS_SMALL =
        // PlacementUtils.register("ore_debris_small",
        // OreFeatures.ORE_ANCIENT_DEBRIS_SMALL, InSquarePlacement.spread(),
        // PlacementUtils.RANGE_8_8, BiomeFilter.biome());

        // .configured(new OreConfiguration(new TagMatchTest(Tags.Blocks.END_STONES),
        // Registration.ENDERITE_ORE.get().defaultBlockState(), veinsize));
        // enderOres.add(registerPlacedFeature("end_enderite_ore",
        // configFeature,
        // CountPlacement.of(count),
        // InSquarePlacement.spread(),
        // HeightRangePlacement.uniform(VerticalAnchor.absolute(bottomOffset),
        // VerticalAnchor.absolute(maximum-topOffset)), BiomeFilter.biome()));

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            for (Holder<PlacedFeature> ore : enderOres) {
                if (ore != null)
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
            }
        }
    }

    // private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?>
    // register(String name,
    // ConfiguredFeature<FC, ?> configureFeature) {
    // return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
    // new ResourceLocation(EnderiteMod.MOD_ID + ":" + name), configureFeature);
    // }

    // private static <C extends FeatureConfiguration, F extends Feature<C>>
    // PlacedFeature registerPlacedFeature(String registryName,
    // ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
    // PlacedFeature placed =
    // BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, new
    // ResourceLocation(EnderiteMod.MOD_ID + ":" + registryName), feature)
    // .placed(placementModifiers);
    // return PlacementUtils.register(registryName, placed);
    // }
}
