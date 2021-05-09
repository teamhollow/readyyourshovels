package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.entity.ant.AbstractAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.state.property.RYSProperties;

import java.util.List;

public class AntNestBlock extends BlockWithEntity {
    public static final String id = "ant_nest";

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final IntProperty ACID_LEVEL = RYSProperties.ACID_LEVEL;

    public AntNestBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACID_LEVEL, 0));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, RYSBlockEntities.ANT_NEST, AntNestBlockEntity::serverTick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACID_LEVEL, FACING);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (!world.isClient && blockEntity instanceof AntNestBlockEntity) {
            AntNestBlockEntity antNestBlockEntity = (AntNestBlockEntity) blockEntity;
            if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                antNestBlockEntity.angerAnts(player, state, AntNestBlockEntity.AntState.EMERGENCY);
                this.angerNearbyAnts(world, pos);
            }
        }

    }

    private void angerNearbyAnts(World world, BlockPos pos) {
        List<AbstractAntEntity> list1 = world.getNonSpectatingEntities(AbstractAntEntity.class, (new Box(pos)).expand(8.0D, 6.0D, 8.0D));
        if (!list1.isEmpty()) {
            List<PlayerEntity> list2 = world.getNonSpectatingEntities(PlayerEntity.class,
                (new Box(pos)).expand(8.0D, 6.0D, 8.0D));
            int i = list2.size();

            for (AbstractAntEntity antEntity : list1) {
                if (antEntity.getTarget() == null) {
                    antEntity.setTarget(list2.get(world.random.nextInt(i)));
                }
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AntNestBlockEntity(pos, state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                ItemStack itemStack = new ItemStack(this);
                int acidLevel = state.get(ACID_LEVEL);
                boolean hasAnts = antBlockEntity.hasAnts();
                if (!hasAnts && acidLevel == 0) {
                    return;
                }

                NbtCompound tag;
                if (hasAnts) {
                    tag = new NbtCompound();
                    tag.put("Ants", antBlockEntity.getAnts());
                    itemStack.putSubTag("BlockEntityTag", tag);
                }

                tag = new NbtCompound();
                tag.putInt("acid_level", acidLevel);
                itemStack.putSubTag("BlockStateTag", tag);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        Entity entity = builder.getNullable(LootContextParameters.THIS_ENTITY);
        if (entity instanceof TntEntity || entity instanceof CreeperEntity || entity instanceof WitherSkullEntity
            || entity instanceof WitherEntity || entity instanceof TntMinecartEntity) {
            BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                antBlockEntity.angerAnts(null, state, AntNestBlockEntity.AntState.EMERGENCY);
            }
        }

        return super.getDroppedStacks(state, builder);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (world.getBlockState(posFrom).getBlock() instanceof FireBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                antBlockEntity.angerAnts(null, state, AntNestBlockEntity.AntState.EMERGENCY);
            }
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    private boolean hasAnts(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestBlockEntity) {
            AntNestBlockEntity antNestBlockEntity = (AntNestBlockEntity) blockEntity;
            return antNestBlockEntity.hasAnts();
        } else {
            return false;
        }
    }

    public void takeAcid(World world, BlockState state, BlockPos pos, PlayerEntity player, AntNestBlockEntity.AntState antState) {
        this.takeAcid(world, state, pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestBlockEntity) {
            ((AntNestBlockEntity) blockEntity).angerAnts(player, state, antState);
        }
    }
    public void takeAcid(World world, BlockState state, BlockPos pos) {
        world.setBlockState(pos, state.with(ACID_LEVEL, 0), 3);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        int i = state.get(ACID_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                itemStack.decrement(1);
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                if (itemStack.isEmpty()) {
                    player.setStackInHand(hand, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!player.getInventory().insertStack(new ItemStack(Items.HONEY_BOTTLE))) {
                    player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
                }

                bl = true;
            }
        }

        if (bl) {
            if (!CampfireBlock.isLitCampfireInRange(world, pos)) {
                if (this.hasAnts(world, pos)) {
                    this.angerNearbyAnts(world, pos);
                }

                this.takeAcid(world, state, pos, player, AntNestBlockEntity.AntState.EMERGENCY);
            } else {
                this.takeAcid(world, state, pos);
            }

            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(ACID_LEVEL);
    }
}
