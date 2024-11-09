package net.enderitemc.enderitemod.materials;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class EnderiteArmorMaterial {

    public static final Identifier ID = Identifier.of(EnderiteMod.MOD_ID, "enderite");

    public static final Identifier ENDERITE_ARMOR_TRIM_ID = ID;
    public static final Identifier DARKER_ENDERITE_ARMOR_TRIM_ID = Identifier.of(EnderiteMod.MOD_ID, "enderite_darker");

    public static final Identifier ENDERITE_ELYTRA_ARMOR_MODEL_ID = Identifier.of(EnderiteMod.MOD_ID, "enderite_elytra");
    public static final Identifier ENDERITE_ELYTRA_SEPERATED_ARMOR_MODEL_ID = Identifier.of(EnderiteMod.MOD_ID, "enderite_elytra_seperated");

    public static final ArmorMaterial ENDERITE = genEnderiteArmorMaterial(ID);
    public static final ArmorMaterial ENDERITE_ELYTRA = genEnderiteArmorMaterial(ENDERITE_ELYTRA_ARMOR_MODEL_ID);

    public static ArmorMaterial genEnderiteArmorMaterial(Identifier id) {
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


    protected static int getDurability(int baseValue) {
        return baseValue * EnderiteMod.CONFIG.armor.durabilityMultiplier;
    }
}