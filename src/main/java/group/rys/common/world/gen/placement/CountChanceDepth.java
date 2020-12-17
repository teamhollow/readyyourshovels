package group.rys.common.world.gen.placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.Placement;

public class CountChanceDepth extends Placement<CountChanceDepthConfig> {
	
	public CountChanceDepth(Function<Dynamic<?>, ? extends CountChanceDepthConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn, Random random, CountChanceDepthConfig configIn, BlockPos pos) {
		return IntStream.range(0, configIn.count).filter((p_215043_2_) -> {
			return random.nextFloat() < configIn.chance;
		}).mapToObj((p_215042_3_) -> {
			int i = random.nextInt(16);
			int j = random.nextInt(16);
			int k = worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.add(i, 0, j)).getY();
			
			int depth = random.nextInt(configIn.depth);
			
			if (k - depth < 0) {
				return pos.add(i, 0, j);
			} else {
				return pos.add(i, k, j).down(depth);
			}
		});
	}
	
}
