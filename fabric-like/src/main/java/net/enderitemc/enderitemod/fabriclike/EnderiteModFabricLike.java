package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteShears;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.world.level.block.DispenserBlock;

public class EnderiteModFabricLike {

    public static void init() {
        EnderiteMod.init();
        EnderiteShears.registerLoottables_Fabric();

        DispenserBlock.registerBehavior(EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem(),
            new ShulkerBoxDispenseBehavior());
        DispenserBlock.registerBehavior(EnderiteTools.ENDERITE_SHEAR.get().asItem(),
            new ShearsDispenseItemBehavior());

    }
}
