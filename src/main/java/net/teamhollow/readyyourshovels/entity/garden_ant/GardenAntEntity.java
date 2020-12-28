package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;

public class GardenAntEntity extends AbstractAntEntity {
    public static final String id = "garden_ant";

    private static final TrackedData<BlockPos> NEST_POS = DataTracker.registerData(GardenAntEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<BlockPos> RESOURCE_POS = DataTracker.registerData(GardenAntEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public GardenAntEntity(EntityType<? extends AbstractAntEntity> type, World worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
        this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }

    public BlockPos getNestPos() {
        return this.dataTracker.get(NEST_POS);
    }
    public boolean hasNestPos() {
        return this.getNestPos() != BlockPos.ZERO;
    }
    public void setNestPos(BlockPos pos) {
        this.dataTracker.set(NEST_POS, pos);
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

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(NEST_POS, new BlockPos(BlockPos.ZERO));
        this.dataTracker.startTracking(RESOURCE_POS, new BlockPos(BlockPos.ZERO));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);

        tag.putInt("Age", this.age);

        if (this.hasNestPos()) tag.put("NestPos", NbtHelper.fromBlockPos(this.getNestPos()));
        if (this.hasResourcePos()) tag.put("ResourcePos", NbtHelper.fromBlockPos(this.getResourcePos()));
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);

        this.age = tag.getInt("Age");

        this.setNestPos(NbtHelper.toBlockPos(tag.getCompound("NestPos")));
        this.setResourcePos(NbtHelper.toBlockPos(tag.getCompound("ResourcePos")));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
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
        return stack.getItem() == Items.APPLE;
    }
}
