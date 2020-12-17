package group.rys.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class PlanterBoxBlock extends FarmlandBlock {
	
	private static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE_0_7;
	private static final VoxelShape SHAPE = VoxelShapes.fullCube();
	
	public PlanterBoxBlock(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(MOISTURE, Integer.valueOf(0)));
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return stateIn;
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return true;
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState();
	}
	
	public boolean func_220074_n(BlockState state) {
		return false;
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		
	}
	
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.fall(fallDistance, 1.0F);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(MOISTURE);
	}
	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
	
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
		if (facing == Direction.UP) {
			BlockState state_1 = plantable.getPlant(world, pos.offset(facing));
			PlantType type = plantable.getPlantType(world, pos.offset(facing));
			
			if (state_1.getBlock() == Blocks.CACTUS || state_1.getBlock() == Blocks.SUGAR_CANE) {
				return true;
			}
			
			if (type == PlantType.Desert || type == PlantType.Crop || type == PlantType.Cave || type == PlantType.Plains) {
				return true;
			}
			
			return false;
		}
		
		return false;
	}
	
}
