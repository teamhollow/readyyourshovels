package group.rys.common.world.gen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.BiomeDictionary;

public class DepositsInRiversFeature extends Feature<SphereConfig> {
	
	private static BlockPos[] arrayOfPos = new BlockPos[] { new BlockPos(-1, -1, -1), new BlockPos(-1, -1, 0), new BlockPos(-1, -1, 1), new BlockPos(0, -1, -1), new BlockPos(0, -1, 0), new BlockPos(0, -1, 1), new BlockPos(1, -1, -1), new BlockPos(1, -1, 0), new BlockPos(1, -1, 1), new BlockPos(-1, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(1, 0, -1), new BlockPos(1, 0, 0), new BlockPos(1, 0, 1), new BlockPos(-1, 1, -1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, 1), new BlockPos(0, 1, -1), new BlockPos(0, 1, 0), new BlockPos(0, 1, 1), new BlockPos(1, 1, -1), new BlockPos(1, 1, 0), new BlockPos(1, 1, 1) };
	
	public DepositsInRiversFeature(Function<Dynamic<?>, ? extends SphereConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, SphereConfig config) {
		for (int x = 0; x < config.radius * 2; x++) {
			for (int z = 0; z < config.radius * 2; z++) {
				for (int y = 0; y < config.radius * 2; y++) {
					BlockPos pos_1 = pos.add(x - config.radius, y - config.radius, z - config.radius);
					
					Biome biome = worldIn.getBiome(pos_1);
					
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER) && rand.nextFloat() < config.integrity && pos.distanceSq(pos_1) < config.radius * config.radius && worldIn.getBlockState(pos_1).getBlock() == config.target.getBlock()) {
						this.trySetBlockState(worldIn, pos_1, config.state);
					}
				}
			}
		}
		
		return true;
	}
	
	private void trySetBlockState(IWorld world, BlockPos pos, BlockState state) {
		if (this.hasWaterAround(world, pos)) {
			this.setBlockState(world, pos, state);
		}
	}
	
	private boolean hasWaterAround(IWorld world, BlockPos pos) {
		for (BlockPos posInArray : arrayOfPos) {
			if (world.hasWater(pos.add(posInArray))) {
				return true;
			}
		}
		
		return false;
	}
	
}
