package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.item.ToolMaterial;

public class EnderiteMaterial {

    public static final ToolMaterial ENDERITE = new ToolMaterial(
        EnderiteTag.INCORRECT_FOR_ENDERITE_TOOL,
        EnderiteMod.CONFIG.tools.durability,
        15.0F,
        2.0F,
        EnderiteMod.CONFIG.armor.enchantability,
        EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT
    );
}