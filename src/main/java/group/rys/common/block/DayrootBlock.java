package group.rys.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DayrootBlock extends BushBlock implements IGrowable {
	
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	
	public DayrootBlock(Block.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HALF, DoubleBlockHalf.UPPER));
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		DoubleBlockHalf half = stateIn.get(HALF);
		
		if (facing.getAxis() != Direction.Axis.Y || (half == DoubleBlockHalf.LOWER && facing == Direction.UP) && (facingState.getBlock() == this && facingState.get(HALF) != half) || (half == DoubleBlockHalf.UPPER && facing == Direction.DOWN) && (facingState.getBlock() == this && facingState.get(HALF) != half) || (half == DoubleBlockHalf.LOWER && facing == Direction.DOWN)) {
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (context.getPos().getY() < context.getWorld().getHeight() - 1 && context.getWorld().getBlockState(context.getPos().down()).isReplaceable(context)) {
			return super.getStateForPlacement(context);
		} else {
			return null;
		}
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.down(), this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), 3);
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState state_1 = worldIn.getBlockState(pos.up());
		
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			return state_1.canSustainPlant(worldIn, pos.up(), Direction.DOWN, this);
		} else {
			return state_1.getBlock() == this && state_1.get(HALF) == DoubleBlockHalf.UPPER;
		}
	}
	
	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
		worldIn.setBlockState(pos.down(), this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
	}
	
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}
	
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos pos_1 = pos.up();
		
		if (state.get(HALF) == DoubleBlockHalf.UPPER) {
			pos_1 = pos.down();
		}
		
		worldIn.setBlockState(pos_1, Blocks.AIR.getDefaultState(), 35);
		
		if (!worldIn.isRemote() && !player.isCreative()) {
			spawnDrops(state, worldIn, pos, null, player, player.getHeldItemMainhand());
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
		super.onBlockHarvested(worldIn, pos_1, state, player);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}
	
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
	}
	
	@OnlyIn(Dist.CLIENT)
	public long getPositionRandom(BlockState state, BlockPos pos) {
		return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}
	
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}
	
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}
	
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
		spawnAsEntity(worldIn, pos, new ItemStack(this));
	}
	
}
