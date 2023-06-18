package net.enderitemc.enderitemod.misc;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnderiteTag {
    public static final TagKey<Item> ENDERITE_ITEM = TagKey.of(RegistryKeys.ITEM,
            new Identifier("enderitemod", "enderite_items"));
    public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.of(RegistryKeys.ITEM,
            new Identifier("enderitemod", "shulker_boxes"));
    public static final TagKey<Item> ENDERITE_ELYTRA = TagKey.of(RegistryKeys.ITEM,
            new Identifier("enderitemod", "enderite_elytras"));
}