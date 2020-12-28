package net.teamhollow.readyyourshovels.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSParticles;

import java.util.Random;

public class DayrootBlock extends WeepingVinesBlock {
    public DayrootBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Block getPlant() {
        return RYSBlocks.DAYROOT_PLANT;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextDouble() <= 0.32D) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (!blockState.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
                double clamp = 0.3D;
                double x = pos.getX() + Math.min(1 - clamp, Math.max(clamp, random.nextDouble()));
                double y = pos.getY() + 0.1D;
                double z = pos.getZ() + Math.min(1 - clamp, Math.max(clamp, random.nextDouble()));
                world.addParticle(RYSParticles.DAYROOT, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
