package net.enderitemc.enderitemod.mixin;

import dev.architectury.impl.RegistrySupplierImpl;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import java.util.Map;

@Mixin(ArmorTrim.class)
public class ArmorTrimFixMixin {

    @Inject(at = @At("TAIL"), method = "getMaterialAssetNameFor(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;)Ljava/lang/String;", cancellable = true)
    private static void getMaterialAssetNameFor(RegistryEntry<ArmorTrimMaterial> material, RegistryEntry<ArmorMaterial> armorMaterial, CallbackInfoReturnable<String> ci) {
        if(armorMaterial instanceof RegistrySupplierImpl<ArmorMaterial> supplier) {
            RegistryEntry<ArmorMaterial> holder = supplier.getHolder();
            Map<RegistryEntry<ArmorMaterial>, String> map = material.value().overrideArmorMaterials();
            String name = map.get(holder);
            if (name != null) {
                ci.setReturnValue(name);
            }
        }
    }
}