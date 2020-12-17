package group.rys.core.registry;

import group.rys.common.world.gen.feature.*;
import group.rys.common.world.gen.feature.structure.AnthillStructure;
import group.rys.common.world.gen.placement.CountChanceDepthConfig;
import group.rys.core.util.Reference;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModFeatures {
	
	public static final DirtCaveFeature dirt_cave = create("dirt_cave", new DirtCaveFeature(NoFeatureConfig::deserialize));
	public static final DirtGradientFeature dirt_gradient = create("dirt_gradient", new DirtGradientFeature(NoFeatureConfig::deserialize));
	public static final DepositsInRiversFeature deposits_in_rivers = create("deposits_in_rivers", new DepositsInRiversFeature(SphereConfig::deserialize));
	public static final DayrootFeature dayroot = create("dayroot", new DayrootFeature(BushConfig::deserialize));
	public static final Feature<ProbabilityConfig> ant_hill = create("ant_hill", new AntHillFeature(ProbabilityConfig::deserialize, false));
	public static final AppleTreeFeature appletree = create("appletree", new AppleTreeFeature(BushConfig::deserialize));
    public static final AnthillStructure anthill_structure = create("anthill_structure", new AnthillStructure(NoFeatureConfig::deserialize));


	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> registry = event.getRegistry();
		
		registry.register(dirt_cave);
		registry.register(dirt_gradient);
		registry.register(deposits_in_rivers);
		registry.register(dayroot);
		registry.register(ant_hill);

        registry.register(anthill_structure);
	}
	
	public static <T extends Feature<?>> T create(String name, T feature) {
		feature.setRegistryName(Reference.MOD_ID, name);
		return feature;
	}
	
	public static void registerFeatures() {
		OreFeatureConfig.FillerBlockType tough_dirt = OreFeatureConfig.FillerBlockType.create("TOUGH_DIRT", "tough_dirt", new BlockMatcher(ModBlocks.tough_dirt));
		
		ForgeRegistries.BIOMES.forEach((Biome biome) -> {
			
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
				// Dirt Cave
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(dirt_cave, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

				// Dirt Gradient
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(dirt_gradient, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
				
				// Cave Diversity
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, Blocks.DIRT.getDefaultState(), 16), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 40)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, Blocks.COARSE_DIRT.getDefaultState(), 16), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 40)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, Blocks.COBBLESTONE.getDefaultState(), 16), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 40)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, Blocks.GRAVEL.getDefaultState(), 16), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 40)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 16), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 40)));
				
				// Deposits In Caves
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, ModBlocks.clay_deposit.getDefaultState(), 8), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 20)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, ModBlocks.peat_deposit.getDefaultState(), 8), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 20)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, ModBlocks.iron_deposit.getDefaultState(), 8), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 20)));
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(tough_dirt, ModBlocks.gold_deposit.getDefaultState(), 8), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 20)));
				
				// Deposits In Rivers
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(deposits_in_rivers, new SphereConfig(Blocks.DIRT.getDefaultState(), ModBlocks.gold_deposit.getDefaultState(), 2, 1.0F), ModPlacements.count_chance_depth, new CountChanceDepthConfig(8, 1.0F, 20)));
				
				if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {

					// Dayroot
					biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(dayroot, new BushConfig(ModBlocks.dayroot.getDefaultState()), ModPlacements.count_chance_depth, new CountChanceDepthConfig(2, 0.5F, 40)));
                    if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
                        //Structure
                        biome.addStructure(anthill_structure, IFeatureConfig.NO_FEATURE_CONFIG);
                    }
                    // Cave Decoration
					biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.BROWN_MUSHROOM.getDefaultState()), ModPlacements.count_chance_depth, new CountChanceDepthConfig(2, 0.5F, 40)));
					biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(Feature.BUSH, new BushConfig(Blocks.RED_MUSHROOM.getDefaultState()), ModPlacements.count_chance_depth, new CountChanceDepthConfig(2, 0.5F, 40)));

					// Ant Hill
					biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(ant_hill, new ProbabilityConfig(100), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NO_PLACEMENT_CONFIG));
                    //Ant hill Structure
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(anthill_structure, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));

                    //AppleTree
					Biomes.FOREST.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apple_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(14)));
					Biomes.FLOWER_FOREST.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apple_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(12)));

					Biomes.BIRCH_FOREST.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apricot_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(12)));
					Biomes.BIRCH_FOREST_HILLS.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apricot_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(12)));
					Biomes.TALL_BIRCH_FOREST.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apricot_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(14)));
					Biomes.TALL_BIRCH_HILLS.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.apricot_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(14)));

					Biomes.DARK_FOREST.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.orange_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(12)));
					Biomes.DARK_FOREST_HILLS.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Biome.createDecoratedFeature(appletree, new BushConfig(ModBlocks.orange_fruit_tree.getDefaultState()), Placement.CHANCE_HEIGHTMAP_DOUBLE, new ChanceConfig(12)));

				}
			}
			
		});
	}
	
}
