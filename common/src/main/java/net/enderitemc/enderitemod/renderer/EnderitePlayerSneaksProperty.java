package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderitePlayerSneaksProperty() implements ConditionalItemModelProperty {
    public static final MapCodec<EnderitePlayerSneaksProperty> CODEC = MapCodec.unit(new EnderitePlayerSneaksProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user, int seed, ItemDisplayContext modelTransformationMode) {
        if (user instanceof LocalPlayer clientPlayerEntity && clientPlayerEntity.isShiftKeyDown()) {
            return true;
        }

        return false;
    }

    @Override
    public MapCodec<EnderitePlayerSneaksProperty> type() {
        return CODEC;
    }
}