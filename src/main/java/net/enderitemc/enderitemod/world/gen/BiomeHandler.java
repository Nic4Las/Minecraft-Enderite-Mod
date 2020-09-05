package net.enderitemc.enderitemod.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.enderitemc.enderitemod.mixin.GenerationAccessor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public final class BiomeHandler {

    private final GenerationAccessor genAcc;

    @SuppressWarnings("ConstantConditions")
    public BiomeHandler(Biome biome) {
        this.genAcc = (GenerationAccessor) biome.func_242440_e();
    }

    public void addFeature(GenerationStage.Decoration step, ConfiguredFeature<?, ?> feature) {

        int stepIndex = step.ordinal();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> featuresByStep = new ArrayList<>(this.genAcc.getFeatures());

        while (featuresByStep.size() <= stepIndex) {
            featuresByStep.add(Lists.newArrayList());
        }

        List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>(featuresByStep.get(stepIndex));
        features.add(() -> feature);
        featuresByStep.set(stepIndex, features);

        this.genAcc.setFeatures(featuresByStep);

    }

}
