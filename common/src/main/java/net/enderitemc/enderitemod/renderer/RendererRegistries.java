package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

public class RendererRegistries {

    public static final IdCodecTuple<? extends SpecialModelRenderer.Unbaked> ENDERITE_SHIELD = IdCodecTuple.of("enderite_shield", EnderiteShieldRenderer.Unbaked.CODEC);

    public static final IdCodecTuple<? extends RangeSelectItemModelProperty> ENDERITE_CHARGE = IdCodecTuple.of("charge", EnderiteChargeProperty.CODEC);
    public static final IdCodecTuple<? extends RangeSelectItemModelProperty> ENDERITE_CROSSBOW_PULL = IdCodecTuple.of("crossbow/pull", EnderiteCrossbowPullProperty.CODEC);
    public static final IdCodecTuple<? extends RangeSelectItemModelProperty> ENDERITE_BOW_PULL = IdCodecTuple.of("bow/pull", EnderiteBowPullProgressProperty.CODEC);

    public static final IdCodecTuple<? extends ConditionalItemModelProperty> ENDERITE_PLAYER_SNEAKS = IdCodecTuple.of("is_sneaking", EnderitePlayerSneaksProperty.CODEC);

    public static void init() {
        // Special models
        SpecialModelRenderers.ID_MAPPER.put(ENDERITE_SHIELD.id, (MapCodec<? extends SpecialModelRenderer.Unbaked<?>>) ENDERITE_SHIELD.codec);

        // Numeric properties
        RangeSelectItemModelProperties.ID_MAPPER.put(ENDERITE_CHARGE.id, ENDERITE_CHARGE.codec);
        RangeSelectItemModelProperties.ID_MAPPER.put(ENDERITE_CROSSBOW_PULL.id, ENDERITE_CROSSBOW_PULL.codec);
        RangeSelectItemModelProperties.ID_MAPPER.put(ENDERITE_BOW_PULL.id, ENDERITE_BOW_PULL.codec);

        // Boolean properties
        ConditionalItemModelProperties.ID_MAPPER.put(ENDERITE_PLAYER_SNEAKS.id, ENDERITE_PLAYER_SNEAKS.codec);
    }

    public record IdCodecTuple<T>(Identifier id, MapCodec<T> codec) {
        public static <U> IdCodecTuple<U> of(String id, MapCodec<U> codec) {
            return new IdCodecTuple<>(Identifier.fromNamespaceAndPath(MOD_ID, id), codec);
        }
    }
}
