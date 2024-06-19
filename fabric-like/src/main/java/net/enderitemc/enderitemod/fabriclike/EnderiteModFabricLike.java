package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.materials.EnderiteArmorMaterial;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

public class EnderiteModFabricLike {

        // Enderite Elytra
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA = EnderiteMod.ITEMS.register("enderite_elytra",
                        () -> new EnderiteElytraChestplate(EnderiteMod.ENDERITE_ARMOR_MATERIAL,
                                        ArmorItem.Type.CHESTPLATE,
                                        EnderiteMod.ENDERITE_ELYTRA_ITEM_SETTINGS));
        public static final RegistrySupplier<Item> ENDERITE_ELYTRA_SEPERATED = EnderiteMod.ITEMS.register(
                        "enderite_elytra_seperated",
                        () -> new EnderiteElytraSeperated(
                                        EnderiteMod.ENDERITE_ELYTRA_SEPERATED_ITEM_SETTINGS));

        public static void init() {
                EnderiteMod.ENDERITE_ELYTRA = ENDERITE_ELYTRA;
                EnderiteMod.ENDERITE_ELYTRA_SEPERATED = ENDERITE_ELYTRA_SEPERATED;
                
                EnderiteMod.init();
                EnderiteShears.registerLoottables_Fabric();

                // ItemGroup
                // ItemGroupEvents.modifyEntriesEvent(EnderiteMod.ENDERITE_TAB.getKey()).register(content -> {
                //         content.add(ENDERITE_ELYTRA.get());			
                //         content.add(ENDERITE_ELYTRA_SEPERATED.get());
                // });

                DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
                                new BlockPlacementDispenserBehavior());
                DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get().asItem(),
                                new ShearsDispenserBehavior());

        }
}
