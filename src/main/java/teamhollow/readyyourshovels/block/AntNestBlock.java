package teamhollow.readyyourshovels.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamhollow.readyyourshovels.entity.AbstractAntEntity;
import teamhollow.readyyourshovels.tileentity.AntNestTileEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class AntNestBlock extends Block implements ITileEntityProvider {
    public AntNestBlock(Properties properties) {
        super(properties);
    }

    private static final Direction[] GENERATE_DIRECTIONS = new Direction[]{Direction.WEST, Direction.EAST, Direction.SOUTH};
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final IntegerProperty RESOURCE_LEVEL = IntegerProperty.create("resource_level", 0, 5);

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(RESOURCE_LEVEL, FACING);
    }

    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (!worldIn.isRemote && te instanceof AntNestTileEntity) {
            AntNestTileEntity antnesttileentity = (AntNestTileEntity) te;
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
                antnesttileentity.angerAnts(player, state, AntNestTileEntity.State.EMERGENCY);
                worldIn.updateComparatorOutputLevel(pos, this);
                this.angerNearbyAnts(worldIn, pos);
            }

            CriteriaTriggers.BEE_NEST_DESTROYED.test((ServerPlayerEntity) player, state.getBlock(), stack, antnesttileentity.getBeeCount());
        }

    }

    private void angerNearbyAnts(World world, BlockPos pos) {
        List<AbstractAntEntity> list = world.getEntitiesWithinAABB(AbstractAntEntity.class, (new AxisAlignedBB(pos)).grow(8.0D, 6.0D, 8.0D));
        if (!list.isEmpty()) {
            List<PlayerEntity> list1 = world.getEntitiesWithinAABB(PlayerEntity.class, (new AxisAlignedBB(pos)).grow(8.0D, 6.0D, 8.0D));
            int i = list1.size();

            for (AbstractAntEntity antEntity : list) {
                if (antEntity.getAttackTarget() == null) {
                    antEntity.setAttackTarget(list1.get(world.rand.nextInt(i)));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(RESOURCE_LEVEL) >= 5) {
            for (int i = 0; i < rand.nextInt(1) + 1; ++i) {
                this.addHoneyParticle(worldIn, pos, stateIn);
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void addHoneyParticle(World world, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(world.rand.nextFloat() < 0.3F)) {
            VoxelShape voxelshape = state.getCollisionShape(world, pos);
            double d0 = voxelshape.getEnd(Direction.Axis.Y);
            if (d0 >= 1.0D && !state.isIn(BlockTags.IMPERMEABLE)) {
                double d1 = voxelshape.getStart(Direction.Axis.Y);
                if (d1 > 0.0D) {
                    this.addHoneyParticle(world, pos, voxelshape, (double) pos.getY() + d1 - 0.05D);
                } else {
                    BlockPos blockpos = pos.down();
                    BlockState blockstate = world.getBlockState(blockpos);
                    VoxelShape voxelshape1 = blockstate.getCollisionShape(world, blockpos);
                    double d2 = voxelshape1.getEnd(Direction.Axis.Y);
                    if ((d2 < 1.0D || !blockstate.hasOpaqueCollisionShape(world, blockpos)) && blockstate.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, pos, voxelshape, (double) pos.getY() - 0.05D);
                    }
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addHoneyParticle(World world, BlockPos pos, VoxelShape shape, double y) {
        this.addHoneyParticle(world, (double) pos.getX() + shape.getStart(Direction.Axis.X), (double) pos.getX() + shape.getEnd(Direction.Axis.X), (double) pos.getZ() + shape.getStart(Direction.Axis.Z), (double) pos.getZ() + shape.getEnd(Direction.Axis.Z), y);
    }

    @OnlyIn(Dist.CLIENT)
    private void addHoneyParticle(World particleData, double x1, double x2, double z1, double z2, double y) {
        particleData.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(particleData.rand.nextDouble(), x1, x2), y, MathHelper.lerp(particleData.rand.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return (BlockState) this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote() && player.isCreative() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            TileEntity TileEntity = world.getTileEntity(pos);
            if (TileEntity instanceof AntNestTileEntity) {
                AntNestTileEntity antTileEntity = (AntNestTileEntity) TileEntity;
                ItemStack itemStack = new ItemStack(this);
                int resourceLevel = state.get(RESOURCE_LEVEL);
                boolean hasAnts = !antTileEntity.hasNoAnts();
                if (!hasAnts && resourceLevel == 0) {
                    return;
                }

                CompoundNBT compoundTag;
                if (hasAnts) {
                    compoundTag = new CompoundNBT();
                    compoundTag.put("Ants", antTileEntity.getAnts());
                    itemStack.setTagInfo("TileEntityTag", compoundTag);
                }

                compoundTag = new CompoundNBT();
                compoundTag.putInt("resource_level", resourceLevel);
                itemStack.setTagInfo("BlockStateTag", compoundTag);
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX(), (double) pos.getY(),
                        (double) pos.getZ(), itemStack);
                itemEntity.setDefaultPickupDelay();
                world.addEntity(itemEntity);
            }
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        Entity entity = builder.get(LootParameters.THIS_ENTITY);
        if (entity instanceof TNTEntity || entity instanceof CreeperEntity || entity instanceof WitherSkullEntity
                || entity instanceof WitherEntity || entity instanceof TNTMinecartEntity) {
            TileEntity tileentity = builder.get(LootParameters.BLOCK_ENTITY);
            if (tileentity instanceof AntNestTileEntity) {
                AntNestTileEntity antTileEntity = (AntNestTileEntity) tileentity;
                antTileEntity.angerAnts((PlayerEntity) null, state, AntNestTileEntity.State.EMERGENCY);
            }
        }

        return super.getDrops(state, builder);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (world.getBlockState(facingPos).getBlock() instanceof FireBlock) {
            TileEntity TileEntity = world.getTileEntity(currentPos);
            if (TileEntity instanceof AntNestTileEntity) {
                AntNestTileEntity antTileEntity = (AntNestTileEntity) TileEntity;
                antTileEntity.angerAnts((PlayerEntity) null, stateIn, AntNestTileEntity.State.EMERGENCY);
            }
        }

        return super.updatePostPlacement(stateIn, facing, facingState, world, currentPos, facingPos);
    }

    private boolean hasAnts(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof AntNestTileEntity) {
            AntNestTileEntity antNestTileEntity = (AntNestTileEntity) tileEntity;
            return !antNestTileEntity.hasNoAnts();
        } else {
            return false;
        }
    }

    public void takeAcid(World world, BlockState state, BlockPos pos, PlayerEntity player, AntNestTileEntity.State antState) {
        this.takeAcid(world, state, pos);
        TileEntity TileEntity = world.getTileEntity(pos);
        if (TileEntity instanceof AntNestTileEntity) {
            AntNestTileEntity beehiveTileEntity = (AntNestTileEntity) TileEntity;
            beehiveTileEntity.angerAnts(player, state, antState);
        }
    }

    public void takeAcid(World world, BlockState state, BlockPos pos) {
        world.setBlockState(pos, state.with(RESOURCE_LEVEL, 0), 3);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemStack = player.getHeldItem(handIn);
        int i = (Integer) state.get(RESOURCE_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                itemStack.shrink(1);
                worldIn.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                if (itemStack.isEmpty()) {
                    player.setHeldItem(handIn, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.HONEY_BOTTLE))) {
                    player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
                }

                bl = true;
            }
        }

        if (bl) {
            if (!CampfireBlock.isSmokingBlockAt(worldIn, pos)) {
                if (this.hasAnts(worldIn, pos)) {
                    this.angerNearbyAnts(worldIn, pos);
                }

                this.takeAcid(worldIn, state, pos, player, AntNestTileEntity.State.EMERGENCY);
            } else {
                this.takeAcid(worldIn, state, pos);
            }

            return ActionResultType.SUCCESS;
        } else {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }
    }

    public static Direction getGenerationDirection(Random rand) {
        return Util.getRandomObject(GENERATE_DIRECTIONS, rand);
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(RESOURCE_LEVEL);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new AntNestTileEntity();
    }
}
