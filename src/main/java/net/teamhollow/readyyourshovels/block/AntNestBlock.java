package net.teamhollow.readyyourshovels.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;

public class AntNestBlock extends BlockWithEntity {
    public static final String id = "ant_nest";

    private static final Direction[] GENERATE_DIRECTIONS;
    public static final DirectionProperty FACING;

    public AntNestBlock() {
        super(FabricBlockSettings.of(Material.SOIL).breakByTool(FabricToolTags.SHOVELS).requiresTool().hardness(1.0F).resistance(1.5F).sounds(BlockSoundGroup.GRAVEL));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
            Iterator<AbstractAntEntity> iterator = list1.iterator();

            while (iterator.hasNext()) {
                AbstractAntEntity antEntity = (AbstractAntEntity) iterator.next();
                if (antEntity.getTarget() == null) {
                    antEntity.setTarget((LivingEntity) list2.get(world.random.nextInt(i)));
                }
            }
        }
    }

    // private boolean hasAnts(World world, BlockPos pos) {
    //     BlockEntity blockEntity = world.getBlockEntity(pos);
    //     if (blockEntity instanceof AntNestBlockEntity) {
    //         AntNestBlockEntity antNestBlockEntity = (AntNestBlockEntity) blockEntity;
    //         return !antNestBlockEntity.hasNoAnts();
    //     } else {
    //         return false;
    //     }
    // }

    @Environment(EnvType.CLIENT)
    private void spawnHoneyParticles(World world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && world.random.nextFloat() >= 0.3F) {
            VoxelShape voxelShape = state.getCollisionShape(world, pos);
            double d = voxelShape.getMax(Direction.Axis.Y);
            if (d >= 1.0D && !state.isIn(BlockTags.IMPERMEABLE)) {
                double e = voxelShape.getMin(Direction.Axis.Y);
                if (e > 0.0D) {
                    this.addHoneyParticle(world, pos, voxelShape, (double) pos.getY() + e - 0.05D);
                } else {
                    BlockPos blockPos = pos.down();
                    BlockState blockState = world.getBlockState(blockPos);
                    VoxelShape voxelShape2 = blockState.getCollisionShape(world, blockPos);
                    double f = voxelShape2.getMax(Direction.Axis.Y);
                    if ((f < 1.0D || !blockState.isFullCube(world, blockPos)) && blockState.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, pos, voxelShape, (double) pos.getY() - 0.05D);
                    }
                }
            }

        }
    }

    @Environment(EnvType.CLIENT)
    private void addHoneyParticle(World world, BlockPos pos, VoxelShape shape, double height) {
        this.addHoneyParticle(world, (double) pos.getX() + shape.getMin(Direction.Axis.X),
                (double) pos.getX() + shape.getMax(Direction.Axis.X),
                (double) pos.getZ() + shape.getMin(Direction.Axis.Z),
                (double) pos.getZ() + shape.getMax(Direction.Axis.Z), height);
    }

    @Environment(EnvType.CLIENT)
    private void addHoneyParticle(World world, double minX, double maxX, double minZ, double maxZ, double height) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(world.random.nextDouble(), minX, maxX), height, MathHelper.lerp(world.random.nextDouble(), minZ, maxZ), 0.0D, 0.0D, 0.0D);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockEntity createBlockEntity(BlockView world) {
        return new AntNestBlockEntity();
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                ItemStack itemStack = new ItemStack(this);
                // int i = (Integer) state.get(HONEY_LEVEL);
                boolean bl = !antBlockEntity.hasNoAnts();
                // if (!bl && i == 0) {
                //     return;
                // }

                CompoundTag compoundTag2;
                if (bl) {
                    compoundTag2 = new CompoundTag();
                    compoundTag2.put("Ants", antBlockEntity.getAnts());
                    itemStack.putSubTag("BlockEntityTag", compoundTag2);
                }

                compoundTag2 = new CompoundTag();
                // compoundTag2.putInt("honey_level", i); TODO
                itemStack.putSubTag("BlockStateTag", compoundTag2);
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX(), (double) pos.getY(),
                        (double) pos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        Entity entity = (Entity) builder.getNullable(LootContextParameters.THIS_ENTITY);
        if (entity instanceof TntEntity || entity instanceof CreeperEntity || entity instanceof WitherSkullEntity
                || entity instanceof WitherEntity || entity instanceof TntMinecartEntity) {
            BlockEntity blockEntity = (BlockEntity) builder.getNullable(LootContextParameters.BLOCK_ENTITY);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                antBlockEntity.angerAnts((PlayerEntity) null, state, AntNestBlockEntity.AntState.EMERGENCY);
            }
        }

        return super.getDroppedStacks(state, builder);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
            WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (world.getBlockState(posFrom).getBlock() instanceof FireBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity antBlockEntity = (AntNestBlockEntity) blockEntity;
                antBlockEntity.angerAnts((PlayerEntity) null, state, AntNestBlockEntity.AntState.EMERGENCY);
            }
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public static Direction getRandomGenerationDirection(Random random) {
        return (Direction) Util.getRandom((Object[]) GENERATE_DIRECTIONS, random);
    }

    static {
        GENERATE_DIRECTIONS = new Direction[] { Direction.WEST, Direction.EAST, Direction.SOUTH };
        FACING = HorizontalFacingBlock.FACING;
    }
}
