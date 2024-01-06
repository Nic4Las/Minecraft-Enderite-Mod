package net.enderitemc.enderitemod.materials;

import java.util.function.Supplier;

import net.enderitemc.enderitemod.*;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Lazy;
import net.minecraft.sound.SoundEvents;

public enum EnderiteArmorMaterial implements ArmorMaterial {

    ENDERITE("enderite", EnderiteMod.CONFIG.armor.durabilityMultiplier,
            new int[] { EnderiteMod.CONFIG.armor.bootsProtection, EnderiteMod.CONFIG.armor.leggingsProtection, 
                        EnderiteMod.CONFIG.armor.chestplateProtection, EnderiteMod.CONFIG.armor.helmetProtection },
            EnderiteMod.CONFIG.armor.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            EnderiteMod.CONFIG.armor.toughness, EnderiteMod.CONFIG.armor.knockbackResistance, () -> {
        return Ingredient.ofItems(EnderiteMod.ENDERITE_INGOT.get());
    });

    private static final int[] baseDurability = { 128, 144, 160, 112 };
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredient;

    EnderiteArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability,
            SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new Lazy<Ingredient>(repairIngredient);
    }

    public int getDurability(Type equipmentSlot_1) {
        return baseDurability[equipmentSlot_1.getEquipmentSlot().getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtection(Type equipmentSlot_1) {
        return this.armorValues[equipmentSlot_1.getEquipmentSlot().getEntitySlotId()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        // We needed to make it a Lazy type so we can actually get the Ingredient from
        // the Supplier.
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}