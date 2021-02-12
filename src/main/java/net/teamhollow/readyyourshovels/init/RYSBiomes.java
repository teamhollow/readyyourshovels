package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

import java.util.function.Predicate;

public class RYSBiomes {
    public static final RegistryKey<Biome> FOREST_MOUND = register("forest_mound", RYSBiomes.createForestMound());

    private static Biome createForestMound() {
        // spawn settingss
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        spawnSettings.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(EntityType.BAT, 10, 8, 8))
                     .spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(RYSEntities.PEATY_SLIME, 10, 4, 6));

        // generation settings
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder().surfaceBuilder(RYSConfiguredSurfaceBuilders.TOUGH_DIRT);

        DefaultBiomeFeatures.addForestTrees(generationSettings);

        DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
        generationSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL)
                          .carver(GenerationStep.Carver.AIR, RYSConfiguredCarvers.DIRT_CAVE)
                          .carver(GenerationStep.Carver.AIR, RYSConfiguredCarvers.DIRT_CANYON);
        DefaultBiomeFeatures.addDefaultLakes(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);

        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        DefaultBiomeFeatures.addDefaultDisks(generationSettings);
        DefaultBiomeFeatures.addForestGrass(generationSettings);
        DefaultBiomeFeatures.addDefaultFlowers(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.FOREST_FLOWER_VEGETATION_COMMON)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_BROWN_MUSHROOM)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_RED_MUSHROOM);

        DefaultBiomeFeatures.addDefaultMushrooms(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);

        DefaultBiomeFeatures.addSprings(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.DIRT_CAVE_DAYROOT)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.DIRT_CAVE_TOUGHROOT)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.PATCH_TOUGHROOT_STEM)
                          .feature(GenerationStep.Feature.VEGETAL_DECORATION, RYSConfiguredFeatures.PATCH_CAVE_CARROT)
                          .feature(GenerationStep.Feature.UNDERGROUND_ORES, RYSConfiguredFeatures.TOUGH_DIRT_PATCH_COBBLESTONE)
                          .feature(GenerationStep.Feature.UNDERGROUND_ORES, RYSConfiguredFeatures.TOUGH_DIRT_DEPOSIT_CLAY)

                          .feature(GenerationStep.Feature.UNDERGROUND_ORES, RYSConfiguredFeatures.TOUGH_DIRT_DEPOSIT_GOLD)
                          .feature(GenerationStep.Feature.UNDERGROUND_ORES, RYSConfiguredFeatures.TOUGH_DIRT_DEPOSIT_IRON)
                          .feature(GenerationStep.Feature.UNDERGROUND_ORES, RYSConfiguredFeatures.TOUGH_DIRT_DEPOSIT_PEAT);

        return new Biome.Builder()
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.FOREST)
            .depth(0.3F).scale(0.2F).temperature(0.4F).downfall(0.8F)
            .effects(
                new BiomeEffects.Builder()
                    .waterColor(0x127ee3).waterFogColor(0x10107d)
                    .fogColor(0xa6c8ff)
                    .skyColor(RYSBiomes.getSkyColor(0.8F))
                    .moodSound(BiomeMoodSound.CAVE)
                    .build()
            )
            .spawnSettings(spawnSettings.build())
            .generationSettings(generationSettings.build())
            .build();
    }

    @SuppressWarnings("deprecation")
    public RYSBiomes() {
        // add biomes to spawn
        OverworldBiomes.addContinentalBiome(FOREST_MOUND, OverworldClimate.TEMPERATE, 1.0D);

        // modify biomes
        Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.OCEAN, Biome.Category.DESERT, Biome.Category.BEACH, Biome.Category.EXTREME_HILLS, Biome.Category.RIVER, Biome.Category.MESA).negate());
        BiomeModifications.addStructure(biomeSelector, RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(RYSConfiguredFeatures.ANT_HILL)));
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
