package group.rys.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import group.rys.core.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;
import java.util.function.Function;

public class AntHillFeature extends Feature<ProbabilityConfig> {


    public AntHillFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configIn, boolean doBlockNotifyIn)
    {
        super(configIn, doBlockNotifyIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos startPosition, ProbabilityConfig config) {
        while (startPosition.getY() > 1 && isAirOrLeaves(worldIn, startPosition)) startPosition = startPosition.down();

        if (!(rand.nextInt((int) config.probability) == 0))
        {
            return false;
        }

        int chancePlacement = 0;
        BlockPos actualPosition = startPosition.up();
        if (worldIn.isAirBlock(actualPosition) && worldIn.isAirBlock(actualPosition.up()) && worldIn.isAirBlock(actualPosition.up(2)) && worldIn.getBlockState(actualPosition.down()) == Blocks.GRASS_BLOCK.getDefaultState() ) {
            placementBlock(worldIn, rand, actualPosition);
            placementBlock(worldIn, rand, actualPosition.up());
            placementBlock(worldIn, rand, actualPosition.up(2));
        } else {
            return false;
        }

        actualPosition = startPosition.up().west();
        chancePlacement = rand.nextInt(4);
        if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0) {
            placementBlock(worldIn, rand, actualPosition);

            actualPosition = actualPosition.up();
            chancePlacement = rand.nextInt(2);
            if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0) {
                placementBlock(worldIn, rand, actualPosition);
            }
        }

        actualPosition = startPosition.up().east();
        chancePlacement = rand.nextInt(4);
        if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0)
        {
            placementBlock(worldIn, rand, actualPosition);

            actualPosition = actualPosition.up();
            chancePlacement = rand.nextInt(2);
            if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0) {
                placementBlock(worldIn, rand, actualPosition);
            }
        }

        actualPosition = startPosition.up().north();
        chancePlacement = rand.nextInt(4);
        if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0)
        {
            placementBlock(worldIn, rand, actualPosition);

            actualPosition = actualPosition.up();
            chancePlacement = rand.nextInt(2);
            if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0) {
                placementBlock(worldIn, rand, actualPosition);
            }
        }

        actualPosition = startPosition.up().south();
        chancePlacement = rand.nextInt(4);
        if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0)
        {
            placementBlock(worldIn, rand, actualPosition);

            actualPosition = actualPosition.up();
            chancePlacement = rand.nextInt(2);
            if (worldIn.isAirBlock(actualPosition) && chancePlacement != 0) {
                placementBlock(worldIn, rand, actualPosition);
            }
        }


        return false;
    }

    protected static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (!(worldIn instanceof net.minecraft.world.IWorldReader)) // FORGE: Redirect to state method when possible
            return worldIn.hasBlockState(pos, (p_214581_0_) -> {
                return p_214581_0_.isAir() || p_214581_0_.isIn(BlockTags.LEAVES);
            });
        else return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.IWorldReader)worldIn, pos));
    }

    private static void placementBlock(IWorld worldIn, Random rand, BlockPos pos)
    {
        int i = rand.nextInt(3);
        if (i == 0) worldIn.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState(), 2);
        if (i == 1) worldIn.setBlockState(pos, ModBlocks.anthill.getDefaultState(), 2);
        if (i == 2) worldIn.setBlockState(pos, ModBlocks.tough_dirt.getDefaultState(), 2);
    }
}
