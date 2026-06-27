package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class EnderiteTag {
    public static final TagKey<Item> ENDERITE_ITEM = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderite_items"));
    public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "shulker_boxes"));
    public static final TagKey<Item> ENDERITE_ELYTRA = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderite_elytras"));
    public static final TagKey<Item> ENDERITE_ARMOR = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderite_armor"));
    public static final TagKey<Item> ENDERITE_TOOLS = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "enderite_tools"));
    public static final TagKey<Item> REPAIRS_ENDERITE_EQUIPMENT = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "repairs_enderite_armor"));

    public static final TagKey<Block> INCORRECT_FOR_ENDERITE_TOOL = TagKey.create(Registries.BLOCK,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "incorrect_for_enderite_tool"));
    public static final TagKey<Block> NEEDS_ENDERITE_TOOL = TagKey.create(Registries.BLOCK,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "needs_enderite_tool"));
    public static final TagKey<Block> IS_VOID_FLOATING_ENCHANTABLE = TagKey.create(Registries.BLOCK,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "is_void_floating_enchantable"));

    public static final TagKey<EntityType<?>> UNAFFECTED_BY_ENDERITE_SHIELD = TagKey.create(Registries.ENTITY_TYPE,
        Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "unaffected_by_enderite_shield"));

}