package group.rys.common.world.gen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import group.rys.core.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class DirtGradientFeature extends Feature<NoFeatureConfig> {
	
	private static BlockPos[] arrayOfPos = new BlockPos[] { new BlockPos(-1, -1, -1), new BlockPos(-1, -1, 0), new BlockPos(-1, -1, 1), new BlockPos(0, -1, -1), new BlockPos(0, -1, 0), new BlockPos(0, -1, 1), new BlockPos(1, -1, -1), new BlockPos(1, -1, 0), new BlockPos(1, -1, 1), new BlockPos(-1, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(1, 0, -1), new BlockPos(1, 0, 0), new BlockPos(1, 0, 1), new BlockPos(-1, 1, -1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, 1), new BlockPos(0, 1, -1), new BlockPos(0, 1, 0), new BlockPos(0, 1, 1), new BlockPos(1, 1, -1), new BlockPos(1, 1, 0), new BlockPos(1, 1, 1) };
	
	public DirtGradientFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int surfaceY = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.add(x, 0, z)).getY();
				
				for (int y = 0; y < surfaceY; y++) {
					BlockPos pos_1 = pos.add(x, surfaceY, z).down(y);
					BlockState state = worldIn.getBlockState(pos_1);
					
					if (state.getBlock() == Blocks.GRASS_BLOCK) {
						this.tryPlace(worldIn, pos_1);
					}
				}
			}
		}
		
		return true;
	}
	
	private void tryPlace(IWorld world, BlockPos pos) {
		for (int y = 0; y < pos.getY(); y++) {
			BlockPos pos_1 = pos.down(y);
			
			if (Block.isRock(world.getBlockState(pos_1).getBlock())) {
				this.trySetBlockState(world, pos_1, ModBlocks.tough_dirt.getDefaultState());
				
				if (Block.isRock(world.getBlockState(pos_1.down()).getBlock())) {
					this.trySetBlockState(world, pos_1.down(), ModBlocks.regolith.getDefaultState());
				}
				
				return;
			}
			
			if (world.isAirBlock(pos_1)) {
				return;
			}
		}
	}
	
	private void trySetBlockState(IWorld world, BlockPos pos, BlockState state) {
		for (BlockPos posInArray : arrayOfPos) {
			if (world.isAirBlock(pos.add(posInArray))) {
				this.setBlockState(world, pos, state);
			}
		}
	}
	
}
