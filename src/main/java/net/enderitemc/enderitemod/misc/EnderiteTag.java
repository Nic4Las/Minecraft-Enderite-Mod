package net.enderitemc.enderitemod.misc;

import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class EnderiteTag {
        public static final TagKey<Item> ENDERITE_ITEM = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("enderitemod", "enderite_items"));
        public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("enderitemod", "shulker_boxes"));
        public static final TagKey<Item> ENDERITE_ELYTRAS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("enderitemod", "enderite_elytras"));
        //                 .getTagOrEmpty(new ResourceLocation("enderitemod", "enderite_items"));
        // public static final TagKey<Item> CRAFTABLE_SHULKER_BOXES = ItemTags.getAllTags()
        //                 .getTagOrEmpty(new ResourceLocation("enderitemod", "shulker_boxes"));
        // public static final TagKey<Item> ENDERITE_ELYTRAS = ItemTags.getAllTags()
        //                 .getTagOrEmpty(new ResourceLocation("enderitemod", "enderite_elytras"));
}