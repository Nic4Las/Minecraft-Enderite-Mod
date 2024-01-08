package net.enderitemc.enderitemod.materials;

import java.util.function.Supplier;

import net.enderitemc.enderitemod.*;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public enum EnderiteMaterial implements ToolMaterial {

    ENDERITE(4, EnderiteMod.CONFIG.tools.durability, 15.0F, 2.0F, EnderiteMod.CONFIG.armor.enchantability, () -> Ingredient.ofItems(EnderiteMod.ENDERITE_INGOT.get()));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    EnderiteMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability,
            Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy<>(repairIngredient);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}