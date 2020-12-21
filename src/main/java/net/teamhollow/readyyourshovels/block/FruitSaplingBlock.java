package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class FruitSaplingBlock extends PlantBlock implements Fertilizable {
    private final FruitTreeBlock fruitTree;

    public FruitSaplingBlock(FruitTreeBlock fruitTree, AbstractBlock.Settings settings) {
        super(settings);
        this.fruitTree = fruitTree;
    }

    public FruitTreeBlock getFruitTree() {
        return fruitTree;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isChunkLoaded(pos)) {
            return;
        }

        if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(6) == 0) {
            this.getFruitTree().placeAt(world, pos, 2);
        }
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return world.random.nextFloat() < 0.36D;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
        this.getFruitTree().placeAt(world, pos, 2);
    }
}
