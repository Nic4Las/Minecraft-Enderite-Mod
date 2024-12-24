package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EnderiteCrossbowPullProperty implements NumericProperty {
    public static final MapCodec<EnderiteCrossbowPullProperty> CODEC = MapCodec.unit(new EnderiteCrossbowPullProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (holder == null) {
            return 0.0F;
        } else if (EnderiteCrossbow.isCharged(stack)) {
            return 0.0F;
        } else {
            int i = EnderiteCrossbow.getPullTime(stack, holder);
            return (float) UseDurationProperty.getTicksUsedSoFar(stack, holder) / (float) i;
        }
    }

    @Override
    public MapCodec<EnderiteCrossbowPullProperty> getCodec() {
        return CODEC;
    }
}
