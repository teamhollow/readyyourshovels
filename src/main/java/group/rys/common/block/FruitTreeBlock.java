package group.rys.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class FruitTreeBlock extends BushBlock implements IGrowable {

    private Item fruit;
    private Item rottenFruit;
    private Item fruitSapling;

    protected static final VoxelShape LOG_SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_3;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public FruitTreeBlock(Item fruitIn, Item rottenFruitIn, Item fruitSapling, Block.Properties properties) {
        super(properties);
        this.fruit = fruitIn;
        this.rottenFruit = rottenFruitIn;
        this.fruitSapling = fruitSapling;
        this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)).with(HALF, DoubleBlockHalf.LOWER));
    }

    public Item getFruit() {
        return this.fruit;
    }

    public Item getRottenFruit() {
        return this.rottenFruit;
    }

    public Item getFruitSapling() {
        return fruitSapling;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return LOG_SHAPE;
        }

        return super.getShape(state, worldIn, pos, context);
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return LOG_SHAPE;
        }

        return super.getCollisionShape(state, worldIn, pos, context);
    }

    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }


        if (age < 3 && worldIn.getLight(pos.up()) >= 9 && random.nextInt(7) == 0 && state.get(HALF) == DoubleBlockHalf.UPPER) {
            worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(age + 1)), 2);
            if (worldIn.getBlockState(pos.down()).getBlock() == this.getBlock()) {
                worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(AGE, Integer.valueOf(age + 1)), 2);
            } else if (worldIn.getBlockState(pos.up()).isAir()) {
                worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, Integer.valueOf(1)), 2);
            }
        }
    }

    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int age = state.get(AGE);
        ItemStack stack = player.getHeldItem(handIn);

        if (age == 3 && state.get(HALF) == DoubleBlockHalf.UPPER) {
            worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(2)), 2);
            if (worldIn.getBlockState(pos.down()).getBlock() == this.getBlock()) {
                worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(AGE, Integer.valueOf(2)), 2);
            }

            if (worldIn.rand.nextInt(5) == 0) {
                spawnAsEntity(worldIn, pos, new ItemStack(this.rottenFruit, 1 + worldIn.rand.nextInt(4)));
            } else {
                spawnAsEntity(worldIn, pos, new ItemStack(this.fruit, 1 + worldIn.rand.nextInt(4)));
            }

            worldIn.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);

            return true;
        }

        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < 3 || state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return worldIn.rand.nextFloat() < 0.45F;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(AGE) + 1);

        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i)), 2);
            if (worldIn.getBlockState(pos.down()).getBlock() == this.getBlock()) {
                worldIn.setBlockState(pos.down(), worldIn.getBlockState(pos.down()).with(AGE, Integer.valueOf(i)), 2);
            }
        } else if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i)), 2);
            if (worldIn.getBlockState(pos.up()).getBlock() == this.getBlock()) {
                worldIn.setBlockState(pos.up(), worldIn.getBlockState(pos.up()).with(AGE, Integer.valueOf(i)), 2);
            } else if (worldIn.getBlockState(pos.up()).isAir()) {
                worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, Integer.valueOf(1)), 2);
            }
        }
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = stateIn.get(HALF);

        if (stateIn.isValidPosition(worldIn, currentPos)) {
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        return blockpos.getY() < context.getWorld().getDimension().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? super.getStateForPlacement(context) : null;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            return super.isValidPosition(state, worldIn, pos);
        } else {
            BlockState blockstate = worldIn.getBlockState(pos.down());
            if (state.getBlock() != this)
                return super.isValidPosition(state, worldIn, pos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
        worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(AGE, 2), flags);
        worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, 2), flags);
    }

    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleblockhalf = state.get(HALF);
        BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
            worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
            if (!worldIn.isRemote && !player.isCreative()) {
                spawnDrops(state, worldIn, pos, (TileEntity) null, player, player.getHeldItemMainhand());
                spawnDrops(blockstate, worldIn, blockpos, (TileEntity) null, player, player.getHeldItemMainhand());
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }


}
