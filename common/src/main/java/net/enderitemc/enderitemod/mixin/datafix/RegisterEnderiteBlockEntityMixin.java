package net.enderitemc.enderitemod.mixin.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.schema.Schema1460;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(Schema1460.class)
public abstract class RegisterEnderiteBlockEntityMixin {

    @Inject(at = @At("TAIL"), method = "registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void addToMap(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir, Map<String, Supplier<TypeTemplate>> map) {
        targetItems(schema, map, EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getIdAsString());
    }

    @Unique
    private static void targetItems(Schema schema, Map<String, Supplier<TypeTemplate>> map, String blockEntityId) {
        // Register enderite shulker box block entity for data fixers
        schema.register(map, blockEntityId, () -> DSL.optionalFields("Items", DSL.list(TypeReferences.ITEM_STACK.in(schema))));
    }
}
