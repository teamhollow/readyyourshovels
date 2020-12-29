package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

import java.util.Random;

public class DirtCaveToughrootFeature extends Feature<DefaultFeatureConfig> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public DirtCaveToughrootFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
        if (!structureWorldAccess.isAir(blockPos)) {
            return false;
        } else {
            BlockState blockState = structureWorldAccess.getBlockState(blockPos.up());
            if (!blockState.isOf(RYSBlocks.TOUGH_DIRT) && !blockState.isOf(Blocks.DIRT)) {
                return false;
            } else {
                this.generateDayrootCrownsInArea(structureWorldAccess, random, blockPos);
                this.generateDayrootsInArea(structureWorldAccess, random, blockPos);
                return true;
            }
        }
    }

    private void generateDayrootCrownsInArea(WorldAccess world, Random random, BlockPos pos) {
        world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutable2 = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mutable.set(pos, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
            if (!world.isAir(mutable)) {
                boolean bool = false;

                for (Direction direction : DIRECTIONS) {
                    BlockState blockState = world.getBlockState(mutable2.set(mutable, direction));
                    if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(Blocks.DIRT)) {
                        bool = true;
                        break;
                    }
                }

                if (bool) world.setBlockState(mutable, Blocks.DIRT.getDefaultState(), 2);
            }
        }
    }

    private void generateDayrootsInArea(WorldAccess world, Random random, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mutable.set(pos, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if (world.isAir(mutable)) {
                BlockState blockState = world.getBlockState(mutable.up());
                if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(Blocks.DIRT)) {
                    world.setBlockState(pos.up(), Blocks.DIRT.getDefaultState(), 2);
                    world.setBlockState(pos, RYSBlocks.TOUGHROOT.getDefaultState(), 2);
                }
            }
        }
    }
}
