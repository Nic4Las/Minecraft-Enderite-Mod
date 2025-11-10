package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HeldItemContext;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderiteBowPullProgressProperty() implements NumericProperty {
    public static final MapCodec<EnderiteBowPullProgressProperty> CODEC = MapCodec.unit(new EnderiteBowPullProgressProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable HeldItemContext context, int seed) {
        if (context == null || context.getEntity() == null) {
            return 0.0F;
        } else {
            return EnderiteBow.getPullProgress(UseDurationProperty.getTicksUsedSoFar(stack, context.getEntity()));
        }
    }

    @Override
    public MapCodec<EnderiteBowPullProgressProperty> getCodec() {
        return CODEC;
    }
}