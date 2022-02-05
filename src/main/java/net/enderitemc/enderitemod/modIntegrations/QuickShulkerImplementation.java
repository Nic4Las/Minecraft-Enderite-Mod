package net.enderitemc.enderitemod.modIntegrations;

import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;

public class QuickShulkerImplementation implements RegisterQuickShulker {
        @Override
        public void registerProviders() {
                QuickOpenableRegistry.register(EnderiteShulkerBoxBlock.class,false,
                                ((player, stack) -> player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i,
                                                playerInventory, playerEntity) -> new EnderiteShulkerBoxScreenHandler(i,
                                                                player.getInventory(), new ItemStackInventory(stack, 45)),
                                                stack.hasCustomName() ? stack.getName()
                                                                : new TranslatableText(
                                                                                "container.enderitemod.enderiteShulkerBox")))
                                                ));
        }
}
