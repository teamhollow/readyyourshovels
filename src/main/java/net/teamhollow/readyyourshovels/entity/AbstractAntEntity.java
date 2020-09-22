package net.teamhollow.readyyourshovels.entity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.Durations;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.LookControl;
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
import net.minecraft.entity.mob.Angerable;
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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.IntRange;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSItemTags;
import net.teamhollow.readyyourshovels.init.RYSPointOfInterests;

public abstract class AbstractAntEntity extends AnimalEntity implements Angerable, Flutterer {
    private static final TrackedData<Byte> multipleByteTracker;
    private static final TrackedData<Integer> anger;
    private static final IntRange field_25363;
    private UUID targetUuid;
    private float currentPitch;
    private float lastPitch;
    private int ticksSincePollination;
    private int cannotEnterNestTicks;
    private int cropsGrownSincePollination;
    private int ticksLeftToFindNest = 0;
    private int ticksUntilCanPollinate = 0;
    private BlockPos flowerPos = null;
    private BlockPos nestPos = null;
    private AbstractAntEntity.PollinateGoal pollinateGoal;
    private AbstractAntEntity.MoveToAntNestGoal moveToNestGoal;
    private AbstractAntEntity.MoveToFlowerGoal moveToFlowerGoal;
    private int ticksInsideWater;

    public AbstractAntEntity(EntityType<? extends AbstractAntEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new AbstractAntEntity.AntLookControl(this);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(multipleByteTracker, (byte) 0);
        this.dataTracker.startTracking(anger, 0);
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
        this.pollinateGoal = new AbstractAntEntity.PollinateGoal();
        this.goalSelector.add(4, this.pollinateGoal);
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new AbstractAntEntity.FindNestGoal());
        this.moveToNestGoal = new AbstractAntEntity.MoveToAntNestGoal();
        this.goalSelector.add(5, this.moveToNestGoal);
        this.moveToFlowerGoal = new AbstractAntEntity.MoveToFlowerGoal();
        this.goalSelector.add(6, this.moveToFlowerGoal);
        this.goalSelector.add(8, new AbstractAntEntity.AntWanderAroundGoal());
        this.goalSelector.add(9, new SwimGoal(this));
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        if (this.hasNest()) {
            tag.put("NestPos", NbtHelper.fromBlockPos(this.getNestPos()));
        }

        if (this.hasFlower()) {
            tag.put("FlowerPos", NbtHelper.fromBlockPos(this.getFlowerPos()));
        }

        tag.putBoolean("HasNectar", this.hasNectar());
        tag.putInt("TicksSincePollination", this.ticksSincePollination);
        tag.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        tag.putInt("CropsGrownSincePollination", this.cropsGrownSincePollination);
        this.angerToTag(tag);
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        this.nestPos = null;
        if (tag.contains("NestPos")) {
            this.nestPos = NbtHelper.toBlockPos(tag.getCompound("NestPos"));
        }

        this.flowerPos = null;
        if (tag.contains("FlowerPos")) {
            this.flowerPos = NbtHelper.toBlockPos(tag.getCompound("FlowerPos"));
        }

        super.readCustomDataFromTag(tag);
        this.setHasNectar(tag.getBoolean("HasNectar"));
        this.ticksSincePollination = tag.getInt("TicksSincePollination");
        this.cannotEnterNestTicks = tag.getInt("CannotEnterNestTicks");
        this.cropsGrownSincePollination = tag.getInt("CropsGrownSincePollination");
        this.angerFromTag((ServerWorld) this.world, tag);
    }

    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.world, this.getX() - 0.30000001192092896D, this.getX() + 0.30000001192092896D,
                        this.getZ() - 0.30000001192092896D, this.getZ() + 0.30000001192092896D, this.getBodyY(0.5D),
                        ParticleTypes.FALLING_NECTAR);
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
        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
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

    public BlockPos getFlowerPos() {
        return this.flowerPos;
    }

    public boolean hasFlower() {
        return this.flowerPos != null;
    }

    public void setFlowerPos(BlockPos pos) {
        this.flowerPos = pos;
    }

    private boolean failedPollinatingTooLong() {
        return this.ticksSincePollination > 3600;
    }

    private boolean canEnterNest() {
        if (this.cannotEnterNestTicks <= 0 && !this.pollinateGoal.isRunning() && this.getTarget() == null) {
            boolean bl = this.failedPollinatingTooLong() || this.world.isRaining() || this.world.isNight()
                    || this.hasNectar();
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
        if (this.isNearTarget()) {
            this.currentPitch = Math.min(1.0F, this.currentPitch + 0.2F);
        } else {
            this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
        }

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

        if (!this.hasNectar()) {
            ++this.ticksSincePollination;
        }

        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld) this.world, false);
        }

    }

    public void resetPollinationTicks() {
        this.ticksSincePollination = 0;
    }

    private boolean isNestNearFire() {
        if (this.nestPos == null) {
            return false;
        } else {
            BlockEntity blockEntity = this.world.getBlockEntity(this.nestPos);
            return blockEntity instanceof AntNestBlockEntity && ((AntNestBlockEntity) blockEntity).isNearFire();
        }
    }

    public int getAngerTime() {
        return (Integer) this.dataTracker.get(anger);
    }

    public void setAngerTime(int ticks) {
        this.dataTracker.set(anger, ticks);
    }

    public UUID getAngryAt() {
        return this.targetUuid;
    }

    public void setAngryAt(UUID uuid) {
        this.targetUuid = uuid;
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(field_25363.choose(this.random));
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

    protected void sendAiDebugData() {
        super.sendAiDebugData();
        // DebugInfoSender.sendAntDebugData(this); TODO
    }

    private int getCropsGrownSincePollination() {
        return this.cropsGrownSincePollination;
    }

    private void resetCropCounter() {
        this.cropsGrownSincePollination = 0;
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

            if (this.ticksUntilCanPollinate > 0) {
                --this.ticksUntilCanPollinate;
            }

            boolean bl = this.hasAngerTime() && this.getTarget() != null
                    && this.getTarget().squaredDistanceTo(this) < 4.0D;
            this.setNearTarget(bl);
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

    public boolean hasNectar() {
        return this.getAntFlag(8);
    }

    private void setHasNectar(boolean hasNectar) {
        if (hasNectar) {
            this.resetPollinationTicks();
        }

        this.setAntFlag(8, hasNectar);
    }

    private boolean isNearTarget() {
        return this.getAntFlag(2);
    }

    private void setNearTarget(boolean nearTarget) {
        this.setAntFlag(2, nearTarget);
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

    private boolean isFlowers(BlockPos pos) {
        return this.world.canSetBlock(pos) && this.world.getBlockState(pos).getBlock().isIn(BlockTags.FLOWERS);
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

    public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

    public void onHoneyDelivered() {
        this.setHasNectar(false);
        this.resetCropCounter();
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.world.isClient) {
                this.pollinateGoal.cancel();
            }

            return super.damage(source, amount);
        }
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    private boolean isWithinDistance(BlockPos pos, int distance) {
        return pos.isWithinDistance(this.getBlockPos(), (double) distance);
    }

    static {
        multipleByteTracker = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.BYTE);
        anger = DataTracker.registerData(AbstractAntEntity.class, TrackedDataHandlerRegistry.INTEGER);
        field_25363 = Durations.betweenSeconds(20, 39);
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

        public boolean canAntContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = AbstractAntEntity.this.world.getBlockEntity(AbstractAntEntity.this.nestPos);
            if (blockEntity instanceof AntNestBlockEntity) {
                AntNestBlockEntity AntNestBlockEntity = (AntNestBlockEntity) blockEntity;
                AntNestBlockEntity.tryEnterNest(AbstractAntEntity.this, AbstractAntEntity.this.hasNectar());
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

        public boolean canAntContinue() {
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

    class PollinateGoal extends Goal {
        private final Predicate<BlockState> flowerPredicate = (blockState) -> {
            if (blockState.isIn(BlockTags.TALL_FLOWERS)) {
                if (blockState.isOf(Blocks.SUNFLOWER)) {
                    return blockState.get(TallPlantBlock.HALF) == DoubleBlockHalf.UPPER;
                } else {
                    return true;
                }
            } else {
                return blockState.isIn(BlockTags.SMALL_FLOWERS);
            }
        };
        private int pollinationTicks = 0;
        private int lastPollinationTick = 0;
        private boolean running;
        private Vec3d nextTarget;
        private int ticks = 0;

        PollinateGoal() {
            super();
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (AbstractAntEntity.this.ticksUntilCanPollinate > 0) {
                return false;
            } else if (AbstractAntEntity.this.hasNectar()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (AbstractAntEntity.this.random.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getFlower();
                if (optional.isPresent()) {
                    AbstractAntEntity.this.flowerPos = (BlockPos) optional.get();
                    AbstractAntEntity.this.navigation.startMovingTo((double) AbstractAntEntity.this.flowerPos.getX() + 0.5D, (double) AbstractAntEntity.this.flowerPos.getY() + 0.5D, (double) AbstractAntEntity.this.flowerPos.getZ() + 0.5D, 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            }
        }

        public boolean canAntContinue() {
            if (!this.running) {
                return false;
            } else if (!AbstractAntEntity.this.hasFlower()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (this.completedPollination()) {
                return AbstractAntEntity.this.random.nextFloat() < 0.2F;
            } else if (AbstractAntEntity.this.age % 20 == 0 && !AbstractAntEntity.this.isFlowers(AbstractAntEntity.this.flowerPos)) {
                AbstractAntEntity.this.flowerPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean completedPollination() {
            return this.pollinationTicks > 400;
        }

        private boolean isRunning() {
            return this.running;
        }

        private void cancel() {
            this.running = false;
        }

        public void start() {
            this.pollinationTicks = 0;
            this.ticks = 0;
            this.lastPollinationTick = 0;
            this.running = true;
            AbstractAntEntity.this.resetPollinationTicks();
        }

        public void stop() {
            if (this.completedPollination()) {
                AbstractAntEntity.this.setHasNectar(true);
            }

            this.running = false;
            AbstractAntEntity.this.navigation.stop();
            AbstractAntEntity.this.ticksUntilCanPollinate = 200;
        }

        public void tick() {
            ++this.ticks;
            if (this.ticks > 600) {
                AbstractAntEntity.this.flowerPos = null;
            } else {
                Vec3d vec3d = Vec3d.ofBottomCenter(AbstractAntEntity.this.flowerPos);
                if (vec3d.distanceTo(AbstractAntEntity.this.getPos()) > 1.0D) {
                    this.nextTarget = vec3d;
                    this.moveToNextTarget();
                } else {
                    if (this.nextTarget == null) {
                        this.nextTarget = vec3d;
                    }

                    boolean bl = AbstractAntEntity.this.getPos().distanceTo(this.nextTarget) <= 0.1D;
                    boolean bl2 = true;
                    if (!bl && this.ticks > 600) {
                        AbstractAntEntity.this.flowerPos = null;
                    } else {
                        if (bl) {
                            boolean bl3 = AbstractAntEntity.this.random.nextInt(25) == 0;
                            if (bl3) {
                                this.nextTarget = new Vec3d(vec3d.getX() + (double) this.getRandomOffset(), vec3d.getY(), vec3d.getZ() + (double) this.getRandomOffset());
                                AbstractAntEntity.this.navigation.stop();
                            } else {
                                bl2 = false;
                            }

                            AbstractAntEntity.this.getLookControl().lookAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
                        }

                        if (bl2) {
                            this.moveToNextTarget();
                        }

                        ++this.pollinationTicks;
                        if (AbstractAntEntity.this.random.nextFloat() < 0.05F
                                && this.pollinationTicks > this.lastPollinationTick + 60) {
                            this.lastPollinationTick = this.pollinationTicks;
                            AbstractAntEntity.this.playSound(SoundEvents.ENTITY_BEE_POLLINATE, 1.0F, 1.0F);
                        }

                    }
                }
            }
        }

        private void moveToNextTarget() {
            AbstractAntEntity.this.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(),
                    this.nextTarget.getZ(), 0.3499999940395355D);
        }

        private float getRandomOffset() {
            return (AbstractAntEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> getFlower() {
            return this.findFlower(this.flowerPredicate, 5.0D);
        }

        private Optional<BlockPos> findFlower(Predicate<BlockState> predicate, double searchDistance) {
            BlockPos blockPos = AbstractAntEntity.this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int i = 0; (double) i <= searchDistance; i = i > 0 ? -i : 1 - i) {
                for (int j = 0; (double) j < searchDistance; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutable.set((Vec3i) blockPos, k, i - 1, l);
                            if (blockPos.isWithinDistance(mutable, searchDistance)
                                    && predicate.test(AbstractAntEntity.this.world.getBlockState(mutable))) {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }

    class AntLookControl extends LookControl {
        AntLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (!AbstractAntEntity.this.hasAngerTime()) {
                super.tick();
            }
        }

        protected boolean shouldStayHorizontal() {
            return !AbstractAntEntity.this.pollinateGoal.isRunning();
        }
    }

    public class MoveToFlowerGoal extends Goal {
        private int ticks;

        private MoveToFlowerGoal() {
            super();
            this.ticks = AbstractAntEntity.this.world.random.nextInt(10);
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return AbstractAntEntity.this.flowerPos != null && !AbstractAntEntity.this.hasPositionTarget() && this.shouldMoveToFlower() && AbstractAntEntity.this.isFlowers(AbstractAntEntity.this.flowerPos) && !AbstractAntEntity.this.isWithinDistance(AbstractAntEntity.this.flowerPos, 2);
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
            if (AbstractAntEntity.this.flowerPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    AbstractAntEntity.this.flowerPos = null;
                } else if (!AbstractAntEntity.this.navigation.isFollowingPath()) {
                    if (AbstractAntEntity.this.isTooFar(AbstractAntEntity.this.flowerPos)) {
                        AbstractAntEntity.this.flowerPos = null;
                    } else {
                        AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.flowerPos);
                    }
                }
            }
        }

        private boolean shouldMoveToFlower() {
            return AbstractAntEntity.this.ticksSincePollination > 2400;
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
