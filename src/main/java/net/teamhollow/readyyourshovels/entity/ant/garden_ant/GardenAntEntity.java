package net.teamhollow.readyyourshovels.entity.ant.garden_ant;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.entity.ant.AbstractAntEntity;
import net.teamhollow.readyyourshovels.entity.ant.ResourceGatherer;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;

public class GardenAntEntity extends AbstractAntEntity implements ResourceGatherer {
    public static final String id = "garden_" + AbstractAntEntity.id;

    private static final TrackedData<BlockPos> RESOURCE_POS = DataTracker.registerData(GardenAntEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Boolean> HAS_RESOURCE = DataTracker.registerData(GardenAntEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int ticksUntilCanResourcePickup = 0;
    private int ticksSinceResourcePickup;
    private ResourceGatherer.ResourcePickupGoal resourcePickupGoal;

    public GardenAntEntity(EntityType<? extends AbstractAntEntity> type, World worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        // this.goalSelector.add(0, new LocateResourceGoal(this));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(RYSItemTags.ANT_TEMPTERS), false));
        // this.goalSelector.add(3, new GardenAntEntity.StoreItemsGoal(this));
        this.resourcePickupGoal = new ResourceGatherer.ResourcePickupGoal(this);
        this.goalSelector.add(4, this.resourcePickupGoal);
        // this.goalSelector.add(6, new MoveToResourceGoal());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(RESOURCE_POS, null);
        this.dataTracker.startTracking(HAS_RESOURCE, false);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putInt("Age", this.age);

        if (this.hasResourcePos()) tag.put("ResourcePos", NbtHelper.fromBlockPos(this.getResourcePos()));

        tag.putBoolean("HasResource", this.hasResource());
        tag.putInt("TicksSinceResourcePickup", this.ticksSinceResourcePickup);
        tag.putBoolean("IsDoingResourcePickup", this.resourcePickupGoal.isRunning());
    }

    public static DefaultAttributeContainer.Builder createAntAttributes() {
        return AbstractAntEntity.createAntAttributes();
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.age = tag.getInt("Age");

        this.setResourcePos(null);
        if (tag.contains("ResourcePos")) this.setResourcePos(NbtHelper.toBlockPos(tag.getCompound("ResourcePos")));

        this.setHasResource(tag.getBoolean("HasResource"));
        this.ticksSinceResourcePickup = tag.getInt("TicksSinceResourcePickup");
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.hasResource() && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.addParticle(this.world, this.getX() - 0.30000001192092896D, this.getX() + 0.30000001192092896D, this.getZ() - 0.30000001192092896D, this.getZ() + 0.30000001192092896D, this.getBodyY(0.5D));
            }
        }

        if (this.ticksUntilCanResourcePickup > 0) {
            --this.ticksUntilCanResourcePickup;
        }
    }

    @Override
    protected void mobTick() {
        if (!this.hasResource()) this.ticksSinceResourcePickup++;
    }

    @Override
    public BlockPos getResourcePos() {
        return this.dataTracker.get(RESOURCE_POS);
    }
    @Override
    public void setResourcePos(BlockPos pos) {
        this.dataTracker.set(RESOURCE_POS, pos);
    }
    @Override
    public boolean hasResourcePos() {
        return this.getResourcePos() != null;
    }

    @Override
    public void setHasResource(boolean hasResource) {
        this.dataTracker.set(HAS_RESOURCE, hasResource);
    }
    @Override
    public boolean hasResource() {
        return this.dataTracker.get(HAS_RESOURCE);
    }

    private boolean failedResourcePickupTooLong() {
        return this.ticksSinceResourcePickup > 3600;
    }
    @Override
    public void resetResourcePickupTicks() {
        this.ticksSinceResourcePickup = 0;
    }
    @Override
    public void setTicksUntilCanResourcePickup(int ticksUntilCanResourcePickup) {
        this.ticksUntilCanResourcePickup = ticksUntilCanResourcePickup;
    }
    @Override
    public int getTicksUntilCanResourcePickup() {
        return this.ticksUntilCanResourcePickup;
    }

    @Override
    protected boolean canEnterNestExt() {
        return this.hasResource() || this.failedResourcePickupTooLong();
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return RYSEntities.GARDEN_ANT.create(world);
    }
}
