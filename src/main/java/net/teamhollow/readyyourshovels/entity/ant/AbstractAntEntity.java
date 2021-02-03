package net.teamhollow.readyyourshovels.entity.ant;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSPointOfInterests;
import net.teamhollow.readyyourshovels.init.RYSSoundEvents;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAntEntity extends AnimalEntity {
    public static final String id = "ant";

    private static final TrackedData<BlockPos> NEST_POS = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private float currentPitch;
    protected int cannotEnterNestTicks;
    private int ticksLeftToFindNest = 0;
    protected AbstractAntEntity.MoveToAntNestGoal moveToNestGoal;
    private int ticksInsideWater;

    public AbstractAntEntity(EntityType<? extends AbstractAntEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(NEST_POS, null);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AbstractAntEntity.EnterNestGoal());
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new AbstractAntEntity.FindNestGoal());
        this.moveToNestGoal = new AbstractAntEntity.MoveToAntNestGoal();
        this.goalSelector.add(5, this.moveToNestGoal);
        this.goalSelector.add(8, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(9, new SwimGoal(this));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        if (this.hasNestPos()) tag.put("NestPos", NbtHelper.fromBlockPos(this.getNestPos()));
        tag.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.setNestPos(null);
        if (tag.contains("NestPos")) this.setNestPos(NbtHelper.toBlockPos(tag.getCompound("NestPos")));
        this.cannotEnterNestTicks = tag.getInt("CannotEnterNestTicks");
    }

    @Override
    public void tick() {
        super.tick();
        this.updateBodyPitch();
    }

    protected void addParticle(World world, double lastX, double x, double lastZ, double z, double y) {
        world.addParticle(ParticleTypes.FALLING_NECTAR, MathHelper.lerp(world.random.nextDouble(), lastX, x), y, MathHelper.lerp(world.random.nextDouble(), lastZ, z), 0.0D, 0.0D, 0.0D);
    }

    protected void startMovingTo(BlockPos pos) {
        Vec3d vec3d = this.getPos();
        int i = 0;
        BlockPos blockPos = this.getBlockPos();
        int j = (int) vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.getManhattanDistance(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3d vec3d2 = TargetFinder.findGroundTargetTowards(this, k, l, i, vec3d, 0.3141592741012573D);
        if (vec3d2 != null) {
            this.navigation.setRangeMultiplier(0.5F);
            this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0D);
        }
    }

    protected boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && this.getTarget() == null) {
            return (this.world.isRaining() || this.world.isNight()) && this.isNestNotNearFire();
        } else {
            return false;
        }
    }

    public void setCannotEnterNestTicks(int ticks) {
        this.cannotEnterNestTicks = ticks;
    }

    private void updateBodyPitch() {
        this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
    }

    @Override
    protected void mobTick() {
        if (this.isInsideWaterOrBubbleColumn()) {
            this.ticksInsideWater++;
        } else {
            this.ticksInsideWater = 0;
        }

        if (this.ticksInsideWater > 20) {
            this.damage(DamageSource.DROWN, 1.0F);
        }
    }

    protected boolean isNestNotNearFire() {
        if (!this.hasNestPos()) {
            return true;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.getNestPos());
            return !(blockEntity instanceof AntNestBlockEntity) || !((AntNestBlockEntity) blockEntity).isNearFire();
        }
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestBlockEntity) {
            return ((AntNestBlockEntity) blockEntity).isNotFullOfAnts();
        } else {
            return false;
        }
    }

    public boolean hasNestPos() {
        return this.getNestPos() != null;
    }
    public BlockPos getNestPos() {
        return this.dataTracker.get(NEST_POS);
    }
    public void setNestPos(BlockPos nestPos) {
        this.dataTracker.set(NEST_POS, nestPos);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (!this.world.isClient) {
            if (this.cannotEnterNestTicks > 0) {
                this.cannotEnterNestTicks--;
            }

            if (this.ticksLeftToFindNest > 0) {
                this.ticksLeftToFindNest--;
            }

            if (this.age % 20 == 0 && !this.isNestValid()) {
                this.setNestPos(null);
            }
        }
    }

    private boolean isNestValid() {
        if (!this.hasNestPos()) {
            return false;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.getNestPos());
            return blockEntity != null && blockEntity.getType() == RYSBlockEntities.ANT_NEST;
        }
    }

    protected boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return MobEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(RYSItemTags.ANT_TEMPTERS);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return RYSSoundEvents.ENTITY_ANT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.5F : dimensions.height * 0.5F;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    protected boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), distance);
    }

    class EnterNestGoal extends Goal {
        private EnterNestGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            if (AbstractAntEntity.this.hasNestPos() && AbstractAntEntity.this.canEnterNest() && AbstractAntEntity.this.getNestPos().isWithinDistance(AbstractAntEntity.this.getPos(), 2.0D)) {
                BlockEntity blockEntity = AbstractAntEntity.this.world.getBlockEntity(AbstractAntEntity.this.getNestPos());
                if (blockEntity instanceof AntNestBlockEntity) {
                    AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                    if (AntNestBlockEntity.isNotFullOfAnts()) {
                        return true;
                    }

                    AbstractAntEntity.this.setNestPos(null);
                }
            }

            return false;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = AbstractAntEntity.this.world.getBlockEntity(AbstractAntEntity.this.getNestPos());
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                AntNestBlockEntity.tryEnterNest(AbstractAntEntity.this);
            }
        }
    }

    class FindNestGoal extends Goal {
        private FindNestGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.ticksLeftToFindNest == 0 && !AbstractAntEntity.this.hasNestPos() && AbstractAntEntity.this.canEnterNest();
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            AbstractAntEntity.this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeNests();
            if (!list.isEmpty()) {
                Iterator<BlockPos> iBlockPos = list.iterator();

                BlockPos blockPos;
                do {
                    if (!iBlockPos.hasNext()) {
                        AbstractAntEntity.this.moveToNestGoal.clearPossibleNests();
                        AbstractAntEntity.this.setNestPos(list.get(0));
                        return;
                    }

                    blockPos = iBlockPos.next();
                } while (AbstractAntEntity.this.moveToNestGoal.isPossibleNest(blockPos));

                AbstractAntEntity.this.setNestPos(blockPos);
            }
        }

        private List<BlockPos> getNearbyFreeNests() {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) AbstractAntEntity.this.world)
                .getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle((pointOfInterestType) -> pointOfInterestType == RYSPointOfInterests.ANT_NEST, blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).filter(AbstractAntEntity.this::doesNestHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.getSquaredDistance(blockPos))).collect(Collectors.toList());
        }
    }

    public class MoveToAntNestGoal extends Goal {
        private int ticks;
        private final List<BlockPos> possibleNests;
        private Path path;
        private int ticksUntilLost;

        private MoveToAntNestGoal() {
            super();
            this.ticks = AbstractAntEntity.this.world.random.nextInt(10);
            this.possibleNests = Lists.newArrayList();
            this.path = null;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.hasNestPos() && AbstractAntEntity.this.canEnterNest() && !this.isCloseEnough(AbstractAntEntity.this.getNestPos()) && AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.getNestPos()).isOf(RYSBlocks.ANT_NEST);
        }

        @Override
        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.navigation.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            if (AbstractAntEntity.this.hasNestPos()) {
                this.ticks++;
                if (this.ticks > 600) {
                    this.makeChosenNestPossibleNest();
                } else if (!AbstractAntEntity.this.navigation.isFollowingPath()) {
                    if (!AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.getNestPos(), 16)) {
                        if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.getNestPos())) {
                            this.setLost();
                        } else {
                            AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.getNestPos());
                        }
                    } else {
                        boolean bl = this.startMovingToFar(AbstractAntEntity.this.getNestPos());
                        if (!bl) {
                            this.makeChosenNestPossibleNest();
                        } else if (this.path != null
                            && Objects.requireNonNull(AbstractAntEntity.this.navigation.getCurrentPath()).equalsPath(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = AbstractAntEntity.this.navigation.getCurrentPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            AbstractAntEntity.this.navigation.setRangeMultiplier(10.0F);
            AbstractAntEntity.this.navigation.startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
            return AbstractAntEntity.this.navigation.getCurrentPath() != null && AbstractAntEntity.this.navigation.getCurrentPath().reachesTarget();
        }

        private boolean isPossibleNest(BlockPos pos) {
            return this.possibleNests.contains(pos);
        }

        private void addPossibleNest(BlockPos pos) {
            this.possibleNests.add(pos);

            while (this.possibleNests.size() > 3) {
                this.possibleNests.remove(0);
            }

        }

        private void clearPossibleNests() {
            this.possibleNests.clear();
        }

        private void makeChosenNestPossibleNest() {
            if (AbstractAntEntity.this.hasNestPos()) {
                this.addPossibleNest(AbstractAntEntity.this.getNestPos());
            }

            this.setLost();
        }

        private void setLost() {
            AbstractAntEntity.this.setNestPos(null);
            AbstractAntEntity.this.ticksLeftToFindNest = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            if (AbstractAntEntity.this.isWithinDistance(pos, 2)) {
                return true;
            } else {
                Path path = AbstractAntEntity.this.navigation.getCurrentPath();
                return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
            }
        }
    }
}
