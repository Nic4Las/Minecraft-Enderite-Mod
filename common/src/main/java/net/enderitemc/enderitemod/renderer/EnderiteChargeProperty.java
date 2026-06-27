package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record EnderiteChargeProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<EnderiteChargeProperty> CODEC = MapCodec.unit(new EnderiteChargeProperty());

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
        float charge = stack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();
        if (charge <= 0 || EnderiteMod.CONFIG.tools.maxTeleportCharge <= 0) {
            // No charge or charge disabled
            return -1;
        }
        // Percentage to full charge
        return charge / EnderiteMod.CONFIG.tools.maxTeleportCharge;
    }

    @Override
    public MapCodec<EnderiteChargeProperty> type() {
        return CODEC;
    }
}
