package net.enderitemc.enderitemod.materials;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import net.enderitemc.enderitemod.*;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorItem.Type;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

public class EnderiteArmorMaterial {

    public static final Identifier ID = new Identifier(EnderiteMod.MOD_ID, "enderite");

    public static final Identifier ENDERITE_ARMOR_TRIM_ID = ID;
    public static final Identifier DARKER_ENDERITE_ARMOR_TRIM_ID = new Identifier(EnderiteMod.MOD_ID, "enderite_darker");

    public static final ArmorMaterial ENDERITE = new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS,  EnderiteMod.CONFIG.armor.bootsProtection);
                map.put(ArmorItem.Type.LEGGINGS, EnderiteMod.CONFIG.armor.leggingsProtection);
                map.put(ArmorItem.Type.CHESTPLATE, EnderiteMod.CONFIG.armor.chestplateProtection);
                map.put(ArmorItem.Type.HELMET, EnderiteMod.CONFIG.armor.helmetProtection);
                map.put(ArmorItem.Type.BODY, EnderiteMod.CONFIG.armor.bodyProtection);
            }),
            EnderiteMod.CONFIG.armor.enchantability,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(EnderiteMod.ENDERITE_INGOT.get()),
            List.of(new ArmorMaterial.Layer(ID)),
            EnderiteMod.CONFIG.armor.toughness,
            EnderiteMod.CONFIG.armor.knockbackResistance);



    protected static int getDurability(int baseValue) {
        return baseValue * EnderiteMod.CONFIG.armor.durabilityMultiplier;
    }
}