package net.teamhollow.readyyourshovels.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.block.DayrootPlantBlock;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

public class DirtCaveDayrootFeature extends Feature<DefaultFeatureConfig> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public DirtCaveDayrootFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
        if (!structureWorldAccess.isAir(blockPos)) {
            return false;
        } else {
            BlockState blockState = structureWorldAccess.getBlockState(blockPos.up());
            if (!blockState.isOf(RYSBlocks.TOUGH_DIRT) && !blockState.isOf(RYSBlocks.DAYROOT_CROWN)) {
                return false;
            } else {
                this.generateDayrootCrownsInArea(structureWorldAccess, random, blockPos);
                this.generateDayrootsInArea(structureWorldAccess, random, blockPos);
                return true;
            }
        }
    }

    private void generateDayrootCrownsInArea(WorldAccess world, Random random, BlockPos pos) {
        world.setBlockState(pos, RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutable2 = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mutable.set(pos, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
            if (!world.isAir(mutable)) {
                boolean bool = false;

                for (Direction direction : DIRECTIONS) {
                    BlockState blockState = world.getBlockState(mutable2.set(mutable, direction));
                    if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(RYSBlocks.DAYROOT_CROWN)) {
                        bool = true;
                        break;
                    }
                }

                if (bool) world.setBlockState(mutable, RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);
            }
        }
    }

    private void generateDayrootsInArea(WorldAccess world, Random random, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mutable.set(pos, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if (world.isAir(mutable)) {
                BlockState blockState = world.getBlockState(mutable.up());
                if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(RYSBlocks.DAYROOT_CROWN)) {
                    int length = MathHelper.nextInt(random, 1, 8);
                    if (random.nextInt(6) == 0) {
                        length *= 2;
                    }

                    if (random.nextInt(5) == 0) {
                        length = 1;
                    }

                    generateDayrootColumn(world, random, mutable, length, 17, 25);
                }
            }
        }
    }

    public static void generateDayrootColumn(WorldAccess world, Random random, BlockPos.Mutable pos, int length, int minAge, int maxAge) {
        world.setBlockState(pos.up(), RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);

        for (int i = 0; i <= length; ++i) {
            if (world.isAir(pos)) {
                if (i == length || !world.isAir(pos.down())) {
                    world.setBlockState(pos, RYSBlocks.DAYROOT.getDefaultState().with(AbstractPlantStemBlock.AGE, MathHelper.nextInt(random, minAge, maxAge)), 2);
                    break;
                }

                world.setBlockState(pos, RYSBlocks.DAYROOT_PLANT.getDefaultState().with(DayrootPlantBlock.ROOT, world.getBlockState(pos.up()).isIn(RYSBlockTags.DIRT_LIKE)), 2);
            }

            pos.move(Direction.DOWN);
        }
    }
}
