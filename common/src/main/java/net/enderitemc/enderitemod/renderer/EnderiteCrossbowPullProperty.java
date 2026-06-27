package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EnderiteCrossbowPullProperty implements RangeSelectItemModelProperty {
    public static final MapCodec<EnderiteCrossbowPullProperty> CODEC = MapCodec.unit(new EnderiteCrossbowPullProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
        if (context == null || context.asLivingEntity() == null) {
            return 0.0F;
        } else if (EnderiteCrossbow.isCharged(stack)) {
            return 0.0F;
        } else {
            LivingEntity holder = context.asLivingEntity();
            int i = EnderiteCrossbow.getChargeDuration(stack, holder);
            return (float) UseDuration.useDuration(stack, holder) / (float) i;
        }
    }

    @Override
    public MapCodec<EnderiteCrossbowPullProperty> type() {
        return CODEC;
    }
}
