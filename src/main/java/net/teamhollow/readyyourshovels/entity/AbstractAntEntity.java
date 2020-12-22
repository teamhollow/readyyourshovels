package net.teamhollow.readyyourshovels.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.teamhollow.readyyourshovels.registry.RYSBlocks;
import net.teamhollow.readyyourshovels.registry.RYSPointOfInterests;
import net.teamhollow.readyyourshovels.registry.RYSTileEntities;
import net.teamhollow.readyyourshovels.tileentity.AntNestTileEntity;
import net.teamhollow.readyyourshovels.registry.RYSSoundEvents;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAntEntity extends AnimalEntity {
    private static final DataParameter<Byte> multipleByteTracker = EntityDataManager.createKey(AbstractAntEntity.class, DataSerializers.BYTE);
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

    protected AbstractAntEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(multipleByteTracker, (byte) 0);
    }


    @Override
    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isIn(RYSBlockTags.ANT_RESOURCES) || blockState.isIn(RYSBlocks.ANT_NEST) ? 10.0F : 0.0F;
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AbstractAntEntity.EnterNestGoal());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(RYSItemTags.ANT_TEMPTERS), false));
        this.resourcePickupGoal = new AbstractAntEntity.ResourcePickupGoal();
        this.goalSelector.addGoal(4, this.resourcePickupGoal);
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new AbstractAntEntity.FindNestGoal());
        this.moveToNestGoal = new AbstractAntEntity.MoveToAntNestGoal();
        this.goalSelector.addGoal(5, this.moveToNestGoal);
        this.moveToResourceGoal = new AbstractAntEntity.MoveToResourceGoal();
        this.goalSelector.addGoal(6, this.moveToResourceGoal);
        this.goalSelector.addGoal(8, new AbstractAntEntity.AntWanderAroundGoal());

    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.hasNest()) {
            compound.put("NestPos", NBTUtil.writeBlockPos(this.getNestPos()));
        }

        if (this.hasResource()) {
            compound.put("ResourcePos", NBTUtil.writeBlockPos(this.getResourcePos()));
        }

        compound.putBoolean("HasResource", this.hasResource());
        compound.putInt("TicksSinceResourcePickup", this.ticksSinceResourcePickup);
        compound.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        compound.putInt("CropsGrownSinceResourcePickup", this.cropsGrownSinceResourcePickup);
    }

    public void readAdditional(CompoundNBT compound) {
        this.nestPos = null;
        if (compound.contains("NestPos")) {
            this.nestPos = NBTUtil.readBlockPos(compound.getCompound("NestPos"));
        }

        this.resourcePos = null;
        if (compound.contains("ResourcePos")) {
            this.resourcePos = NBTUtil.readBlockPos(compound.getCompound("ResourcePos"));
        }

        super.readAdditional(compound);
        this.setHasResource(compound.getBoolean("HasResource"));
        this.ticksSinceResourcePickup = compound.getInt("TicksSinceResourcePickup");
        this.cannotEnterNestTicks = compound.getInt("CannotEnterNestTicks");
        this.cropsGrownSinceResourcePickup = compound.getInt("CropsGrownSinceResourcePickup");
    }

    public void tick() {
        super.tick();
        if (this.hasResource() && this.getCropsGrownSinceResourcePickup() < 10 && this.rand.nextFloat() < 0.05F) {
            for (int i = 0; i < this.rand.nextInt(2) + 1; ++i) {
                this.addParticle(this.world, this.getPosX() - 0.30000001192092896D, this.getPosX() + 0.30000001192092896D, this.getPosZ() - 0.30000001192092896D, this.getPosZ() + 0.30000001192092896D, this.getPosYHeight(0.5F), ParticleTypes.FALLING_NECTAR);
            }
        }

        this.updateBodyPitch();
    }

    private void addParticle(World world, double lastX, double x, double lastZ, double z, double y,
                             IParticleData effect) {
        world.addParticle(effect, MathHelper.lerp(world.rand.nextDouble(), lastX, x), y,
                MathHelper.lerp(world.rand.nextDouble(), lastZ, z), 0.0D, 0.0D, 0.0D);
    }

    private void startMovingTo(BlockPos pos) {
        this.navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.5D);
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
        if (this.cannotEnterNestTicks <= 0 && this.getAttackTarget() == null) {
            boolean bl = this.failedResourcePickupTooLong() || this.world.isRaining() || this.world.isNightTime() || this.hasResource();
            return bl && !this.isNestNearFire();
        } else {
            return false;
        }
    }

    public void setCannotEnterNestTicks(int ticks) {
        this.cannotEnterNestTicks = ticks;
    }

    @OnlyIn(Dist.CLIENT)
    public float getBodyPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.lastPitch, this.currentPitch);
    }

    private void updateBodyPitch() {
        this.lastPitch = this.currentPitch;
        this.currentPitch = Math.max(0.0F, this.currentPitch - 0.24F);
    }

    protected void mobTick() {
        if (this.isInWaterRainOrBubbleColumn()) {
            ++this.ticksInsideWater;
        } else {
            this.ticksInsideWater = 0;
        }

        if (this.ticksInsideWater > 20) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
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
            TileEntity blockEntity = this.world.getTileEntity(this.nestPos);
            return blockEntity instanceof AntNestTileEntity && ((AntNestTileEntity) blockEntity).isNearFire();
        }
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        TileEntity blockEntity = this.world.getTileEntity(pos);
        if (blockEntity instanceof AntNestTileEntity) {
            return !((AntNestTileEntity) blockEntity).isFullOfAnts();
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

    public void updateAITasks() {
        super.updateAITasks();
        if (!this.world.isRemote) {
            if (this.cannotEnterNestTicks > 0) {
                --this.cannotEnterNestTicks;
            }

            if (this.ticksLeftToFindNest > 0) {
                --this.ticksLeftToFindNest;
            }

            if (this.ticksUntilCanResourcePickup > 0) {
                --this.ticksUntilCanResourcePickup;
            }

            if (this.ticksExisted % 20 == 0 && !this.isNestValid()) {
                this.nestPos = null;
            }
        }

    }

    private boolean isNestValid() {
        if (!this.hasNest()) {
            return false;
        } else {
            TileEntity blockEntity = this.world.getTileEntity(this.nestPos);
            return blockEntity != null && blockEntity.getType() == RYSTileEntities.ANT_NEST;
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
        return !this.isWithinHomeDistanceFromPosition(pos, 32);
    }

    private void setAntFlag(int bit, boolean value) {
        if (value) {
            this.dataManager.set(multipleByteTracker, (byte) (this.dataManager.get(multipleByteTracker) | bit));
        } else {
            this.dataManager.set(multipleByteTracker, (byte) (this.dataManager.get(multipleByteTracker) & ~bit));
        }

    }

    private boolean getAntFlag(int location) {
        return (this.dataManager.get(multipleByteTracker) & location) != 0;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().isIn(RYSItemTags.ANT_TEMPTERS);
    }

    private boolean isResource(BlockPos pos) {
        return this.world.isBlockPresent(pos) && this.world.getBlockState(pos).getBlock().isIn(RYSBlockTags.ANT_RESOURCES);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    protected SoundEvent getAmbientSound() {
        return RYSSoundEvents.ENTITY_GARDEN_ANT_AMBIENT;
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

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isChild() ? sizeIn.height * 0.5F : sizeIn.height * 0.5F;
    }

    public void onResourceDelivered() {
        this.setHasResource(false);
        this.resetCropCounter();
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    private boolean isWithinHomeDistanceFromPosition(BlockPos pos, int distance) {
        return pos.withinDistance(this.getPosition(), distance);
    }

    class EnterNestGoal extends Goal {
        private EnterNestGoal() {
            super();
        }

        @Override
        public boolean shouldExecute() {
            if (AbstractAntEntity.this.hasNest() && AbstractAntEntity.this.canEnterNest() && AbstractAntEntity.this.nestPos.withinDistance(AbstractAntEntity.this.getPositionVec(), 30.0D)) {
                if (!AbstractAntEntity.this.nestPos.withinDistance(AbstractAntEntity.this.getPositionVec(), 2.0D)) {
                    if (AbstractAntEntity.this.navigator.noPath()) AbstractAntEntity.this.startMovingTo(nestPos);
                    return false;
                }
                TileEntity blockEntity = AbstractAntEntity.this.world.getTileEntity(AbstractAntEntity.this.nestPos);
                if (blockEntity instanceof AntNestTileEntity) {
                    AntNestTileEntity AntNestTileEntity = (net.teamhollow.readyyourshovels.tileentity.AntNestTileEntity) blockEntity;
                    if (!AntNestTileEntity.isFullOfAnts()) {
                        return true;
                    }

                    AbstractAntEntity.this.nestPos = null;
                }
            }

            return false;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            TileEntity blockEntity = AbstractAntEntity.this.world.getTileEntity(AbstractAntEntity.this.nestPos);
            if (blockEntity instanceof AntNestTileEntity) {
                AntNestTileEntity AntNestTileEntity = (AntNestTileEntity) blockEntity;
                AntNestTileEntity.tryEnterNest(AbstractAntEntity.this, AbstractAntEntity.this.hasResource());
            }
        }
    }

    class FindNestGoal extends Goal {
        private FindNestGoal() {
            super();
        }

        @Override
        public boolean shouldExecute() {
            return AbstractAntEntity.this.ticksLeftToFindNest == 0 && !AbstractAntEntity.this.hasNest() && AbstractAntEntity.this.canEnterNest();
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            AbstractAntEntity.this.ticksLeftToFindNest = 200;
            List<BlockPos> list = this.getNearbyFreeNests();
            if (!list.isEmpty()) {
                Iterator<BlockPos> var2 = list.iterator();

                BlockPos blockPos;
                do {
                    if (!var2.hasNext()) {
                        AbstractAntEntity.this.moveToNestGoal.clearPossibleNests();
                        AbstractAntEntity.this.nestPos = list.get(0);
                        return;
                    }

                    blockPos = var2.next();
                } while (AbstractAntEntity.this.moveToNestGoal.isPossibleNest(blockPos));

                AbstractAntEntity.this.nestPos = blockPos;
            }
        }

        private List<BlockPos> getNearbyFreeNests() {
            BlockPos blockPos = AbstractAntEntity.this.getPosition();
            PointOfInterestManager pointOfInterestManager = ((ServerWorld) AbstractAntEntity.this.world)
                    .getPointOfInterestManager();
            Stream<PointOfInterest> stream = pointOfInterestManager.func_219146_b((pointOfInterestType) -> pointOfInterestType == RYSPointOfInterests.ANT_NEST || pointOfInterestType == RYSPointOfInterests.ANT_NEST, blockPos, 20, PointOfInterestManager.Status.ANY);
            return stream.map(PointOfInterest::getPos).filter((blockPosx) -> AbstractAntEntity.this.doesNestHaveSpace(blockPosx)).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.distanceSq(blockPos))).collect(Collectors.toList());
        }
    }

    public class MoveToResourceGoal extends Goal {
        private int ticks;

        private MoveToResourceGoal() {
            super();
            this.ticks = AbstractAntEntity.this.world.rand.nextInt(10);
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return AbstractAntEntity.this.resourcePos != null && !AbstractAntEntity.this.hasPath() && this.shouldMoveToResource() && AbstractAntEntity.this.isResource(AbstractAntEntity.this.resourcePos) && !AbstractAntEntity.this.isWithinHomeDistanceFromPosition(AbstractAntEntity.this.resourcePos, 2);
        }

        @Override
        public void startExecuting() {
            this.ticks = 0;
            super.startExecuting();
        }

        @Override
        public void resetTask() {
            this.ticks = 0;
            AbstractAntEntity.this.navigator.clearPath();
            AbstractAntEntity.this.navigator.resetRangeMultiplier();
        }

        @Override
        public void tick() {
            if (AbstractAntEntity.this.resourcePos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    AbstractAntEntity.this.resourcePos = null;
                } else if (!AbstractAntEntity.this.navigator.hasPath()) {
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
            this.ticks = AbstractAntEntity.this.world.rand.nextInt(10);
            this.possibleNests = Lists.newArrayList();
            this.path = null;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return AbstractAntEntity.this.nestPos != null && !AbstractAntEntity.this.hasPath() && AbstractAntEntity.this.canEnterNest() && !this.isCloseEnough(AbstractAntEntity.this.nestPos) && AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.nestPos).isIn(RYSBlocks.ANT_NEST);
        }

        public void startExecuting() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            super.startExecuting();
        }

        public void resetTask() {
            this.ticks = 0;
            this.ticksUntilLost = 0;
            AbstractAntEntity.this.navigator.clearPath();
            AbstractAntEntity.this.navigator.resetRangeMultiplier();
        }

        public void tick() {
            if (AbstractAntEntity.this.nestPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    this.makeChosenNestPossibleNest();
                } else if (!AbstractAntEntity.this.navigator.hasPath()) {
                    if (!AbstractAntEntity.this.isWithinHomeDistanceFromPosition(AbstractAntEntity.this.nestPos, 16)) {
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
                                && AbstractAntEntity.this.navigator.getPath().isSamePath(this.path)) {
                            ++this.ticksUntilLost;
                            if (this.ticksUntilLost > 60) {
                                this.setLost();
                                this.ticksUntilLost = 0;
                            }
                        } else {
                            this.path = AbstractAntEntity.this.navigator.getPath();
                        }

                    }
                }
            }
        }

        private boolean startMovingToFar(BlockPos pos) {
            AbstractAntEntity.this.navigator.setRangeMultiplier(10.0F);
            AbstractAntEntity.this.navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
            return AbstractAntEntity.this.navigator.getPath() != null && AbstractAntEntity.this.navigator.getPath().reachesTarget();
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
            if (AbstractAntEntity.this.isWithinHomeDistanceFromPosition(pos, 2)) {
                return true;
            } else {
                Path path = AbstractAntEntity.this.navigator.getPath();
                return path != null && path.getTarget().equals(pos) && path.reachesTarget() && path.isFinished();
            }
        }
    }

    class ResourcePickupGoal extends Goal {
        private final Predicate<BlockState> resourcePredicate = (blockState) -> blockState.isIn(RYSBlockTags.ANT_RESOURCES);
        private int resourcePickupTicks = 0;
        private int lastResourcePickupTick = 0;
        private boolean running;
        private Vector3d nextTarget;
        private int ticks = 0;

        ResourcePickupGoal() {
            super();
        }

        public boolean shouldExecute() {
            if (AbstractAntEntity.this.ticksUntilCanResourcePickup > 0) {
                return false;
            } else if (AbstractAntEntity.this.navigator.noPath()) {
                return false;
            } else if (AbstractAntEntity.this.hasResource()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (AbstractAntEntity.this.rand.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional = this.getResource();
                if (optional.isPresent()) {
                    AbstractAntEntity.this.resourcePos = optional.get();
                    AbstractAntEntity.this.navigator.tryMoveToXYZ((double) AbstractAntEntity.this.resourcePos.getX() + 0.5D, (double) AbstractAntEntity.this.resourcePos.getY() + 1.0D, (double) AbstractAntEntity.this.resourcePos.getZ() + 0.5D, 1.2000000476837158D);
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            if (!this.running) {
                return false;
            } else if (AbstractAntEntity.this.hasResource()) {
                return false;
            } else if (AbstractAntEntity.this.world.isRaining()) {
                return false;
            } else if (this.completedPickup()) {
                return AbstractAntEntity.this.rand.nextFloat() < 0.2F;
            } else if (AbstractAntEntity.this.ticksExisted % 20 == 0 && !AbstractAntEntity.this.isResource(AbstractAntEntity.this.resourcePos)) {
                AbstractAntEntity.this.resourcePos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean completedPickup() {
            return this.resourcePickupTicks > 400;
        }

        public void startExecuting() {
            this.resourcePickupTicks = 0;
            this.ticks = 0;
            this.lastResourcePickupTick = 0;
            this.running = true;
            AbstractAntEntity.this.resetResourcePickupTicks();
        }

        public void resetTask() {
            if (this.completedPickup()) {
                BlockState blockState = AbstractAntEntity.this.world.getBlockState(AbstractAntEntity.this.resourcePos);
                AbstractAntEntity.this.world.removeBlock(AbstractAntEntity.this.resourcePos, false);
                AbstractAntEntity.this.world.setBlockState(AbstractAntEntity.this.resourcePos, blockState);
                AbstractAntEntity.this.setHasResource(true);
            }

            this.running = false;
            AbstractAntEntity.this.navigator.clearPath();
            AbstractAntEntity.this.ticksUntilCanResourcePickup = 200;
        }

        public void tick() {
            this.ticks++;
            if (this.ticks > 600) {
                AbstractAntEntity.this.resourcePos = null;
            } else if (AbstractAntEntity.this.resourcePos != null) {
                Vector3d vec3d = Vector3d.copyCenteredHorizontally(AbstractAntEntity.this.resourcePos).add(0.0D, 1.0D, 0.0D);
                if (this.nextTarget == null) {
                    this.nextTarget = vec3d;
                }

                boolean isWithinRangeOfTarget = AbstractAntEntity.this.getPositionVec().distanceTo(this.nextTarget) <= 0.1D;
                if (!isWithinRangeOfTarget) {
                    if (!AbstractAntEntity.this.isAIDisabled())
                        AbstractAntEntity.this.startMovingTo(AbstractAntEntity.this.resourcePos);

                    if (this.ticks > 600)
                        AbstractAntEntity.this.resourcePos = null;
                    else {
                        this.resourcePickupTicks++;
                        if (AbstractAntEntity.this.rand.nextFloat() < 0.05F && this.resourcePickupTicks > this.lastResourcePickupTick + 60) {
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
            BlockPos blockPos = AbstractAntEntity.this.getPosition();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for (int i = 0; (double) i <= searchDistance; i = i > 0 ? -i : 1 - i) {
                for (int j = 0; (double) j < searchDistance; ++j) {
                    for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for (int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutable.setAndOffset(blockPos, k, i - 1, l);
                            if (blockPos.withinDistance(mutable, searchDistance) & predicate.test(AbstractAntEntity.this.world.getBlockState(mutable))) {
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
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return AbstractAntEntity.this.navigator.noPath() && AbstractAntEntity.this.rand.nextInt(10) == 0;
        }

        public boolean shouldContinueExecuting() {
            return AbstractAntEntity.this.navigator.hasPath();
        }

        public void startExecuting() {
            Vector3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                AbstractAntEntity.this.navigator.setPath(
                        AbstractAntEntity.this.navigator.getPathToPos(new BlockPos(vec3d), 1), 1.0D);
            }

        }

        private Vector3d getRandomLocation() {
            Vector3d vec3d3;
            if (AbstractAntEntity.this.isNestValid() && !AbstractAntEntity.this.isWithinHomeDistanceFromPosition(AbstractAntEntity.this.nestPos, 22)) {
                Vector3d vec3d = Vector3d.copyCentered(AbstractAntEntity.this.nestPos);
                vec3d3 = vec3d.subtract(AbstractAntEntity.this.getPositionVec()).normalize();
            } else {
                vec3d3 = AbstractAntEntity.this.getUpVector(0.0F);
            }

            Vector3d vec3d4 = RandomPositionGenerator.findAirTarget(AbstractAntEntity.this, 8, 7, vec3d3, 1.5707964F, 2, 1);
            return vec3d4 != null ? vec3d4 : RandomPositionGenerator.findGroundTarget(AbstractAntEntity.this, 8, 4, -2, vec3d3, 1.5707963705062866D);
        }
    }
}
