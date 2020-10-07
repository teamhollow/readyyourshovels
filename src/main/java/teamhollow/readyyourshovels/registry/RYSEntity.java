package teamhollow.readyyourshovels.registry;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.entity.GardenAntEntity;

@Mod.EventBusSubscriber(modid = ReadyYourShovels.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RYSEntity {
    public static final EntityType<GardenAntEntity> GARDEN_ANT = EntityType.Builder.create(GardenAntEntity::new, EntityClassification.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(8).size(0.5F, 0.5F).build(prefix("garden_ant"));


    @SubscribeEvent
    public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
        GlobalEntityTypeAttributes.put(GARDEN_ANT, GardenAntEntity.registerAttributes().create());

        event.getRegistry().register(GARDEN_ANT.setRegistryName("garden_ant"));
        EntitySpawnPlacementRegistry.register(GARDEN_ANT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
    }

    private static String prefix(String path) {
        return ReadyYourShovels.MODID + "." + path;
    }
}
