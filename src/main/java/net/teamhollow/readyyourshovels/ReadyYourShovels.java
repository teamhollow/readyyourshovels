package net.teamhollow.readyyourshovels;

import java.util.function.Predicate;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.teamhollow.readyyourshovels.init.*;

@SuppressWarnings("deprecation")
public class ReadyYourShovels implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "readyyourshovels";
    public static final String MOD_NAME = "Ready Your Shovels";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(Items.WOODEN_SHOVEL)
    );

    @Override
    public void onInitialize() {
        log("Initializing");

        new RYSSoundEvents();
        new RYSPointOfInterests();

        new RYSBlocks();
        new RYSBlockEntities();
        new RYSItems();
        new RYSEntities();

        new RYSFeatures();
        new RYSConfiguredFeatures();
        new RYSStructureFeatures();
        addBiomeModifications();

        log("Initialized");
    }

    private static void addBiomeModifications() {
        // add dirt caves
        Predicate<BiomeSelectionContext> DIRT_CAVE_FEATURE_BIOME_SELECTOR = BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.Category.OCEAN, Biome.Category.DESERT, Biome.Category.BEACH, Biome.Category.EXTREME_HILLS, Biome.Category.RIVER).negate());

        // add to underground decoration step
        BiomeModifications.addFeature(DIRT_CAVE_FEATURE_BIOME_SELECTOR, GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_FEATURE.getId(RYSConfiguredFeatures.DIRT_CAVE)));

        // add generic features
        BiomeModifications.addFeature(DIRT_CAVE_FEATURE_BIOME_SELECTOR, GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_FEATURE.getId(RYSConfiguredFeatures.DAYROOT)));

        // add to underground ores step
        for (ConfiguredFeature<?, ?> feature : new ConfiguredFeature[]{ RYSConfiguredFeatures.TOUGH_DIRT_MOSSY_COBBLESTONE, RYSConfiguredFeatures.DEPOSIT_CLAY, RYSConfiguredFeatures.DEPOSIT_PEAT, RYSConfiguredFeatures.DEPOSIT_IRON, RYSConfiguredFeatures.DEPOSIT_GOLD }) {
            BiomeModifications.addFeature(DIRT_CAVE_FEATURE_BIOME_SELECTOR, GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_FEATURE.getId(feature)));
        }

        // add structures
        BiomeModifications.addStructure(DIRT_CAVE_FEATURE_BIOME_SELECTOR, RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(RYSConfiguredFeatures.ANT_HILL)));
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static Identifier texture(String path) {
        return new Identifier(MOD_ID, "textures/" + path + ".png");
    }
}
