package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSBlockTags {
    public static final Tag<Block> ANT_RESOURCES = register("ant_resources");
    public static final Tag<Block> PLANTER_BOXES = register("planter_boxes");

    public RYSBlockTags() {}

    private static Tag<Block> register(String id) {
        return TagRegistry.block(new Identifier(ReadyYourShovels.MOD_ID, id));
    }
}
