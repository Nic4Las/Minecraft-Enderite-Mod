package net.enderitemc.enderitemod.fabriclike;

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
import net.minecraft.util.Rarity;

public class EnderiteModFabricLike {

        // Enderite Elytra
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytraChestplate(EnderiteArmorMaterial.ENDERITE,
                                        EquipmentSlot.CHEST,
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof()
                                                        .rarity(Rarity.EPIC))));
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
                        "enderite_elytra_seperated",
                        () -> new EnderiteElytraSeperated(
                                        (new Item.Settings().group(EnderiteMod.ENDERITE_TAB).fireproof().maxCount(1)
                                                        .maxDamage(1024))
                                                        .rarity(Rarity.EPIC)));

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

                DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
                                new BlockPlacementDispenserBehavior());

        }
}
