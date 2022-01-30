package net.enderitemc.enderitemod;

import static net.enderitemc.enderitemod.EnderiteMod.MOD_ID;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.events.GenerationEvent;
import net.enderitemc.enderitemod.init.AnimationFeatures;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.renderer.EnderiteElytraRenderer;
import net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.renderer.EnderiteShulkerBoxTileEntityRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class EnderiteMod {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "enderitemod";

    public EnderiteMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EnderiteModConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnderiteModConfig.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        EnderiteModConfig.loadConfig(EnderiteModConfig.CLIENT_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve("enderitemod-client.toml"));
        EnderiteModConfig.loadConfig(EnderiteModConfig.COMMON_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve("enderitemod-common.toml"));
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        Registration.init();

        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        GenerationEvent.registerOres();
        DispenserBlock.registerBehavior(Registration.ENDERITE_SHULKER_BOX.get().asItem(), new ShulkerBoxDispenseBehavior());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}",
                event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEventsClient {

        @SubscribeEvent
        public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
            // WorldFeatures.init();
            AnimationFeatures.init();
        }

        @SubscribeEvent
        public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
            LOGGER.info("RSL" + event.getMap().location() + ", " + TextureAtlas.LOCATION_BLOCKS);
            if (event.getMap().location() == TextureAtlas.LOCATION_BLOCKS) {
                event.addSprite(EnderiteShieldRenderer.LOCATION_ENDERITE_SHIELD_BASE_NO_PATTERN);
            }
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BlockEntityRenderers.register(Registration.ENDERITE_SHULKER_BOX_TILE_ENTITY.get(),
            EnderiteShulkerBoxTileEntityRenderer::new);
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void renderPlayer(final EntityRenderersEvent.AddLayers event) {
            //default
            LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin("default");
            EnderiteElytraRenderer<Player, PlayerModel<Player>> layer = new EnderiteElytraRenderer<>(renderer, event.getEntityModels());
            if (renderer != null) {
                
                renderer.addLayer(layer);
            } else {
                LOGGER.error("Couldn't get renderer");
            }
            //Slim
            renderer = event.getSkin("slim");
            layer = new EnderiteElytraRenderer<>(renderer, event.getEntityModels());
            if (renderer != null) {
                
                renderer.addLayer(layer);
            } else {
                LOGGER.error("Couldn't get renderer");
            }
            
            addEntityLayer(event, EntityType.ARMOR_STAND);
            addEntityLayer(event, EntityType.ZOMBIE);
            addEntityLayer(event, EntityType.ZOMBIE_VILLAGER);
            addEntityLayer(event, EntityType.SKELETON);
            addEntityLayer(event, EntityType.HUSK);
            addEntityLayer(event, EntityType.STRAY);
            addEntityLayer(event, EntityType.WITHER_SKELETON);
            addEntityLayer(event, EntityType.DROWNED);
            addEntityLayer(event, EntityType.PIGLIN);
            addEntityLayer(event, EntityType.PIGLIN_BRUTE);
            addEntityLayer(event, EntityType.ZOMBIFIED_PIGLIN);
        }

        private static <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
            R renderer = evt.getRenderer(entityType);

            if (renderer != null) {
                renderer.addLayer(new EnderiteElytraRenderer<>(renderer, evt.getEntityModels()));
            }
        }

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
            // WorldFeatures.init();
        }

    }
}
