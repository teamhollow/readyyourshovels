package net.teamhollow.readyyourshovels.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSItemTags {
    public static final Tag<Item> ANT_TEMPTERS = register("ant_tempters");

    public RYSItemTags() {}

    private static Tag<Item> register(String id) {
        return TagRegistry.item(new Identifier(ReadyYourShovels.MOD_ID, id));
    }
}
