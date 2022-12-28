package net.enderitemc.enderitemod.modIntegrations;

import java.util.function.BiConsumer;

import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlock;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxScreenHandler;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.QuickShulkerData;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public class QuickShulkerImplementation implements RegisterQuickShulker {
        private static BiConsumer<PlayerEntity, ItemStack> ENDERITE_SHULKER_BOX_CONSUMER;

        @Override
        public void registerProviders() {
                QuickOpenableRegistry.register(EnderiteShulkerBoxBlock.class,
                                new QuickShulkerData(ENDERITE_SHULKER_BOX_CONSUMER, false));
        }

        static {
                ENDERITE_SHULKER_BOX_CONSUMER = (PlayerEntity player, ItemStack stack) -> {
                        ItemStackInventory inventory = new ItemStackInventory(stack, 45);

                        ScreenHandlerFactory screenHandlerFactory = (int i, PlayerInventory playerInventory,
                                        PlayerEntity playerEntity) -> new EnderiteShulkerBoxScreenHandler(
                                                        i,
                                                        playerInventory,
                                                        inventory);
                        Text text = stack.hasCustomName() ? stack.getName()
                                        : Text.translatable("container.enderitemod.enderiteShulkerBox");

                        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(screenHandlerFactory, text));
                };
        }
}
