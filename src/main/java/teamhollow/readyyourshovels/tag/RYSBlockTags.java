package teamhollow.readyyourshovels.tag;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSBlockTags {
    public static final ITag.INamedTag<Block> ANT_RESOURCES = register("ant_resources");
    public static final ITag.INamedTag<Block> PLANTER_BOXES = register("planter_boxes");

    public RYSBlockTags() {
    }

    private static ITag.INamedTag<Block> register(String id) {
        return BlockTags.createOptional(new ResourceLocation(ReadyYourShovels.MODID, id));
    }
}