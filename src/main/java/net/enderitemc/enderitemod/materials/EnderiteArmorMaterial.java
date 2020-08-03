package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum EnderiteArmorMaterial implements IArmorMaterial {
    ENDERITE("enderite", 8, new int[] { 4, 7, 9, 4 }, 17,SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 4.0F, 0.1F, () -> {
        return Ingredient.fromItems(Registration.ENDERITE_INGOT.get());
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

    public int getDurability(EquipmentSlotType equipmentSlot_1) {
        return baseDurability[equipmentSlot_1.getIndex()] * this.durabilityMultiplier;
    }

    public int getDamageReductionAmount(EquipmentSlotType equipmentSlot_1) {
        return this.armorValues[equipmentSlot_1.getIndex()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getSoundEvent() {
        return this.equipSound;
    }

    public Ingredient getRepairMaterial() {
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

    public float func_230304_f_() {
        return this.knockbackResistance;
    }
}
