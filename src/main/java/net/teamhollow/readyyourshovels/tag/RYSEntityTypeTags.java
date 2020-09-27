package net.teamhollow.readyyourshovels.tag;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSEntityTypeTags {
    public static final Tag<EntityType<?>> ANT_NEST_INHABITORS = register("ant_nest_inhabitors");

    public RYSEntityTypeTags() {}

    private static Tag<EntityType<?>> register(String id) {
        return TagRegistry.entityType(new Identifier(ReadyYourShovels.MOD_ID, id));
    }
}
