package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;

public class EnderiteModFabricLike {

    public static void init() {
        EnderiteMod.init();
        EnderiteShears.registerLoottables_Fabric();

        DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
            new BlockPlacementDispenserBehavior());
        DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get().asItem(),
            new ShearsDispenserBehavior());

    }
}
