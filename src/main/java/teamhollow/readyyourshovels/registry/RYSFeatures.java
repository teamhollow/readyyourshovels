package teamhollow.readyyourshovels.registry;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.feature.DirtSurfaceFeature;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovels.MODID)
public class RYSFeatures {
    public static final Feature<NoFeatureConfig> DIRT_SURFACE = new DirtSurfaceFeature(NoFeatureConfig.field_236558_a_);

    public static final ConfiguredFeature<?, ?> DIRT_SURFACE_CONFIGURATE_FEATURE = DIRT_SURFACE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static final RuleTest TOUGH_DIRT_RULE = new BlockMatchRuleTest(RYSBlocks.TOUGH_DIRT);

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> registry) {
        registry.getRegistry().register(DIRT_SURFACE.setRegistryName("dirt_surface"));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerConfiguredFeature(String name, ConfiguredFeature<FC, ?> feature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, name, feature);
    }
}
