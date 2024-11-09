package net.enderitemc.enderitemod.fabriclike;

import dev.architectury.registry.registries.RegistrySupplier;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.Item;

import static net.enderitemc.enderitemod.tools.EnderiteTools.ENDERITE_SHIELD_ITEM_SETTINGS;

public class EnderiteModFabricLike {

    public static final RegistrySupplier<Item> ENDERITE_SHIELD = EnderiteMod.ITEMS.register("enderite_shield",
        () -> new EnderiteShield(
            EnderiteMod.getItemSettings("enderite_shield", ENDERITE_SHIELD_ITEM_SETTINGS)));

    public static void init() {
        EnderiteTools.ENDERITE_SHIELD = ENDERITE_SHIELD;

        EnderiteMod.init();
        EnderiteShears.registerLoottables_Fabric();

        DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
            new BlockPlacementDispenserBehavior());
        DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get().asItem(),
            new ShearsDispenserBehavior());

    }
}
