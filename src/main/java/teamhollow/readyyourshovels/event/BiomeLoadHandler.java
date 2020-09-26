package teamhollow.readyyourshovels.event;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.registry.RYSBlocks;
import teamhollow.readyyourshovels.registry.RYSFeatures;

@Mod.EventBusSubscriber(modid = ReadyYourShovels.MODID)
public class BiomeLoadHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void loadingBiome(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();

        if (event.getName().getNamespace().toString().contains("minecraft") || event.getName().getNamespace().toString().contains("biomesoplenty")) {
            if (event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND && event.getCategory() != Biome.Category.RIVER && event.getCategory() != Biome.Category.OCEAN && event.getCategory() != Biome.Category.DESERT && event.getCategory() != Biome.Category.EXTREME_HILLS) {
                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, RYSFeatures.DIRT_SURFACE_CONFIGURATE_FEATURE);

                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(RYSFeatures.TOUGH_DIRT_RULE, RYSBlocks.CLAY_DEPOSIT.getDefaultState(), 9)).func_242733_d(128).func_242728_a().func_242731_b(18));
                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(RYSFeatures.TOUGH_DIRT_RULE, RYSBlocks.PEAT_DEPOSIT.getDefaultState(), 12)).func_242733_d(128).func_242728_a().func_242731_b(20));
                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(RYSFeatures.TOUGH_DIRT_RULE, RYSBlocks.IRON_DEPOSIT.getDefaultState(), 9)).func_242733_d(64).func_242728_a().func_242731_b(20));
                generation.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(new OreFeatureConfig(RYSFeatures.TOUGH_DIRT_RULE, RYSBlocks.GOLD_DEPOSIT.getDefaultState(), 6)).func_242733_d(32).func_242728_a().func_242731_b(2));
            }
        }
    }
}