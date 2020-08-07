package net.enderitemc.enderitemod.world.biome;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.world.gen.EnderiteOreHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;

public class EndBiomeFeatures {

    public static void addEnderiteOre(Biome biome) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new EnderiteOreHelper(null, Registration.ENDERITE_ORE.get().getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(30,0,12,48))));
    }
}
