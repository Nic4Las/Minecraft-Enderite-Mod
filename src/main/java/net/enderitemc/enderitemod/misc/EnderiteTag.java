package net.enderitemc.enderitemod.misc;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class EnderiteTag {
    public static final Tag<Item> ENDERITE_ITEM = TagRegistry.item(new Identifier("enderitemod", "enderite_items"));
}