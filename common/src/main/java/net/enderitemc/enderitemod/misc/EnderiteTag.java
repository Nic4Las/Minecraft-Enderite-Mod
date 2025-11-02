package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnderiteTag {
    public static final TagKey<Item> ENDERITE_ITEM = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "enderite_items"));
    public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "shulker_boxes"));
    public static final TagKey<Item> ENDERITE_ELYTRA = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "enderite_elytras"));
    public static final TagKey<Item> ENDERITE_ARMOR = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "enderite_armor"));
    public static final TagKey<Item> ENDERITE_TOOLS = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "enderite_tools"));
    public static final TagKey<Item> REPAIRS_ENDERITE_EQUIPMENT = TagKey.of(RegistryKeys.ITEM,
        Identifier.of(EnderiteMod.MOD_ID, "repairs_enderite_armor"));

    public static final TagKey<Block> INCORRECT_FOR_ENDERITE_TOOL = TagKey.of(RegistryKeys.BLOCK,
        Identifier.of(EnderiteMod.MOD_ID, "incorrect_for_enderite_tool"));
    public static final TagKey<Block> NEEDS_ENDERITE_TOOL = TagKey.of(RegistryKeys.BLOCK,
        Identifier.of(EnderiteMod.MOD_ID, "needs_enderite_tool"));
    public static final TagKey<Block> IS_VOID_FLOATING_ENCHANTABLE = TagKey.of(RegistryKeys.BLOCK,
        Identifier.of(EnderiteMod.MOD_ID, "is_void_floating_enchantable"));

    public static final TagKey<EntityType<?>> UNAFFECTED_BY_ENDERITE_SHIELD = TagKey.of(RegistryKeys.ENTITY_TYPE,
        Identifier.of(EnderiteMod.MOD_ID, "unaffected_by_enderite_shield"));

}