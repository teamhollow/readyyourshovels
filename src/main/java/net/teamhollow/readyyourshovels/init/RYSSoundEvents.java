package net.teamhollow.readyyourshovels.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSSoundEvents {
    public static final SoundEvent ENTITY_GARDEN_ANT_AMBIENT = register("entity.garden_ant.ambient");

    private static SoundEvent register(String id) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);
        return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
    }
}
