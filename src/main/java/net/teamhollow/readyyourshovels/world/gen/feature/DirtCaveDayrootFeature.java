package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.teamhollow.readyyourshovels.block.DayrootPlantBlock;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

import java.util.Random;

public class DirtCaveDayrootFeature extends Feature<DefaultFeatureConfig> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public DirtCaveDayrootFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        return this.generate(context.getWorld(), context.getRandom(), context.getOrigin());
    }

    public boolean generate(StructureWorldAccess world, Random random, BlockPos pos) {
        if (!world.isAir(pos) || !world.getFluidState(pos).isEmpty()) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos.up());
            if (!state.isOf(RYSBlocks.TOUGH_DIRT) && !state.isOf(RYSBlocks.DAYROOT_CROWN)) {
                return false;
            } else {
                this.generateDayrootCrownsInArea(world, random, pos);
                this.generateDayrootsInArea(world, random, pos);
                return true;
            }
        }
    }

    private void generateDayrootCrownsInArea(WorldAccess world, Random random, BlockPos pos) {
        world.setBlockState(pos, RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);
        BlockPos.Mutable root = new BlockPos.Mutable();
        BlockPos.Mutable mpos = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            root.set(pos, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
            if (!world.isAir(root)) {
                boolean placedAny = false;

                for (Direction direction : DIRECTIONS) {
                    BlockState state = world.getBlockState(mpos.set(root, direction));
                    if (state.isOf(RYSBlocks.TOUGH_DIRT) || state.isOf(RYSBlocks.DAYROOT_CROWN)) {
                        placedAny = true;
                        break;
                    }
                }

                if (placedAny) {
                    world.setBlockState(root, RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);
                }
            }
        }
    }

    private void generateDayrootsInArea(WorldAccess world, Random random, BlockPos pos) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mpos.set(pos, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if (world.isAir(mpos)) {
                BlockState blockState = world.getBlockState(mpos.up());
                if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(RYSBlocks.DAYROOT_CROWN)) {
                    int length = MathHelper.nextInt(random, 1, 8);
                    if (random.nextInt(6) == 0) {
                        length *= 2;
                    }

                    if (random.nextInt(5) == 0) {
                        length = 1;
                    }

                    generateDayrootColumn(world, random, mpos, length, 17, 25);
                }
            }
        }
    }

    public static void generateDayrootColumn(WorldAccess world, Random random, BlockPos.Mutable mpos, int length, int minAge, int maxAge) {
        world.setBlockState(mpos.up(), RYSBlocks.DAYROOT_CROWN.getDefaultState(), 2);

        BlockPos.Mutable mposDown2 = mpos.down(2).mutableCopy();
        BlockPos.Mutable mposDown3 = mpos.down(3).mutableCopy();

        for (int i = 0; i <= length; ++i) {
            if (world.isAir(mpos) && world.isAir(mposDown2)) {
                if (i == length || !world.isAir(mposDown3)) {
                    world.setBlockState(mpos, RYSBlocks.DAYROOT.getDefaultState().with(AbstractPlantStemBlock.AGE, MathHelper.nextInt(random, minAge, maxAge)), 2);
                    break;
                }

                world.setBlockState(mpos, RYSBlocks.DAYROOT_PLANT.getDefaultState().with(DayrootPlantBlock.ROOT, world.getBlockState(mpos.up()).isIn(RYSBlockTags.DIRT)), 2);
            }

            for (BlockPos.Mutable impos/*tor*/ : new BlockPos.Mutable[]{ mpos, mposDown2, mposDown3 }) {
                impos.move(Direction.DOWN);
            }
        }
    }
}
