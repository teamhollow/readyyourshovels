package net.teamhollow.readyyourshovels.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

@Mod.EventBusSubscriber(modid = ReadyYourShovels.MOD_ID)
public class RYSSoundEvents {
    public static final SoundEvent ENTITY_GARDEN_ANT_AMBIENT = createEvent("entity.garden_ant.ambient");

    private static SoundEvent createEvent(String name) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(ReadyYourShovels.MOD_ID, name));

        sound.setRegistryName(new ResourceLocation(ReadyYourShovels.MOD_ID, name));
        return sound;
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        evt.getRegistry().register(ENTITY_GARDEN_ANT_AMBIENT);
    }

}
