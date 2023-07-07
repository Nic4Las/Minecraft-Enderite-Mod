package net.enderitemc.enderitemod.forge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.misterpemodder.shulkerboxtooltip.api.forge.ShulkerBoxTooltipPlugin;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.config.ClothConfig;
import net.enderitemc.enderitemod.forge.init.AnimationFeatures;
import net.enderitemc.enderitemod.forge.item.EnderiteShieldForge;
import net.enderitemc.enderitemod.forge.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.forge.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.misc.EnderiteElytraFeatureRender;
import net.enderitemc.enderitemod.modIntegrations.ShulkerBoxTooltipApiImplementation;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;

@Mod(EnderiteMod.MOD_ID)
public class EnderiteModForge {
        // Directly reference a log4j logger.
        private static final Logger LOGGER = LogManager.getLogger();

        // Enderite Elytra
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytraChestplate(EnderiteArmorMaterial.ENDERITE,
                                        ArmorItem.Type.CHESTPLATE,
                                        EnderiteMod.ENDERITE_ELYTRA_ITEM_SETTINGS));

        public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
                        "enderite_elytra_seperated",
                        () -> new EnderiteElytraSeperated(EnderiteMod.ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS));

        public static final RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY = EnderiteMod.BLOCK_ENTITY_TYPES
                        .register("enderite_shulker_box_block_entity",
                                        () -> BlockEntityType.Builder.create(EnderiteShulkerBoxBlockEntity::new,
                                                        EnderiteMod.ENDERITE_SHULKER_BOX.get())
                                                        .build(null));

        public static final RegistrySupplier<Item> ENDERITE_SHIELD_FORGE = EnderiteMod.ITEMS
                        .register("enderite_shield",
                                        () -> new EnderiteShieldForge(
                                                        EnderiteMod.ENDERITE_SHIELD_ITEM_SETTINGS));

        public EnderiteModForge() {
                // Submit our event bus to let architectury register our content on the right
                // time
                EventBuses.registerModEventBus(EnderiteMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
                EnderiteMod.init();



                EnderiteMod.ENDERITE_ELYTRA = ENDERITE_ELYTRA;
                EnderiteMod.ENDERITE_ELYTRA_SEPERATED = ENDERITE_ELYTRA_SEPERATED;
                EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY = ENDERITE_SHULKER_BOX_BLOCK_ENTITY;
                EnderiteMod.ENDERITE_SHIELD = ENDERITE_SHIELD_FORGE;

                FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        }

        private void setup(final FMLCommonSetupEvent event) {
                DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
                                new BlockPlacementDispenserBehavior());
                DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHEAR.get().asItem(),
                                new ShearsDispenserBehavior());
        }

        @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = EnderiteMod.MOD_ID)
        public static class RegistryEventsClient {

                @SubscribeEvent
                public static void loadCompleteEvent(FMLLoadCompleteEvent event) {
                        AnimationFeatures.init();
                }

                @SubscribeEvent
                public static void onClientSetup(FMLClientSetupEvent event) {
                        BlockEntityRendererRegistry.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
                                        EnderiteShulkerBoxBlockEntityRenderer::new);

                        if (ModList.get().isLoaded("cloth_config")) {
                                ModLoadingContext.get().registerExtensionPoint(
                                                ConfigScreenHandler.ConfigScreenFactory.class,
                                                () -> new ConfigScreenHandler.ConfigScreenFactory(
                                                                (mc, screen) -> new ClothConfig(screen).getScreen()));
                        }

                        if (ModList.get().isLoaded("shulkerboxtooltip")) {
                                ModLoadingContext.get().registerExtensionPoint(ShulkerBoxTooltipPlugin.class,
                                                () -> new ShulkerBoxTooltipPlugin(
                                                                ShulkerBoxTooltipApiImplementation::new));
                        }
                }

                @SubscribeEvent(priority = EventPriority.LOW)
                public static void renderPlayer(final EntityRenderersEvent.AddLayers event) {
                        // default
                        LivingEntityRenderer<PlayerEntity, PlayerEntityModel<PlayerEntity>> renderer = event
                                        .getSkin("default");
                        EnderiteElytraFeatureRender<PlayerEntity, PlayerEntityModel<PlayerEntity>> layer = new EnderiteElytraFeatureRender<>(
                                        renderer, event.getEntityModels());
                        if (renderer != null) {

                                renderer.addFeature(layer);
                        } else {
                                LOGGER.error("Couldn't get renderer");
                        }
                        // Slim
                        renderer = event.getSkin("slim");
                        layer = new EnderiteElytraFeatureRender<>(renderer, event.getEntityModels());
                        if (renderer != null) {

                                renderer.addFeature(layer);
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

                private static <T extends LivingEntity, M extends BipedEntityModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(
                                EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
                        R renderer = evt.getRenderer(entityType);

                        if (renderer != null) {
                                renderer.addFeature(new EnderiteElytraFeatureRender<>(renderer, evt.getEntityModels()));
                        }
                }
        }

}
