package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public abstract class RYSFeatures extends Feature<RYSOreFeatureConfig> {
    public static final Feature<RYSOreFeatureConfig> ORE = register("ore", new RYSOreFeature(RYSOreFeatureConfig.CODEC));

    public static final Feature<DefaultFeatureConfig> DIRT_CAVE = new DirtCaveFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<DefaultFeatureConfig, Feature<DefaultFeatureConfig>> DIRT_CAVE_FEATURE = new ConfiguredFeature<>(DIRT_CAVE, DefaultFeatureConfig.DEFAULT);

    public RYSFeatures(Codec<RYSOreFeatureConfig> codec) {
        super(codec);
    }

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String id, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), feature);
    }
}
