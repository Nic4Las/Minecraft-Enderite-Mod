package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum EnderiteArmorMaterial implements ArmorMaterial {
    ENDERITE("enderite", 8, new int[] { 4, 7, 9, 4 }, 17,SoundEvents.ARMOR_EQUIP_CHAIN, 4.0F, 0.1F, () -> {
        return Ingredient.of(Registration.ENDERITE_INGOT.get());
    });

    private static final int[] baseDurability = { 128, 144, 160, 112 };
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    EnderiteArmorMaterial(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability,
                          SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient.get();
    }

    public int getDurabilityForSlot(EquipmentSlot equipmentSlot_1) {
        return baseDurability[equipmentSlot_1.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot equipmentSlot_1) {
        return this.armorValues[equipmentSlot_1.getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        // We needed to make it a Lazy type so we can actually get the Ingredient from
        // the Supplier.
        return this.repairIngredient;
    }

    @OnlyIn(Dist.CLIENT)
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
