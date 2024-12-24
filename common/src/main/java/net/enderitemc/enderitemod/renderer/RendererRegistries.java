package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.util.Identifier;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

public class RendererRegistries {

    public static final IdCodecTuple<? extends SpecialModelRenderer.Unbaked> ENDERITE_SHIELD = IdCodecTuple.of("enderite_shield", EnderiteShieldRenderer.Unbaked.CODEC);

    public static final IdCodecTuple<? extends NumericProperty> ENDERITE_CHARGE = IdCodecTuple.of("charge", EnderiteChargeProperty.CODEC);
    public static final IdCodecTuple<? extends NumericProperty> ENDERITE_CROSSBOW_PULL = IdCodecTuple.of("crossbow/pull", EnderiteCrossbowPullProperty.CODEC);
    public static final IdCodecTuple<? extends NumericProperty> ENDERITE_BOW_PULL = IdCodecTuple.of("bow/pull", EnderiteBowPullProgressProperty.CODEC);

    public static final IdCodecTuple<? extends BooleanProperty> ENDERITE_PLAYER_SNEAKS = IdCodecTuple.of("is_sneaking", EnderitePlayerSneaksProperty.CODEC);

    public static void init() {
        // Special models
        SpecialModelTypes.ID_MAPPER.put(ENDERITE_SHIELD.id, ENDERITE_SHIELD.codec);

        // Numeric properties
        NumericProperties.ID_MAPPER.put(ENDERITE_CHARGE.id, ENDERITE_CHARGE.codec);
        NumericProperties.ID_MAPPER.put(ENDERITE_CROSSBOW_PULL.id, ENDERITE_CROSSBOW_PULL.codec);
        NumericProperties.ID_MAPPER.put(ENDERITE_BOW_PULL.id, ENDERITE_BOW_PULL.codec);

        // Boolean properties
        BooleanProperties.ID_MAPPER.put(ENDERITE_PLAYER_SNEAKS.id, ENDERITE_PLAYER_SNEAKS.codec);
    }

    public record IdCodecTuple<T>(Identifier id, MapCodec<T> codec) {
        public static <U> IdCodecTuple<U> of(String id, MapCodec<U> codec) {
            return new IdCodecTuple<>(Identifier.of(MOD_ID, id), codec);
        }
    }
}
