package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSBiomes {
    public static final RegistryKey<Biome> PLAINS_MOUND = register("plains_mound", createPlainsMound());

    private static Biome createPlainsMound() {
        // spawn settingss
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        spawnSettings.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(RYSEntities.PEATY_SLIME, 10, 4, 6));

        // generation settings
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder().surfaceBuilder(RYSSurfaceBuilders.TOUGH_DIRT_SURFACE_BUILDER);

        DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
        generationSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addDefaultLakes(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);

        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        DefaultBiomeFeatures.addDefaultDisks(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.PATCH_TOUGHROOT_STEM);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);

        DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);

        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSConfiguredFeatures.DIRT_CAVE);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.DIRT_CAVE_DAYROOT);
        for (ConfiguredFeature<?, ?> feature : new ConfiguredFeature[]{ RYSConfiguredFeatures.TOUGH_DIRT_PATCH_COBBLESTONE, RYSConfiguredFeatures.TOUGH_DIRT_DEPOSIT_CLAY}) {
            generationSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, feature);
        }

        return new Biome.Builder()
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.PLAINS)
            .depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F)
            .effects(
                new BiomeEffects.Builder()
                    .waterColor(4159204).waterFogColor(329011)
                    .fogColor(12638463)
                    .skyColor(getSkyColor(0.8F))
                    .moodSound(BiomeMoodSound.CAVE)
                    .build()
            )
            .spawnSettings(spawnSettings.build())
            .generationSettings(generationSettings.build())
            .build();
    }

    public RYSBiomes() {
        // add biomes to spawn
        OverworldBiomes.addContinentalBiome(PLAINS_MOUND, OverworldClimate.TEMPERATE, 1.0D);

        // modify biomes
        BiomeModifications.addStructure(BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.OCEAN, Biome.Category.DESERT, Biome.Category.BEACH, Biome.Category.EXTREME_HILLS, Biome.Category.RIVER, Biome.Category.MESA).negate()), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(RYSConfiguredFeatures.ANT_HILL)));
    }

    private static RegistryKey<Biome> register(String id, Biome biome) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);
        BuiltinRegistries.add(BuiltinRegistries.BIOME, identifier, biome);

        return getKey(identifier);
    }

    private static RegistryKey<Biome> getKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }
}
