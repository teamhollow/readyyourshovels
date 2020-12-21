package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.block.FruitTreeBlock;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;
import net.teamhollow.readyyourshovels.util.InventoryUtils;

import java.util.EnumSet;
import java.util.List;

public class GardenAntEntity extends AbstractAntEntity {
    public static final String id = "garden_ant";

    private static final TrackedData<BlockPos> RESOURCE_POS = DataTracker.registerData(GardenAntEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public GardenAntEntity(EntityType<? extends AbstractAntEntity> type, World worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }

    public BlockPos getResourcePos() {
        return this.dataTracker.get(RESOURCE_POS);
    }
    public boolean hasResourcePos() {
        return this.getResourcePos() != BlockPos.ZERO;
    }
    public void setResourcePos(BlockPos pos) {
        this.dataTracker.set(RESOURCE_POS, pos);
    }
    public void resetResourcePos() {
        this.setResourcePos(new BlockPos(BlockPos.ZERO));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(RESOURCE_POS, new BlockPos(BlockPos.ZERO));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putInt("Age", this.age);

        if (this.hasResourcePos()) tag.put("ResourcePos", NbtHelper.fromBlockPos(this.getResourcePos()));
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.age = tag.getInt("Age");

        BlockPos resourcePos = NbtHelper.toBlockPos(tag.getCompound("ResourcePos"));
        if (tag.get("ResourcePos") != null) this.setResourcePos(resourcePos);
    }

    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return AbstractAntEntity.createAntAttributes();
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(0, new LocateResourceGoal(this));
        this.goalSelector.add(1, new UpdateResourceGoal(this));
		this.goalSelector.add(2, new GardenAntEntity.FindItemsGoal(this));
		this.goalSelector.add(3, new GardenAntEntity.StoreItemsGoal(this));
		this.goalSelector.add(4, new GardenAntEntity.HarvestFruitTreeGoal(this));
        // this.goalSelector.add(6, new WanderAroundResourceGoal(this, 1.0D));
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.world.isClient && this.isAlive() && this.age >= 2400 && this.getMainHandStack().isEmpty())
            this.remove();
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return RYSEntities.GARDEN_ANT.create(world);
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        return stack.getItem().isIn(RYSItemTags.FRUIT_TREE_ITEMS);
    }

    public static class LocateResourceGoal extends Goal {
        private final GardenAntEntity ant;

        public LocateResourceGoal(GardenAntEntity ant) {
            this.ant = ant;
        }

        @Override
        public boolean canStart() {
            return !ant.hasResourcePos();
        }

        @Override
        public void tick() {
            BlockPos pos = this.ant.getBlockPos();
            World world = this.ant.getEntityWorld();

            BlockPos resourcePos = null;

            for (BlockPos iPos : BlockPos.iterate(pos.getX() - 16, pos.getY() - 16, pos.getZ() - 16, pos.getX() + 16, pos.getY() + 16, pos.getZ() + 16)) {
                if (world.getBlockState(iPos).getBlock() == RYSBlocks.ANT_NEST && InventoryUtils.canStoreStackInInventory(world, iPos, this.ant.getMainHandStack())) {
                    resourcePos = iPos;
                    break;
                }
            }

            if (resourcePos != null) {
                this.ant.setResourcePos(resourcePos);
            }
        }
    }

    public static class UpdateResourceGoal extends Goal {
        private final GardenAntEntity ant;

        public UpdateResourceGoal(GardenAntEntity ant) {
            this.ant = ant;
        }

        @Override
        public boolean canStart() {
            return ant.hasResourcePos();
        }

        @Override
        public void tick() {
            World world = this.ant.getEntityWorld();
            BlockPos pos = this.ant.getResourcePos();

            if (world.getBlockState(pos).getBlock() != RYSBlocks.ANT_NEST || !InventoryUtils.canStoreStackInInventory(world, pos, this.ant.getMainHandStack())) {
                this.ant.resetResourcePos();
            }
        }
    }

    public static class FindItemsGoal extends Goal {
        private final GardenAntEntity ant;
        private List<ItemEntity> list;

        public FindItemsGoal(GardenAntEntity ant) {
            this.ant = ant;
            this.list = null;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            list = this.ant.world.getEntitiesByClass(ItemEntity.class, this.ant.getBoundingBox().expand(8.0D), (itemEntity -> itemEntity.getStack().getItem().isIn(RYSItemTags.FRUIT_TREE_ITEMS)));
            return this.ant.getMainHandStack().isEmpty() && !list.isEmpty();
        }

        @Override
        public void start() {
            if (!list.isEmpty())
                this.ant.getNavigation().startMovingAlong(this.ant.getNavigation().findPathTo(list.get(0), 16), 1.0D);
        }
    }

    public static class StoreItemsGoal extends Goal {
        private final GardenAntEntity ant;

        public StoreItemsGoal(GardenAntEntity ant) {
            this.ant = ant;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return !this.ant.getMainHandStack().isEmpty() && this.ant.hasResourcePos();
        }

        @Override
        public void start() {
            World world = this.ant.world;
            BlockPos pos = this.ant.getResourcePos();
            BlockState state = world.getBlockState(pos);

            ItemStack stack = this.ant.getMainHandStack();

            if (state.getBlock() == RYSBlocks.ANT_NEST && InventoryUtils.canStoreStackInInventory(world, pos, stack)) {
                this.ant.getMoveControl().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);

                if (this.ant.getPos().squaredDistanceTo(new Vec3d(pos.getX(), pos.getY(), pos.getZ())) <= 1) {
                    InventoryUtils.addStackToInventory(world, pos, stack);
                    this.ant.equipLootStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
            }
        }
    }

    public static class HarvestFruitTreeGoal extends Goal {
        private final GardenAntEntity ant;

        public HarvestFruitTreeGoal(GardenAntEntity ant) {
            this.ant = ant;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            List<ItemEntity> list = this.ant.world.getEntitiesByClass(ItemEntity.class, this.ant.getBoundingBox().expand(8.0D), (itemEntity -> itemEntity.getStack().getItem().isIn(RYSItemTags.FRUIT_TREE_ITEMS)));

            assert list != null;
            return this.ant.getMainHandStack().isEmpty() && list.isEmpty();
        }

        @Override
        public void start() {
            World world = this.ant.world;
            if (!(world instanceof ServerWorld)) return;

            BlockPos pos = this.ant.getBlockPos();
            BlockPos treePos = null;

            for (BlockPos iPos : BlockPos.iterate(pos.getX() - 16, pos.getY() - 16, pos.getZ() - 16, pos.getX() + 16, pos.getY() + 16, pos.getZ() + 16)) {
                System.out.println("ITERATING: " + iPos.toString());
                BlockState state = world.getBlockState(iPos);

                if (state.getBlock() instanceof FruitTreeBlock && (state.get(FruitTreeBlock.HALF) == DoubleBlockHalf.UPPER && state.get(FruitTreeBlock.AGE) == 3)) {
                    System.out.println("FOUND TREE");
                    treePos = iPos;
                    break;
                }
            }

            if (treePos != null) {
                System.out.println("TREE");
                BlockState state = world.getBlockState(treePos);
                this.ant.getMoveControl().moveTo(treePos.getX(), treePos.getY(), treePos.getZ(), 1.2D);

                if (this.ant.getPos().squaredDistanceTo(Vec3d.ofCenter(treePos)) <= 6) {
                    System.out.println("ANT IS NEAR");
                    FruitTreeBlock block = (FruitTreeBlock) state.getBlock();
                    block.harvest((ServerWorld)world, treePos, state);
                }
            }
        }
    }

    public static class WanderAroundResourceGoal extends WanderAroundGoal {
        public WanderAroundResourceGoal(PathAwareEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        public boolean canStart() {
            GardenAntEntity ant = (GardenAntEntity)this.mob;
            return ant.hasResourcePos() && ant.getNavigation().isIdle();
        }

        @Override
        public void start() {
            World world = this.mob.world;
            int x = world.random.nextInt(16) - 16;
            int y = world.random.nextInt(16) - 16;
            int z = world.random.nextInt(16) - 16;

            BlockPos resourcePos = ((GardenAntEntity) this.mob).getResourcePos();
            BlockPos randPos = resourcePos.add(x, y, z);

            if (world.isAir(randPos) && !world.isWater(randPos) && !world.isAir(randPos.down()) && !world.isWater(randPos.down()))
                this.mob.getNavigation().startMovingTo(randPos.getX(), randPos.getY(), randPos.getZ(), 1.0D);
        }
    }
}
