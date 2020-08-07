package net.enderitemc.enderitemod.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class EnderiteOreFeature extends OreFeature {
    public EnderiteOreFeature(Codec<OreFeatureConfig> oreFeatureConfigCodec) {
        super(oreFeatureConfigCodec);
    }
}
