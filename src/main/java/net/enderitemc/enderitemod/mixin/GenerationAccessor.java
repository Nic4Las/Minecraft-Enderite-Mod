package net.enderitemc.enderitemod.mixin;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;

/**
 * Allows the settings in a Biome's generation settings to be modified.
 */
@Mixin(BiomeGenerationSettings.class)
public interface GenerationAccessor {

    @Accessor("field_242484_f")
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();

    @Accessor("field_242484_f")
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);

}
