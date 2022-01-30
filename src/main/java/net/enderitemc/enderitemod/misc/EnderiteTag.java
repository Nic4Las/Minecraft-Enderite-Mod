package net.enderitemc.enderitemod.misc;

import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class EnderiteTag {
        public static final Tag<Item> ENDERITE_ITEM = ItemTags.getAllTags()
                        .getTagOrEmpty(new ResourceLocation("enderitemod", "enderite_items"));
        public static final Tag<Item> CRAFTABLE_SHULKER_BOXES = ItemTags.getAllTags()
                        .getTagOrEmpty(new ResourceLocation("enderitemod", "shulker_boxes"));
        public static final Tag<Item> ENDERITE_ELYTRAS = ItemTags.getAllTags()
                        .getTagOrEmpty(new ResourceLocation("enderitemod", "enderite_elytras"));
}