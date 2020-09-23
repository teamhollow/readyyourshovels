package net.teamhollow.readyyourshovels.entity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.init.RYSBlockTags;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSItemTags;
import net.teamhollow.readyyourshovels.init.RYSPointOfInterests;

public abstract class AbstractAntEntity extends AnimalEntity {
    private static final TrackedData<Byte> multipleByteTracker;
    private float currentPitch;
    private float lastPitch;
    private int ticksSinceResourcePickup;
    private int cannotEnterNestTicks;
    private int cropsGrownSinceResourcePickup;
    private int ticksLeftToFindNest = 0;
    private int ticksUntilCanResourcePickup = 0;
    private BlockPos resourcePos = null;
    private BlockPos nestPos = null;
    private AbstractAntEntity.ResourcePickupGoal resourcePickupGoal;
    private AbstractAntEntity.MoveToAntNestGoal moveToNestGoal;
    private AbstractAntEntity.MoveToResourceGoal moveToResourceGoal;
    private int ticksInsideWater;

    public AbstractAntEntity(EntityType<? extends AbstractAntEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(multipleByteTracker, (byte) 0);
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AbstractAntEntity.EnterNestGoal());
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(RYSItemTags.ANT_TEMPTERS), false));
        this.resourcePickupGoal = new AbstractAntEntity.ResourcePickupGoal();
        this.goalSelector.add(4, this.resourcePickupGoal);
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new AbstractAntEntity.FindNestGoal());
        this.moveToNestGoal = new AbstractAntEntity.MoveToAntNestGoal();
        this.goalSelector.add(5, this.moveToNestGoal);
        this.moveToResourceGoal = new AbstractAntEntity.MoveToResourceGoal();
        this.goalSelector.add(6, this.moveToResourceGoal);
        this.goalSelector.add(8, new AbstractAntEntity.AntWanderAroundGoal());
        this.goalSelector.add(9, new SwimGoal(this));
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        if (this.hasNest()) {
            tag.put("NestPos", NbtHelper.fromBlockPos(this.getNestPos()));
        }

        if (this.hasResource()) {
            tag.put("ResourcePos", NbtHelper.fromBlockPos(this.getResourcePos()));
        }

        tag.putBoolean("HasResource", this.hasResource());
        tag.putInt("TicksSinceResourcePickup", this.ticksSinceResourcePickup);
        tag.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        tag.putInt("CropsGrownSinceResourcePickup", this.cropsGrownSinceResourcePickup);
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        this.nestPos = null;
        if (tag.contains("NestPos")) {
            this.nestPos = NbtHelper.toBlockPos(tag.getCompound("NestPos"));
        }

        this.resourcePos = null;
        if (tag.contains("ResourcePos")) {
            this.resourcePos = NbtHelper.toBlockPos(tag.getCompound("ResourcePos"));
        }

        super.readCustomDataFromTag(tag);
        this.setHasResource(tag.getBoolean("HasResource"));
        this.ticksSinceResourcePickup = tag.getInt("TicksSinceResourcePickup");
        this.cannotEnterNestTicks = tag.getInt("CannotEnterNestTicks");
        this.cropsGrownSinceResourcePickup = tag.getInt("CropsGrownSinceResourcePickup");
    }

    public void tick() {
        super.tick();
        if (this.hasResource() && this.getCropsGrownSinceResourcePickup() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.world, this.getX() - 0.30000001192092896D, this.getX() + 0.30000001192092896D, this.getZ() - 0.30000001192092896D, this.getZ() + 0.30000001192092896D, this.getBodyY(0.5D), ParticleTypes.FALLING_NECTAR);
            }
        }

        this.updateBodyPitch();
    }

    private void addParticle(World world, double lastX, double x, double lastZ, double z, double y,
            ParticleEffect effect) {
        world.addParticle(effect, MathHelper.lerp(world.random.nextDouble(), lastX, x), y,
                MathHelper.lerp(world.random.nextDouble(), lastZ, z), 0.0D, 0.0D, 0.0D);
    }

    private void startMovingTo(BlockPos pos) {
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

    public BlockPos getResourcePos() {
        return this.resourcePos;
    }

    public void setResourcePos(BlockPos pos) {
        this.resourcePos = pos;
    }

    private boolean failedResourcePickupTooLong() {
        return this.ticksSinceResourcePickup > 3600;
    }

    private boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && this.getTarget() == null) {
            boolean bl = this.failedResourcePickupTooLong() || this.world.isRaining() || this.world.isNight() || this.hasResource();
            return bl && !this.isNestNearFire();
        } else {
            return false;
        }
    }

    public void setCannotEnterNestTicks(int ticks) {
        this.cannotEnterNestTicks = ticks;
    }

    @Environment(EnvType.CLIENT)
    public float getBodyPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.lastPitch, this.currentPitch);
    }

    private void updateBodyPitch() {
        this.lastPitch = this.currentPitch;
        this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
    }

    protected void mobTick() {
        if (this.isInsideWaterOrBubbleColumn()) {
            ++this.ticksInsideWater;
        } else {
            this.ticksInsideWater = 0;
        }

        if (this.ticksInsideWater > 20) {
            this.damage(DamageSource.DROWN, 1.0F);
        }

        if (!this.hasResource()) {
            ++this.ticksSinceResourcePickup;
        }
    }

    public void resetResourcePickupTicks() {
        this.ticksSinceResourcePickup = 0;
    }

    private boolean isNestNearFire() {
        if (this.nestPos == null) {
            return false;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.nestPos);
            return blockEntity instanceof AntNestBlockEntity && ((AntNestBlockEntity) blockEntity).isNearFire();
        }
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof AntNestBlockEntity) {
            return !((AntNestBlockEntity) blockEntity).isFullOfAnts();
        } else {
            return false;
        }
    }

    public boolean hasNest() {
        return this.nestPos != null;
    }

    public BlockPos getNestPos() {
        return this.nestPos;
    }

    private int getCropsGrownSinceResourcePickup() {
        return this.cropsGrownSinceResourcePickup;
    }

    private void resetCropCounter() {
        this.cropsGrownSinceResourcePickup = 0;
    }

    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            if (this.cannotEnterNestTicks > 0) {
                --this.cannotEnterNestTicks;
            }

            if (this.ticksLeftToFindNest > 0) {
                --this.ticksLeftToFindNest;
            }

            if (this.ticksUntilCanResourcePickup > 0) {
                --this.ticksUntilCanResourcePickup;
            }

            if (this.age % 20 == 0 && !this.isNestValid()) {
                this.nestPos = null;
            }
        }

    }

    private boolean isNestValid() {
        if (!this.hasNest()) {
            return false;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.nestPos);
            return blockEntity != null && blockEntity.getType() == RYSBlockEntities.ANT_NEST;
        }
    }

    public boolean hasResource() {
        return this.getAntFlag(8);
    }

    private void setHasResource(boolean hasResource) {
        if (hasResource) {
            this.resetResourcePickupTicks();
        }

        this.setAntFlag(8, hasResource);
    }

    private boolean isTooFar(BlockPos pos) {
        return !this.isWithinDistance(pos, 32);
    }

    private void setAntFlag(int bit, boolean value) {
        if (value) {
            this.dataTracker.set(multipleByteTracker, (byte) ((Byte) this.dataTracker.get(multipleByteTracker) | bit));
        } else {
            this.dataTracker.set(multipleByteTracker, (byte) ((Byte) this.dataTracker.get(multipleByteTracker) & ~bit));
        }

    }

    private boolean getAntFlag(int location) {
        return ((Byte) this.dataTracker.get(multipleByteTracker) & location) != 0;
    }

    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return MobEntity.createMobAttributes();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(RYSItemTags.ANT_TEMPTERS);
    }

    private boolean isResource(BlockPos pos) {
        return this.world.canSetBlock(pos) && this.world.getBlockState(pos).getBlock().isIn(RYSBlockTags.ANT_RESOURCES);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {}

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BEE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BEE_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.5F : dimensions.height * 0.5F;
    }

    public void onResourceDelivered() {
        this.setHasResource(false);
        this.resetCropCounter();
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double) distance);
    }

    static {
        multipleByteTracker = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    class EnterNestGoal extends Goal {
        private EnterNestGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            if (AbstractAntEntity.this.hasNest() && AbstractAntEntity.this.canEnterNest() && AbstractAntEntity.this.nestPos.isWithinDistance(AbstractAntEntity.this.getPos(), 2.0D)) {
                BlockEntity blockEntity = AbstractAntEntity.this.world.getBlockEntity(AbstractAntEntity.this.nestPos);
                if (blockEntity instanceof AntNestBlockEntity) {
                    AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                    if (!AntNestBlockEntity.isFullOfAnts()) {
                        return true;
                    }

                    AbstractAntEntity.this.nestPos = null;
                }
            }

            return false;
        }

        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = AbstractAntEntity.this.world.getBlockEntity(AbstractAntEntity.this.nestPos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                AntNestBlockEntity.tryEnterNest(AbstractAntEntity.this, AbstractAntEntity.this.hasResource());
            }

        }
    }

    class FindNestGoal extends Goal {
        private FindNestGoal() {
            super();
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.ticksLeftToFindNest == 0 && !AbstractAntEntity.this.hasNest() && AbstractAntEntity.this.canEnterNest();
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            AbstractAntEntity.this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeNests();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        AbstractAntEntity.this.moveToNestGoal.clearPossibleNests();
                        AbstractAntEntity.this.nestPos = (BlockPos) list.get(0);
                        return;
                    }

                    blockPos = (BlockPos) var2.next();
                } while (AbstractAntEntity.this.moveToNestGoal.isPossibleNest(blockPos));

                AbstractAntEntity.this.nestPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeNests() {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) AbstractAntEntity.this.world)
                    .getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle((pointOfInterestType) -> {
                return pointOfInterestType == RYSPointOfInterests.ANT_NEST || pointOfInterestType == RYSPointOfInterests.ANT_NEST;
            }, blockPos, 20, PointOfInterestStorage.OccupationStatus.ANY);
            return (List<BlockPos>) stream.map(PointOfInterest::getPos).filter((blockPosx) -> {
                return AbstractAntEntity.this.doesNestHaveSpace(blockPosx);
            }).sorted(Comparator.comparingDouble((blockPos2) -> {
                return blockPos2.getSquaredDistance(blockPos);
            })).collect(Collectors.toList());
        }
    }

    public class MoveToResourceGoal extends Goal {
        private int ticks;

        private MoveToResourceGoal() {
            super();
            this.ticks = AbstractAntEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.resourcePos != null && !AbstractAntEntity.this.hasPositionTarget() && this.shouldMoveToResource() && AbstractAntEntity.this.isResource(AbstractAntEntity.this.resourcePos) && !AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.resourcePos, 2);
        }

        @Override
        public void start() {
            this.ticks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.ticks = 0;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.navigation.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            if (AbstractAntEntity.this.resourcePos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    AbstractAntEntity.this.resourcePos = null;
                } else if (!AbstractAntEntity.this.navigation.isFollowingPath()) {
                    if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.resourcePos)) {
                        AbstractAntEntity.this.resourcePos = null;
                    } else {
                        AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.resourcePos);
                    }
                }
            }
        }

        private boolean shouldMoveToResource() {
            return AbstractAntEntity.this.ticksSinceResourcePickup > 2400;
        }
    }

    public class MoveToAntNestGoal extends Goal {
        private int ticks;
        private List<BlockPos> possibleNests;
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
            return AbstractAntEntity.this.nestPos != null && !AbstractAntEntity.this.hasPositionTarget() && AbstractAntEntity.this.canEnterNest() && !this.isCloseEnough(AbstractAntEntity.this.nestPos) && AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.nestPos).isOf(RYSBlocks.ANT_NEST);
        }

        public void start() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.navigation.resetRangeMultiplier();
        }

        public void tick() {
            if (AbstractAntEntity.this.nestPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    this.makeChosenNestPossibleNest();
                } else if (!AbstractAntEntity.this.navigation.isFollowingPath()) {
                    if (!AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.nestPos, 16)) {
                        if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.nestPos)) {
                            this.setLost();
                        } else {
                            AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.nestPos);
                        }
                    } else {
                        boolean bl = this.startMovingToFar(AbstractAntEntity.this.nestPos);
                        if (!bl) {
                            this.makeChosenNestPossibleNest();
                        } else if (this.path != null
                                && AbstractAntEntity.this.navigation.getCurrentPath().equalsPath(this.path)) {
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
            AbstractAntEntity.this.navigation.startMovingTo((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 1.0D);
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
            if (AbstractAntEntity.this.nestPos != null) {
                this.addPossibleNest(AbstractAntEntity.this.nestPos);
            }

            this.setLost();
        }

        private void setLost() {
            AbstractAntEntity.this.nestPos = null;
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

    class ResourcePickupGoal extends Goal {
        private final Predicate<BlockState> resourcePredicate = (blockState) -> {
            return blockState.isIn(RYSBlockTags.ANT_RESOURCES);
        };
        private int resourcePickupTicks = 0;
        private int lastResourcePickupTick = 0;
        private boolean running;
        private Vec3d nextTarget;
        private int ticks = 0;

        ResourcePickupGoal() {
            super();
        }

        public boolean canStart() {
            if (AbstractAntEntity.this.ticksUntilCanResourcePickup > 0) {
                return false;
            } else if (AbstractAntEntity.this.navigation.isIdle()) {
                return false;
            } else if (AbstractAntEntity.this.hasResource()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (AbstractAntEntity.this.random.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getResource();
                if (optional.isPresent()) {
                    AbstractAntEntity.this.resourcePos = (BlockPos) optional.get();
                    AbstractAntEntity.this.navigation.startMovingTo((double) AbstractAntEntity.this.resourcePos.getX() + 0.5D, (double) AbstractAntEntity.this.resourcePos.getY() + 1.0D, (double) AbstractAntEntity.this.resourcePos.getZ() + 0.5D, 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean shouldContinue() {
            if (!this.running) {
                return false;
            } else if (AbstractAntEntity.this.hasResource()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (this.completedPickup()) {
                return AbstractAntEntity.this.random.nextFloat() < 0.2F;
            } else if (AbstractAntEntity.this.age % 20 == 0 && !AbstractAntEntity.this.isResource(AbstractAntEntity.this.resourcePos)) {
                AbstractAntEntity.this.resourcePos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean completedPickup() {
            return this.resourcePickupTicks > 400;
        }

        public void start() {
            this.resourcePickupTicks = 0;
            this.ticks = 0;
            this.lastResourcePickupTick = 0;
            this.running = true;
            AbstractAntEntity.this.resetResourcePickupTicks();
        }

        public void stop() {
            if (this.completedPickup()) {
                AbstractAntEntity.this.setHasResource(true);
            }

            this.running = false;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.ticksUntilCanResourcePickup = 200;
        }

        public void tick() {
            this.ticks++;
            if (this.ticks > 600) {
                AbstractAntEntity.this.resourcePos = null;
            } else if (AbstractAntEntity.this.resourcePos != null) {
                Vec3d vec3d = Vec3d.ofBottomCenter(AbstractAntEntity.this.resourcePos).add(0.0D, 1.0D, 0.0D);
                if (this.nextTarget == null) {
                    this.nextTarget = vec3d;
                }

                boolean isWithinRangeOfTarget = AbstractAntEntity.this.getPos().distanceTo(this.nextTarget) <= 0.1D;
                if (!isWithinRangeOfTarget) {
                    if (!AbstractAntEntity.this.isNavigating()) AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.resourcePos);

                    if (this.ticks > 600)
                        AbstractAntEntity.this.resourcePos = null;
                    else {
                        if (isWithinRangeOfTarget) {
                            AbstractAntEntity.this.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                        }

                        this.resourcePickupTicks++;
                        if (AbstractAntEntity.this.random.nextFloat() < 0.05F && this.resourcePickupTicks > this.lastResourcePickupTick + 60) {
                            this.lastResourcePickupTick = this.resourcePickupTicks;
                            AbstractAntEntity.this.playSound(SoundEvents.ENTITY_BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

        private Optional<BlockPos> getResource() {
            return this.findResource(this.resourcePredicate, 5.0D);
        }

        private Optional<BlockPos> findResource(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int i = 0; (double) i <= searchDistance; i = i > 0 ? -i : 1 - i) {
                for (int j = 0; (double) j < searchDistance; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutable.set((Vec3i) blockPos, k, i - 1, l);
                            if (blockPos.isWithinDistance(mutable, searchDistance) & predicate.test(AbstractAntEntity.this.world.getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }

    class AntWanderAroundGoal extends Goal {
        AntWanderAroundGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            return AbstractAntEntity.this.navigation.isIdle() && AbstractAntEntity.this.random.nextInt(10) == 0;
        }

        public boolean shouldContinue() {
            return AbstractAntEntity.this.navigation.isFollowingPath();
        }

        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                AbstractAntEntity.this.navigation.startMovingAlong(
                        AbstractAntEntity.this.navigation.findPathTo((BlockPos) (new BlockPos(vec3d)), 1), 1.0D);
            }

        }

        private Vec3d getRandomLocation() {
            Vec3d vec3d3;
            if (AbstractAntEntity.this.isNestValid() && !AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.nestPos, 22)) {
                Vec3d vec3d = Vec3d.ofCenter(AbstractAntEntity.this.nestPos);
                vec3d3 = vec3d.subtract(AbstractAntEntity.this.getPos()).normalize();
            } else {
                vec3d3 = AbstractAntEntity.this.getRotationVec(0.0F);
            }

            Vec3d vec3d4 = TargetFinder.findAirTarget(AbstractAntEntity.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
            return vec3d4 != null ? vec3d4 : TargetFinder.findGroundTarget(AbstractAntEntity.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
        }
    }
}
