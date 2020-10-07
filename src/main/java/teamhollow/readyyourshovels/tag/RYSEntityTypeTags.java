package teamhollow.readyyourshovels.tag;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSEntityTypeTags {
    public static final ITag.INamedTag<EntityType<?>> ANT_NEST_INHABITORS = register("ant_nest_inhabitors");

    public RYSEntityTypeTags() {
    }

    private static ITag.INamedTag<EntityType<?>> register(String id) {
        return EntityTypeTags.createOptional(new ResourceLocation(ReadyYourShovels.MODID, id));
    }
}
