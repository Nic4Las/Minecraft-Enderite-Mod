package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteDataComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderiteChargeProperty() implements NumericProperty {
    public static final MapCodec<EnderiteChargeProperty> CODEC = MapCodec.unit(new EnderiteChargeProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        float charge = stack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0);
        if (charge <= 0 || EnderiteMod.CONFIG.tools.maxTeleportCharge <= 0) {
            // No charge or charge disabled
            return -1;
        }
        // Percentage to full charge
        return charge / EnderiteMod.CONFIG.tools.maxTeleportCharge;
    }

    @Override
    public MapCodec<EnderiteChargeProperty> getCodec() {
        return CODEC;
    }
}
