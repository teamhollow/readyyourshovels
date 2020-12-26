package net.teamhollow.readyyourshovels.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.feature.DirtCaveDayrootFeature;
import net.teamhollow.readyyourshovels.world.gen.feature.DirtCaveFeature;
import net.teamhollow.readyyourshovels.world.gen.feature.DirtCaveMossyBoulderFeature;

public class RYSFeatures {
    public static final Feature<DefaultFeatureConfig> DIRT_CAVE = register("dirt_cave", new DirtCaveFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> DIRT_CAVE_DAYROOT = register("dirt_cave_dayroot", new DirtCaveDayrootFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<SingleStateFeatureConfig> DIRT_CAVE_MOSSY_BOULDER = register("dirt_cave_mossy_boulder", new DirtCaveMossyBoulderFeature(SingleStateFeatureConfig.CODEC));

    public RYSFeatures() {}

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), feature);
    }
}
