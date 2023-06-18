package net.enderitemc.enderitemod.fabriclike;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;

public class EnderiteModFabricLike {

        // Enderite Elytra
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytraChestplate(EnderiteArmorMaterial.ENDERITE,
                                        EquipmentSlot.CHEST,
                                        EnderiteMod.ENDERITE_ELYTRA_ITEM_SETTINGS));
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
                        "enderite_elytra_seperated",
                        () -> new EnderiteElytraSeperated(
                                        EnderiteMod.ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS));

        public static final RegistrySupplier<BlockEntityType<EnderiteShulkerBoxBlockEntity>> ENDERITE_SHULKER_BOX_BLOCK_ENTITY = EnderiteMod.BLOCK_ENTITY_TYPES
                        .register("enderite_shulker_box_block_entity",
                                        () -> FabricBlockEntityTypeBuilder
                                                        .create(EnderiteShulkerBoxBlockEntity::new,
                                                                        EnderiteMod.ENDERITE_SHULKER_BOX.get())
                                                        .build(null));

        public static void init() {
                EnderiteMod.init();
                EnderiteMod.ENDERITE_ELYTRA = ENDERITE_ELYTRA;
                EnderiteMod.ENDERITE_ELYTRA_SEPERATED = ENDERITE_ELYTRA_SEPERATED;
                EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY = ENDERITE_SHULKER_BOX_BLOCK_ENTITY;

                // ItemGroup
		ItemGroupEvents.modifyEntriesEvent(EnderiteMod.ENDERITE_TAB.get()).register(content -> {
			content.add(ENDERITE_ELYTRA.get());			
                        content.add(ENDERITE_ELYTRA_SEPERATED.get());
		});

                DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
                                new BlockPlacementDispenserBehavior());

        }
}
