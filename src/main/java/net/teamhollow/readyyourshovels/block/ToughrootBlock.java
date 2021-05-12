package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ToughrootBlock extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty VERTICAL_DIRECTION = DirectionProperty.of("vertical_direction", Direction.DOWN, Direction.UP);

    public ToughrootBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(VERTICAL_DIRECTION, Direction.DOWN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED, VERTICAL_DIRECTION);
    }

    protected boolean canPlantAt(BlockState state) {
        return state.isIn(RYSBlockTags.DIRT);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction side = ctx.getSide();
        BlockState defaultState = this.getDefaultState();
        return defaultState.with(
            VERTICAL_DIRECTION, side.getAxis() == Direction.Axis.Y
                ? side
                : this.canPlaceAt(defaultState.with(VERTICAL_DIRECTION, Direction.DOWN), ctx.getWorld(), ctx.getBlockPos())
                    ? Direction.DOWN
                    : Direction.UP
        ).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isIn(FluidTags.WATER));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos checkPos = state.get(VERTICAL_DIRECTION) == Direction.DOWN
            ? pos.up()
            : pos.down();

        return this.canPlantAt(world.getBlockState(checkPos));
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.AIR && !this.collidable || super.canPathfindThrough(state, world, pos, type);
    }
}
