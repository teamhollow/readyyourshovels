package net.teamhollow.readyyourshovels;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSItems;
import net.teamhollow.readyyourshovels.world.gen.feature.RYSFeatures;
import net.teamhollow.readyyourshovels.world.gen.feature.RYSOreFeatureConfig;

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

        new RYSBlocks();
        new RYSItems();

        this.initWorldGen();

        log("Initialized");
    }

    private void initWorldGen() {
        for (Biome biome : Registry.BIOME) {
            if ((biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) && biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.DESERT && biome.getCategory() != Biome.Category.BEACH && biome.getCategory() != Biome.Category.EXTREME_HILLS) {
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSFeatures.DIRT_SURFACE_FEATURE);

                biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSFeatures.ORE.configure(new RYSOreFeatureConfig(RYSOreFeatureConfig.Target.TOUGH_DIRT, RYSBlocks.CLAY_DEPOSIT.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(18, 0, 0, 128))));
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSFeatures.ORE.configure(new RYSOreFeatureConfig(RYSOreFeatureConfig.Target.TOUGH_DIRT, RYSBlocks.PEAT_DEPOSIT.getDefaultState(), 12)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(20, 0, 0, 128))));
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSFeatures.ORE.configure(new RYSOreFeatureConfig(RYSOreFeatureConfig.Target.TOUGH_DIRT, RYSBlocks.IRON_DEPOSIT.getDefaultState(), 9)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(20, 0, 0, 64))));
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, RYSFeatures.ORE.configure(new RYSOreFeatureConfig(RYSOreFeatureConfig.Target.TOUGH_DIRT, RYSBlocks.GOLD_DEPOSIT.getDefaultState(), 6)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(2, 0, 0, 32))));
            }
        }
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
