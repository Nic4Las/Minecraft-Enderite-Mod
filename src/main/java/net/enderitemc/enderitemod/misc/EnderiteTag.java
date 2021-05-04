package net.enderitemc.enderitemod.misc;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class EnderiteTag {
        public static final ITag<Item> ENDERITE_ITEM = ItemTags.getAllTags()
                        .getTagOrEmpty(new ResourceLocation("enderitemod", "enderite_items"));
        public static final ITag<Item> CRAFTABLE_SHULKER_BOXES = ItemTags.getAllTags()
                        .getTagOrEmpty(new ResourceLocation("enderitemod", "shulker_boxes"));
}