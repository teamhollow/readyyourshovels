package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.teamhollow.readyyourshovels.block.ToughrootBlock;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

import java.util.Random;

public class DirtCaveToughrootFeature extends Feature<DefaultFeatureConfig> {
    public static final BlockState TOUGHROOT_STATE = RYSBlocks.TOUGHROOT.getDefaultState().with(ToughrootBlock.VERTICAL_DIRECTION, Direction.DOWN);
    private static final Direction[] DIRECTIONS = Direction.values();

    public DirtCaveToughrootFeature(Codec<DefaultFeatureConfig> codec) {
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
            if (!state.isOf(RYSBlocks.TOUGH_DIRT) && !state.isOf(Blocks.DIRT)) {
                return false;
            } else {
                this.generateDirtInArea(world, random, pos);
                this.generateToughrootsInArea(world, random, pos);
                return true;
            }
        }
    }

    private void generateDirtInArea(WorldAccess world, Random random, BlockPos pos) {
        world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
        BlockPos.Mutable root = new BlockPos.Mutable();
        BlockPos.Mutable mpos = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            root.set(pos, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
            if (!world.isAir(root)) {
                boolean placedAny = false;

                for (Direction direction : DIRECTIONS) {
                    BlockState state = world.getBlockState(mpos.set(root, direction));
                    if (state.isOf(RYSBlocks.TOUGH_DIRT) || state.isOf(Blocks.DIRT)) {
                        placedAny = true;
                        break;
                    }
                }

                if (placedAny) {
                    world.setBlockState(root, Blocks.DIRT.getDefaultState(), 2);
                }
            }
        }
    }

    private void generateToughrootsInArea(WorldAccess world, Random random, BlockPos pos) {
        BlockPos.Mutable mpos = new BlockPos.Mutable();

        for (int i = 0; i < 100; ++i) {
            mpos.set(pos, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if (world.isAir(mpos)) {
                BlockState blockState = world.getBlockState(mpos.up());
                if (blockState.isOf(RYSBlocks.TOUGH_DIRT) || blockState.isOf(Blocks.DIRT)) {
                    world.setBlockState(pos.up(), Blocks.DIRT.getDefaultState(), 2);
                    world.setBlockState(pos, TOUGHROOT_STATE, 2);
                }
            }
        }
    }
}
