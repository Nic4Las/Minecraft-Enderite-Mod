package net.enderitemc.enderitemod.forge;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorRenderer;
import net.enderitemc.enderitemod.forge.modIntegrations.ClothConfigImplementation;
import net.enderitemc.enderitemod.forge.modIntegrations.ShulkerBoxTooltipImplementation;
import net.enderitemc.enderitemod.renderer.RendererRegistries;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.apache.logging.log4j.LogManager;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.Logger;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

@Mod(MOD_ID)
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
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get(),
                new BlockPlacementDispenserBehavior());
            DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get(),
                new ShearsDispenserBehavior());
        });
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID)
    public static class RegistryEventsClient {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EnderiteMod.clientInit();

            event.enqueueWork(() -> {
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

                // Special model types (No neoforge event)
//                SpecialModelTypes.ID_MAPPER.put(RendererRegistries.ENDERITE_SHIELD.id(), RendererRegistries.ENDERITE_SHIELD.codec());
            });
        }


        @SubscribeEvent
        public static void setupNumericProviders(RegisterRangeSelectItemModelPropertyEvent event) {
            // Numeric properties
            event.register(RendererRegistries.ENDERITE_CHARGE.id(), RendererRegistries.ENDERITE_CHARGE.codec());
            event.register(RendererRegistries.ENDERITE_CROSSBOW_PULL.id(), RendererRegistries.ENDERITE_CROSSBOW_PULL.codec());
            event.register(RendererRegistries.ENDERITE_BOW_PULL.id(), RendererRegistries.ENDERITE_BOW_PULL.codec());
        }


        @SubscribeEvent
        public static void setupConditionalProviders(RegisterConditionalItemModelPropertyEvent event) {
            // Boolean properties
            event.register(RendererRegistries.ENDERITE_PLAYER_SNEAKS.id(), RendererRegistries.ENDERITE_PLAYER_SNEAKS.codec());
        }

        @SubscribeEvent
        public static void registerItemModels(RegisterItemModelsEvent event) {
            SpecialModelTypes.ID_MAPPER.put(RendererRegistries.ENDERITE_SHIELD.id(), RendererRegistries.ENDERITE_SHIELD.codec());
        }

        @SubscribeEvent
        private static void onAddPackFinders(AddPackFindersEvent event) {
            // We only want to inject into the client-side resource packs (assets), not server data (datapacks)
            if (event.getPackType() == ResourceType.CLIENT_RESOURCES && FMLEnvironment.isProduction()) {

                // This helper method automatically looks in your mod's /resources/resourcepacks/ folder
                event.addPackFinders(
                        Identifier.of(MOD_ID, "resourcepacks/" + "alternative_textures_amber3562"),  // 1. The namespace and the folder name of your pack
                        ResourceType.CLIENT_RESOURCES,                                                    // 2. The type of pack (client resources)
                        Text.of("Alternative Enderitemod Textures (by Amber3562)"),                 // 3. The display name shown in the Resource Packs menu
                        ResourcePackSource.BUILTIN,                                                       // 4. Denotes that this is a built-in pack
                        false,                                                                            // 5. 'false' means optional/disabled by default
                        ResourcePackProfile.InsertionPosition.TOP                                         // 6. Where the pack goes in the load order if enabled
                );
            }
        }
    }
}
