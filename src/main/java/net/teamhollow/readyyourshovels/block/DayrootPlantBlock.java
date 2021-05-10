package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WeepingVinesPlantBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.state.property.RYSProperties;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

public class DayrootPlantBlock extends WeepingVinesPlantBlock {
    public static final BooleanProperty ROOT = RYSProperties.ROOT;

    public DayrootPlantBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ROOT, false));
    }

    @Override
    protected AbstractPlantStemBlock getStem() {
        return (AbstractPlantStemBlock) RYSBlocks.DAYROOT;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (newState.isOf(this)) {
            return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom).with(ROOT, world.getBlockState(pos.up()).isIn(RYSBlockTags.DIRT_LIKE));
        } else return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ROOT);
    }
}
