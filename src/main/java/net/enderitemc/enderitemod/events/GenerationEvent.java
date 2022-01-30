package net.enderitemc.enderitemod.events;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
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
                .configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
                        Registration.ENDERITE_ORE.get().defaultBlockState(), 3))
                .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.absolute(bottomOffset), VerticalAnchor.absolute(maximum-topOffset)))))
                .squared() // spawn height
                .count(count))); // spawn frequency per chunks
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            for (ConfiguredFeature<?, ?> ore : enderOres) {
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
}
