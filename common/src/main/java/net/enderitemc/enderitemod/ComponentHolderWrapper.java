package net.enderitemc.enderitemod;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

public class ComponentHolderWrapper {
    @ExpectPlatform
    public static Boolean contains(ItemStack itemStack, DataComponentType<?> componentType) {
        throw new AssertionError();
    }
}
