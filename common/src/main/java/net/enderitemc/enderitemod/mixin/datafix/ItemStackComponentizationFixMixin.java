package net.enderitemc.enderitemod.mixin.datafix;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Dynamic;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(ItemStackComponentizationFix.class)
public abstract class ItemStackComponentizationFixMixin {
    private static Supplier<Set<String>> chargable_items = Suppliers.memoize(() -> Set.of(EnderiteTools.ENDERITE_SWORD.getRegisteredName(), EnderiteTools.ENDERITE_SHIELD.getRegisteredName()));

    @Shadow
    protected static <T> Dynamic<T> fixBlockEntityTag(ItemStackComponentizationFix.ItemStackData data, Dynamic<T> dynamic, String blockEntityId) {
        return null;
    }

    @Inject(at = @At("TAIL"), method = "fixBlockEntityTag(Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;Lcom/mojang/serialization/Dynamic;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;", cancellable = true)
    private static <T> void enderitemod$fixData(ItemStackComponentizationFix.ItemStackData data, Dynamic<T> dynamic, String blockEntityId, CallbackInfoReturnable<Dynamic<T>> cir) {
        if (blockEntityId.equals("minecraft:")
            && data.is(EnderiteTools.ENDERITE_SHIELD.getRegisteredName())) {
            // Fix missing entity id for enderite shield
            cir.setReturnValue(fixBlockEntityTag(data, dynamic, "minecraft:banner"));
        }
        if ((blockEntityId.equals("minecraft:") || blockEntityId.equals(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getRegisteredName()))
            && data.is(EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.getRegisteredName())) {
            // Fix missing entity id for enderite shulker box and write
            cir.setReturnValue(fixBlockEntityTag(data, dynamic, "minecraft:shulker_box"));
        }
    }

    @ModifyArg(method = "fixItemStack(Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;Lcom/mojang/serialization/Dynamic;)V",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;setComponent(Ljava/lang/String;Lcom/mojang/serialization/Dynamic;)V"))
    private static Dynamic<?> fixTrims(Dynamic<?> dynamic) {
        // Replace material: enderitemod:enderite_darker with material: enderitemod:enderite
        if (dynamic.get("material").result().isPresent()) {
            if (dynamic.get("material").result().get().equals(dynamic.createString("enderitemod:enderite_darker"))) {
                dynamic = dynamic.set("material", dynamic.createString("enderitemod:enderite"));
            }
        }
        return dynamic;
    }

    @Inject(at = @At("HEAD"), method = "fixItemStack(Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;Lcom/mojang/serialization/Dynamic;)V")
    private static <T> void enderitemod$fixData(ItemStackComponentizationFix.ItemStackData data, Dynamic<T> dynamic, CallbackInfo info) {
        if (data.is(chargable_items.get())) {
            // Replace teleport_charge with Data Component enderitemod:teleport_charge
            data.moveTagToComponent("teleport_charge", "enderitemod:teleport_charge");
        }
    }


}
