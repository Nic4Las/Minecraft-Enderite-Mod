package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnderiteTag {
    public static final TagKey<Item> ENDERITE_ITEM = TagKey.of(RegistryKeys.ITEM,
            new Identifier(EnderiteMod.MOD_ID, "enderite_items"));
    public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.of(RegistryKeys.ITEM,
            new Identifier(EnderiteMod.MOD_ID, "shulker_boxes"));
    public static final TagKey<Item> ENDERITE_ELYTRA = TagKey.of(RegistryKeys.ITEM,
            new Identifier(EnderiteMod.MOD_ID, "enderite_elytras"));
    public static final TagKey<Item> ENDERITE_ARMOR = TagKey.of(RegistryKeys.ITEM,
            new Identifier(EnderiteMod.MOD_ID, "enderite_armor"));

    public static final TagKey<Block> INCORRECT_FOR_ENDERITE_TOOL = TagKey.of(RegistryKeys.BLOCK,
            new Identifier(EnderiteMod.MOD_ID, "incorrect_for_enderite_tool"));
}