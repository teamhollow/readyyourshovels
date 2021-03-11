package net.teamhollow.readyyourshovels.entity.ant;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.NoPenaltyTargeting;
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
    public CompoundTag writeNbt(CompoundTag tag) {
        super.writeNbt(tag);

        if (this.hasNestPos()) tag.put("NestPos", NbtHelper.fromBlockPos(this.getNestPos()));
        tag.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);

        return tag;
    }

    @Override
    public void readNbt(CompoundTag tag) {
        super.readNbt(tag);

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
        int range = 10;
        Vec3d vec3d2 = NoPenaltyTargeting.find(this, range, range);
        if (vec3d2 != null) {
            this.navigation.setRangeMultiplier(0.5F);
            this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0D);
        }
    }

    protected boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && this.getPositionTarget() == null || this.world.isRaining() || this.world.isNight() || this.canEnterNestExt()) {
            return this.isNestNotNearFire();
        } else {
            return false;
        }
    }
    protected boolean canEnterNestExt() {
        return true;
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
        return RYSItemTags.ANT_TEMPTERS.contains(stack.getItem());
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    protected SoundEvent getAmbientSound() {
        return RYSSoundEvents.ENTITY_ANT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return RYSSoundEvents.ENTITY_ANT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return RYSSoundEvents.ENTITY_ANT_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.5F : dimensions.height * 0.5F;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() && this.hasNestPos();
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return super.canImmediatelyDespawn(distanceSquared) || !this.hasNestPos();
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
            AbstractAntEntity $this = AbstractAntEntity.this;
            if ($this.hasNestPos() && $this.canEnterNest() && $this.getNestPos().isWithinDistance($this.getPos(), 2.0D)) {
                BlockEntity blockEntity = $this.world.getBlockEntity($this.getNestPos());
                if (blockEntity instanceof AntNestBlockEntity) {
                    AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                    if (AntNestBlockEntity.isNotFullOfAnts()) {
                        return true;
                    }

                    $this.setNestPos(null);
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
            AbstractAntEntity $this = AbstractAntEntity.this;
            BlockEntity blockEntity = $this.world.getBlockEntity($this.getNestPos());
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                AntNestBlockEntity.tryEnterNest($this);
            }
        }
    }

    class FindNestGoal extends Goal {
        private FindNestGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            AbstractAntEntity $this = AbstractAntEntity.this;
            return $this.ticksLeftToFindNest == 0 && !$this.hasNestPos() && $this.canEnterNest();
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            AbstractAntEntity $this = AbstractAntEntity.this;
            $this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeNests();
            if (!list.isEmpty()) {
                Iterator<BlockPos> iBlockPos = list.iterator();

                BlockPos blockPos;
                do {
                    if (!iBlockPos.hasNext()) {
                        $this.moveToNestGoal.clearPossibleNests();
                        $this.setNestPos(list.get(0));
                        return;
                    }

                    blockPos = iBlockPos.next();
                } while ($this.moveToNestGoal.isPossibleNest(blockPos));

                $this.setNestPos(blockPos);
            }
        }

        private List<BlockPos> getNearbyFreeNests() {
            AbstractAntEntity $this = AbstractAntEntity.this;
            BlockPos blockPos = $this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) $this.world).getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle(pointOfInterestType -> pointOfInterestType == RYSPointOfInterests.ANT_NEST, blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).filter($this::doesNestHaveSpace).sorted(Comparator.comparingDouble(blockPos2 -> blockPos2.getSquaredDistance(blockPos))).collect(Collectors.toList());
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
            AbstractAntEntity $this = AbstractAntEntity.this;
            return $this.hasNestPos() && $this.canEnterNest() && !this.isCloseEnough($this.getNestPos()) && $this.world.getBlockState($this.getNestPos()).isOf(RYSBlocks.ANT_NEST);
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
            AbstractAntEntity $this = AbstractAntEntity.this;
            $this.navigation.stop();
            $this.navigation.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            AbstractAntEntity $this = AbstractAntEntity.this;
            if ($this.hasNestPos()) {
                this.ticks++;
                if (this.ticks > 600) {
                    this.makeChosenNestPossibleNest();
                } else if (!$this.navigation.isFollowingPath()) {
                    if (!$this.isWithinDistance($this.getNestPos(), 16)) {
                        if ($this.isTooFar($this.getNestPos())) {
                            this.setLost();
                        } else {
                            $this.startMovingTo($this.getNestPos());
                        }
                    } else {
                        boolean bl = this.startMovingToFar($this.getNestPos());
                        if (!bl) {
                            this.makeChosenNestPossibleNest();
                        } else if (this.path != null
                            && Objects.requireNonNull($this.navigation.getCurrentPath()).equalsPath(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = $this.navigation.getCurrentPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            AbstractAntEntity $this = AbstractAntEntity.this;
            $this.navigation.setRangeMultiplier(10.0F);
            $this.navigation.startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
            return $this.navigation.getCurrentPath() != null && $this.navigation.getCurrentPath().reachesTarget();
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
            AbstractAntEntity $this = AbstractAntEntity.this;
            if ($this.hasNestPos()) {
                this.addPossibleNest($this.getNestPos());
            }

            this.setLost();
        }

        private void setLost() {
            AbstractAntEntity $this = AbstractAntEntity.this;
            $this.setNestPos(null);
            $this.ticksLeftToFindNest = 200;
        }

        private boolean isCloseEnough(BlockPos pos) {
            AbstractAntEntity $this = AbstractAntEntity.this;
            if ($this.isWithinDistance(pos, 2)) {
                return true;
            } else {
                Path path = $this.navigation.getCurrentPath();
                return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
            }
        }
    }
}
