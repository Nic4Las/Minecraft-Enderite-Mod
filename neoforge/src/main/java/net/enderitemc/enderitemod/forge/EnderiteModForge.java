package net.enderitemc.enderitemod.forge;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorRenderer;
import net.enderitemc.enderitemod.forge.init.AnimationFeatures;
import net.enderitemc.enderitemod.forge.modIntegrations.ClothConfigImplementation;
import net.enderitemc.enderitemod.forge.modIntegrations.ShulkerBoxTooltipImplementation;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EnderiteMod.MOD_ID)
public class EnderiteModForge {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public EnderiteModForge(IEventBus modBus) {
        // Submit our event bus to let architectury register our content on the right
        // time
        //EventBuses.registerModEventBus(EnderiteMod.MOD_ID, modBus);

        EnderiteMod.init();

        modBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
            new BlockPlacementDispenserBehavior());
        DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get().asItem(),
            new ShearsDispenserBehavior());
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = EnderiteMod.MOD_ID)
    public static class RegistryEventsClient {

        @SubscribeEvent
        public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
            event.enqueueWork(AnimationFeatures::init);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRendererRegistry.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
                EnderiteShulkerBoxBlockEntityRenderer::new);
            BlockEntityRendererRegistry.register(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(),
                EnderiteRespawnAnchorRenderer::new);

            if (ModList.get().isLoaded("cloth_config")) {
                ClothConfigImplementation.registerEntryPoint(event);
            }

            if (ModList.get().isLoaded("shulkerboxtooltip")) {
                ShulkerBoxTooltipImplementation.registerEntryPoint(event);
            }
        }
    }
}
