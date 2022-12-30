package net.enderitemc.enderitemod.tools;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;

public class AxeSubclass extends AxeItem {
    public AxeSubclass(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
}