package net.enderitemc.enderitemod.forge;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

public class ComponentHolderWrapperImpl {
    public static Boolean contains(ItemStack itemStack, DataComponentType<?> componentType) {
        return itemStack.has(componentType);
    }
}
