package net.teamhollow.readyyourshovels.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

@SuppressWarnings("unused")
public class RYSBlockTags {
    public static final Tag<Block> DIRT_LIKE = register(new Identifier("dirt_like"));

    public static final Tag<Block> ANT_RESOURCES = register("ant_resources");
    public static final Tag<Block> PLANTER_BOXES = register("planter_boxes");
    public static final Tag<Block> TOUGH_DIRT_REPLACEABLE = register("tough_dirt_replaceable");
    public static final Tag<Block> ORE_TOUGH_DIRT_REPLACEABLE = register("ore_tough_dirt_replaceable");

    public RYSBlockTags() {}

    private static Tag<Block> register(String id) {
        return TagRegistry.block(new Identifier(ReadyYourShovels.MOD_ID, id));
    }
    private static Tag<Block> register(Identifier identifier) {
        return TagRegistry.block(identifier);
    }
}
