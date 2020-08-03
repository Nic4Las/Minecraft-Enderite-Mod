package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum EnderiteMaterial implements IItemTier {

    ENDERITE(4, 4096, 15.0F, 2.0F, 17, () -> Ingredient.fromItems(Registration.ENDERITE_INGOT.get()));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Ingredient repairIngredient;

    EnderiteMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability,
                     Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient.get();
    }

    @Override
    public int getMaxUses() {
        return this.itemDurability;
    }

    @Override
    public float getEfficiency() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairIngredient;
    }

}
