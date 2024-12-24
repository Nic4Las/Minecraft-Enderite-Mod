package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderitePlayerSneaksProperty() implements BooleanProperty {
    public static final MapCodec<EnderitePlayerSneaksProperty> CODEC = MapCodec.unit(new EnderitePlayerSneaksProperty());

    @Override
    public boolean getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ModelTransformationMode modelTransformationMode) {
        if (user instanceof ClientPlayerEntity clientPlayerEntity && clientPlayerEntity.isSneaking()) {
            return true;
        }

        return false;
    }

    @Override
    public MapCodec<EnderitePlayerSneaksProperty> getCodec() {
        return CODEC;
    }
}