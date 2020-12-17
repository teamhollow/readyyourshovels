package group.rys.core.registry;

import group.rys.core.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModSounds {
    public static final SoundEvent RECORD_COLLY = createEvent("music.record.colly");

    private static SoundEvent createEvent(String name) {

        SoundEvent sound = new SoundEvent(new ResourceLocation(Reference.MOD_ID, name));

        sound.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));

        return sound;
    }


    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
        evt.getRegistry().register(RECORD_COLLY);
    }

    private ModSounds() {

    }
}