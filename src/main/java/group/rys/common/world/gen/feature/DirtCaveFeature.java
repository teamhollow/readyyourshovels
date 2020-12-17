package group.rys.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import group.rys.core.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;
import java.util.function.Function;

public class DirtCaveFeature extends Feature<NoFeatureConfig> {
	
	public DirtCaveFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				
				Biome biome = worldIn.getBiome(pos.add(x, 0, z));

                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER) && biome != Biomes.STONE_SHORE && biome != Biomes.SAVANNA_PLATEAU && biome != Biomes.WOODED_BADLANDS_PLATEAU && biome != Biomes.BADLANDS_PLATEAU && biome != Biomes.SHATTERED_SAVANNA_PLATEAU && biome != Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU && biome != Biomes.MODIFIED_BADLANDS_PLATEAU) {
					int surfaceY = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.add(x, 0, z)).getY();
					
					for (int y = surfaceY - 20; y < surfaceY; y++) {
						this.tryPlace(worldIn, pos.add(x, y, z));
					}
					
					this.tryPlaceLayer(worldIn, rand, pos.add(x, surfaceY - 20, z));
				}
				
			}
		}
		
		return true;
	}
	
	private void tryPlace(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		
		if (Block.isRock(state.getBlock())) {
			this.setBlockState(world, pos, ModBlocks.tough_dirt.getDefaultState());
		}
		if (state.getBlock() == Blocks.COAL_ORE) {
			this.setBlockState(world, pos, ModBlocks.peat_deposit.getDefaultState());
		}
		if (state.getBlock() == Blocks.IRON_ORE) {
			this.setBlockState(world, pos, ModBlocks.iron_deposit.getDefaultState());
		}
		if (state.getBlock() == Blocks.GOLD_ORE) {
			this.setBlockState(world, pos, ModBlocks.gold_deposit.getDefaultState());
		}
	}
	
	private void tryPlaceLayer(IWorld world, Random random, BlockPos pos) {
		for (int i = 0; i < 5; i++) {
			BlockPos pos_1 = pos.add(0, random.nextInt(3) - 1 - i, 0);
			BlockState state = world.getBlockState(pos_1);
			
			if (Block.isRock(state.getBlock())) {
				this.setBlockState(world, pos_1, ModBlocks.regolith.getDefaultState());
			}
		}
	}
	
}
