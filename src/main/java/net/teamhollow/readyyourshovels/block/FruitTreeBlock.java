package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class FruitTreeBlock extends TallPlantBlock implements Fertilizable {
    protected static final VoxelShape LOG_SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);

    public static final IntProperty AGE = Properties.AGE_3;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    private final Item fruit;
    private final Item rottenFruit;

    public FruitTreeBlock(Item fruit, Item rottenFruit, AbstractBlock.Settings settings) {
        super(settings);

        this.fruit = fruit;
        this.rottenFruit = rottenFruit;

        this.setDefaultState(this.getStateManager().getDefaultState().with(AGE, 0).with(HALF, DoubleBlockHalf.LOWER));
    }

    public Item getFruit() {
        return this.fruit;
    }
    public Item getRottenFruit() {
        return this.rottenFruit;
    }

    public VoxelShape getShape(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? LOG_SHAPE : (state.get(AGE) <= 1 ? VoxelShapes.empty() : VoxelShapes.fullCube());
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (!world.isChunkLoaded(pos)) {
            return;
        }

        if (age < 3 && world.getLightLevel(pos.up()) >= 9 && random.nextInt(7) == 0 && state.get(HALF) == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos, state.with(AGE, age + 1), 2);
            if (world.getBlockState(pos.down()).getBlock() == this) {
                world.setBlockState(pos.down(), world.getBlockState(pos.down()).with(AGE, age + 1), 2);
            } else if (world.getBlockState(pos.up()).isAir()) {
                world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, 1), 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int age = state.get(AGE);

        if (world instanceof ServerWorld && age == 3 && state.get(HALF) == DoubleBlockHalf.UPPER) {
            this.harvest((ServerWorld)world, pos, state);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < 3 || state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45F;
    }

    @Override
    public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(AGE) + 1);

        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos, state.with(AGE, i), 2);
            if (world.getBlockState(pos.down()).getBlock() == this) {
                world.setBlockState(pos.down(), world.getBlockState(pos.down()).with(AGE, i), 2);
            }
        } else if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            world.setBlockState(pos, state.with(AGE, i), 2);
            if (world.getBlockState(pos.up()).getBlock() == this) {
                world.setBlockState(pos.up(), world.getBlockState(pos.up()).with(AGE, i), 2);
            } else if (world.getBlockState(pos.up()).isAir()) {
                world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER).with(AGE, 1), 2);
            }
        }
    }
    public void harvest(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            ReadyYourShovels.log(Level.ERROR, "FruitTreeBlock#harvest called with lower half");
            return;
        }

        world.setBlockState(pos, state.with(AGE, state.get(AGE) - 1), 3);

        BlockPos lowerPos = pos.down();
        BlockState lowerState = world.getBlockState(lowerPos);
        world.setBlockState(lowerPos, lowerState.with(AGE, lowerState.get(AGE) - 1), 3);

        if (world.random.nextInt(5) == 0)
            Block.dropStack(world, pos, new ItemStack(this.getRottenFruit(), 1 + world.random.nextInt(4)));
        else
            Block.dropStack(world, pos, new ItemStack(this.getFruit(), 1 + world.random.nextInt(4)));

        world.playSound(null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

    }

    @Override
    public void placeAt(WorldAccess world, BlockPos pos, int flags) {
        world.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
    }

    @Override
    public AbstractBlock.OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
