package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import java.util.EnumMap;

public class EnderiteArmorMaterial {
    static ResourceKey<? extends Registry<EquipmentAsset>> EQUIPMENT_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> ID = register("enderite");

    public static final ResourceKey<EquipmentAsset> ENDERITE_ELYTRA_ARMOR_MODEL_ID = register("enderite_elytra");
    public static final ResourceKey<EquipmentAsset> ENDERITE_ELYTRA_SEPERATED_ARMOR_MODEL_ID = register("enderite_elytra_seperated");

    public static final ArmorMaterial ENDERITE = genEnderiteArmorMaterial(ID);
    public static final ArmorMaterial ENDERITE_ELYTRA = genEnderiteArmorMaterial(ENDERITE_ELYTRA_ARMOR_MODEL_ID);

    public static ArmorMaterial genEnderiteArmorMaterial(ResourceKey<EquipmentAsset> id) {
        return new ArmorMaterial(
            EnderiteMod.CONFIG.armor.durabilityMultiplier,
            Util.make(new EnumMap<>(ArmorType.class), map -> {
                map.put(ArmorType.BOOTS, EnderiteMod.CONFIG.armor.bootsProtection);
                map.put(ArmorType.LEGGINGS, EnderiteMod.CONFIG.armor.leggingsProtection);
                map.put(ArmorType.CHESTPLATE, EnderiteMod.CONFIG.armor.chestplateProtection);
                map.put(ArmorType.HELMET, EnderiteMod.CONFIG.armor.helmetProtection);
                map.put(ArmorType.BODY, EnderiteMod.CONFIG.armor.bodyProtection);
            }),
            EnderiteMod.CONFIG.armor.enchantability,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            EnderiteMod.CONFIG.armor.toughness,
            EnderiteMod.CONFIG.armor.knockbackResistance,
            EnderiteTag.REPAIRS_ENDERITE_EQUIPMENT,
            id);
    }

    static ResourceKey<EquipmentAsset> register(String name) {
        return ResourceKey.create(EQUIPMENT_REGISTRY_KEY, Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, name));
    }
}