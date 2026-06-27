package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderiteBowPullProgressProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<EnderiteBowPullProgressProperty> CODEC = MapCodec.unit(new EnderiteBowPullProgressProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
        if (context == null || context.asLivingEntity() == null) {
            return 0.0F;
        } else {
            return EnderiteBow.getPowerForTime(UseDuration.useDuration(stack, context.asLivingEntity()));
        }
    }

    @Override
    public MapCodec<EnderiteBowPullProgressProperty> type() {
        return CODEC;
    }
}