package net.enderitemc.enderitemod.mixin.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V1460;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(V1460.class)
public abstract class RegisterEnderiteBlockEntityMixin {

    @Inject(at = @At("TAIL"), method = "registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void enderitemod$addToMap(Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> cir, Map<String, Supplier<TypeTemplate>> map) {
        enderitemod$targetItems(schema, map, EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getRegisteredName());
    }

    @Unique
    private static void enderitemod$targetItems(Schema schema, Map<String, Supplier<TypeTemplate>> map, String blockEntityId) {
        // Register enderite shulker box block entity for data fixers
        schema.register(map, blockEntityId, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
    }
}
