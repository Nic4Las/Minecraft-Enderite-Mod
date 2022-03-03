package net.enderitemc.enderitemod.misc;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnderiteTag {
    public static final TagKey<Item> ENDERITE_ITEM = TagKey.of(Registry.ITEM_KEY, new Identifier("enderitemod", "enderite_items"));
    public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.of(Registry.ITEM_KEY, new Identifier("enderitemod", "shulker_boxes"));
    public static final TagKey<Item> ENDERITE_ELYTRA = TagKey.of(Registry.ITEM_KEY, new Identifier("enderitemod", "enderite_elytras"));
}