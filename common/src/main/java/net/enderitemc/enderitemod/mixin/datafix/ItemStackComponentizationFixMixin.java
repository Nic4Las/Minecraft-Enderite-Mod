package net.enderitemc.enderitemod.mixin.datafix;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Dynamic;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.datafixer.fix.ItemStackComponentizationFix;
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
    private static Supplier<Set<String>> chargable_items = Suppliers.memoize(() -> Set.of(EnderiteTools.ENDERITE_SWORD.getIdAsString(), EnderiteTools.ENDERITE_SHIELD.getIdAsString()));

    @Shadow
    protected static <T> Dynamic<T> fixBlockEntityData(ItemStackComponentizationFix.StackData data, Dynamic<T> dynamic, String blockEntityId) {
        return null;
    }

    @Inject(at= @At("TAIL"), method="fixBlockEntityData(Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix$StackData;Lcom/mojang/serialization/Dynamic;Ljava/lang/String;)Lcom/mojang/serialization/Dynamic;", cancellable = true)
    private static <T> void fixData(ItemStackComponentizationFix.StackData data, Dynamic<T> dynamic, String blockEntityId, CallbackInfoReturnable<Dynamic<T>> cir) {
        if(blockEntityId.equals("minecraft:")
                && data.itemEquals(EnderiteTools.ENDERITE_SHIELD.getIdAsString())) {
            // Fix missing entity id for enderite shield
            cir.setReturnValue(fixBlockEntityData(data, dynamic, "minecraft:banner"));
        }
        if((blockEntityId.equals("minecraft:") || blockEntityId.equals(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getIdAsString()))
                && data.itemEquals(EnderiteMod.ENDERITE_SHULKER_BOX_ITEM.getIdAsString())) {
            // Fix missing entity id for enderite shulker box and write
            cir.setReturnValue(fixBlockEntityData(data, dynamic, "minecraft:shulker_box"));
        }
    }

    @ModifyArg(method = "Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix;fixStack(Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix$StackData;Lcom/mojang/serialization/Dynamic;)V",
            at = @At(value = "INVOKE",
            target = "Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix$StackData;setComponent(Ljava/lang/String;Lcom/mojang/serialization/Dynamic;)V"))
    private static Dynamic<?> fixTrims(Dynamic<?> dynamic) {
        // Replace material: enderitemod:enderite_darker with material: enderitemod:enderite
        if(dynamic.get("material").result().isPresent()) {
            if (dynamic.get("material").result().get().equals(dynamic.createString("enderitemod:enderite_darker"))) {
                dynamic = dynamic.set("material", dynamic.createString("enderitemod:enderite"));
            }
        }
        return dynamic;
    }

    @Inject(at= @At("HEAD"), method="Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix;fixStack(Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix$StackData;Lcom/mojang/serialization/Dynamic;)V")
    private static <T> void fixData(ItemStackComponentizationFix.StackData data, Dynamic<T> dynamic, CallbackInfo info) {
        if(data.itemMatches(chargable_items.get())) {
            // Replace teleport_charge with Data Component enderitemod:teleport_charge
            data.moveToComponent("teleport_charge", "enderitemod:teleport_charge");
        }
    }


}
