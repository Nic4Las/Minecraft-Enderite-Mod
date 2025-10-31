package net.enderitemc.enderitemod;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;

public class ComponentHolderWrapper {
    @ExpectPlatform
    public static Boolean contains(ItemStack itemStack, ComponentType<?> componentType) {
        throw new AssertionError();
    }
}
