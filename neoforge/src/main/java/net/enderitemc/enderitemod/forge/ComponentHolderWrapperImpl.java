package net.enderitemc.enderitemod.forge;

import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;

public class ComponentHolderWrapperImpl {
    public static Boolean contains(ItemStack itemStack, ComponentType<?> componentType) {
        return itemStack.has(componentType);
    }
}
