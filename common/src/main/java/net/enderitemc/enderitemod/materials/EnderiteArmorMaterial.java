package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class EnderiteArmorMaterial {
    static RegistryKey<? extends Registry<EquipmentAsset>> EQUIPMENT_REGISTRY_KEY = RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset"));

    public static final RegistryKey<EquipmentAsset> ID = register("enderite");

    public static final RegistryKey<EquipmentAsset> ENDERITE_ELYTRA_ARMOR_MODEL_ID = register("enderite_elytra");
    public static final RegistryKey<EquipmentAsset> ENDERITE_ELYTRA_SEPERATED_ARMOR_MODEL_ID = register("enderite_elytra_seperated");

    public static final ArmorMaterial ENDERITE = genEnderiteArmorMaterial(ID);
    public static final ArmorMaterial ENDERITE_ELYTRA = genEnderiteArmorMaterial(ENDERITE_ELYTRA_ARMOR_MODEL_ID);

    public static ArmorMaterial genEnderiteArmorMaterial(RegistryKey<EquipmentAsset> id) {
        return new ArmorMaterial(
            EnderiteMod.CONFIG.armor.durabilityMultiplier,
            Util.make(new EnumMap<>(EquipmentType.class), map -> {
                map.put(EquipmentType.BOOTS, EnderiteMod.CONFIG.armor.bootsProtection);
                map.put(EquipmentType.LEGGINGS, EnderiteMod.CONFIG.armor.leggingsProtection);
                map.put(EquipmentType.CHESTPLATE, EnderiteMod.CONFIG.armor.chestplateProtection);
                map.put(EquipmentType.HELMET, EnderiteMod.CONFIG.armor.helmetProtection);
                map.put(EquipmentType.BODY, EnderiteMod.CONFIG.armor.bodyProtection);
            }),
            EnderiteMod.CONFIG.armor.enchantability,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            EnderiteMod.CONFIG.armor.toughness,
            EnderiteMod.CONFIG.armor.knockbackResistance,
            EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT,
            id);
    }

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(EQUIPMENT_REGISTRY_KEY, Identifier.of(EnderiteMod.MOD_ID, name));
    }
}