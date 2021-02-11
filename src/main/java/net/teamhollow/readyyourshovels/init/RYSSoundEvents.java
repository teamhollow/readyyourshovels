package net.teamhollow.readyyourshovels.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.DayrootBlock;
import net.teamhollow.readyyourshovels.entity.ant.AbstractAntEntity;

public class RYSSoundEvents {
    public static final SoundEvent ENTITY_ANT_AMBIENT = createAntSound("ambient");
    public static final SoundEvent ENTITY_ANT_HURT = createAntSound("hurt");
    public static final SoundEvent ENTITY_ANT_DEATH = createAntSound("death");
    private static SoundEvent createAntSound(String id) {
        return createEntitySound(AbstractAntEntity.id, id);
    }

    public static final SoundEvent MUSIC_DISC_COLLY = register("music_disc.colly");

    public static final SoundEvent BLOCK_TOUGH_DIRT_BREAK = createToughDirtSound("break");
    public static final SoundEvent BLOCK_TOUGH_DIRT_STEP = createToughDirtSound("step");
    public static final SoundEvent BLOCK_TOUGH_DIRT_PLACE = createToughDirtSound("place");
    public static final SoundEvent BLOCK_TOUGH_DIRT_HIT = createToughDirtSound("hit");
    public static final SoundEvent BLOCK_TOUGH_DIRT_FALL = createToughDirtSound("fall");
    private static SoundEvent createToughDirtSound(String id) {
        return createBlockSound("tough_dirt", id);
    }

    public static final SoundEvent BLOCK_REGOLITH_BREAK = createRegolithSound("break");
    public static final SoundEvent BLOCK_REGOLITH_STEP = createRegolithSound("step");
    public static final SoundEvent BLOCK_REGOLITH_PLACE = createRegolithSound("place");
    public static final SoundEvent BLOCK_REGOLITH_HIT = createRegolithSound("hit");
    public static final SoundEvent BLOCK_REGOLITH_FALL = createRegolithSound("fall");
    private static SoundEvent createRegolithSound(String id) {
        return createBlockSound("regolith", id);
    }

    public static final SoundEvent BLOCK_DAYROOT_AMBIENT = createBlockSound(DayrootBlock.id, "ambient");

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }

    private static SoundEvent createBlockSound(String block, String id) {
        return register("block." + block + "." + id);
    }
    private static SoundEvent createEntitySound(String entity, String id) {
        return register("entity." + entity + "." + id);
    }
}
