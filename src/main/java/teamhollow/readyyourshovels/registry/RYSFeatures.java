package teamhollow.readyyourshovels.registry;

import net.minecraft.world.gen.feature.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.feature.DirtSurfaceFeature;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovels.MODID)
public class RYSFeatures {
    public static final Feature<NoFeatureConfig> DIRT_SURFACE = new DirtSurfaceFeature(NoFeatureConfig.field_236558_a_);

    public static final ConfiguredFeature<NoFeatureConfig, Feature<NoFeatureConfig>> DIRT_SURFACE_FEATURE = new ConfiguredFeature<>(DIRT_SURFACE, IFeatureConfig.NO_FEATURE_CONFIG);

    public static final OreFeatureConfig.FillerBlockType DIRT_FILLER = OreFeatureConfig.FillerBlockType.create("readyyourshovels:dirt_filler", "readyyourshovels:dirt_filler", (state) -> state.getBlock() == RYSBlocks.TOUGH_DIRT);

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> registry) {
        registry.getRegistry().register(DIRT_SURFACE.setRegistryName("clay_deposit"));
    }
}
