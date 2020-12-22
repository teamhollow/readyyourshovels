package net.teamhollow.readyyourshovels.tag;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSItemTags {
    public static final ITag.INamedTag<Item> ANT_TEMPTERS = register("ant_tempters");

    public RYSItemTags() {
    }

    private static ITag.INamedTag<Item> register(String id) {
        return ItemTags.createOptional(new ResourceLocation(ReadyYourShovels.MOD_ID, id));
    }
}
