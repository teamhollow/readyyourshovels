package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WeepingVinesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

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
}
