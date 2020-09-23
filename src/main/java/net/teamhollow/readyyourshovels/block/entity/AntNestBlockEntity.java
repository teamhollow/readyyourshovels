package net.teamhollow.readyyourshovels.block.entity;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntityTypeTags;

public class AntNestBlockEntity extends BlockEntity implements Tickable {
    public static final String id = AntNestBlock.id;
    public static final BlockEntityType.Builder<AntNestBlockEntity> builder = BlockEntityType.Builder.create(AntNestBlockEntity::new, RYSBlocks.ANT_NEST);

    private final List<AntNestBlockEntity.Ant> ants = Lists.newArrayList();
    private BlockPos resourcePos = null;

    public AntNestBlockEntity() {
        super(RYSBlockEntities.ANT_NEST);
    }

    public void markDirty() {
        if (this.isNearFire()) {
            this.angerAnts((PlayerEntity) null, this.world.getBlockState(this.getPos()), AntNestBlockEntity.AntState.EMERGENCY);
        }

        super.markDirty();
    }

    public boolean isNearFire() {
        if (this.world == null) {
            return false;
        } else {
            Iterator<BlockPos> iterator = BlockPos.iterate(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1)).iterator();

            BlockPos blockPos;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                blockPos = (BlockPos) iterator.next();
            } while (!(this.world.getBlockState(blockPos).getBlock() instanceof FireBlock));

            return true;
        }
    }

    public boolean hasNoAnts() {
        return this.ants.isEmpty();
    }

    public boolean isFullOfAnts() {
        return this.ants.size() == 5;
    }

    public void angerAnts(PlayerEntity player, BlockState state, AntNestBlockEntity.AntState antState) {
        List<Entity> list = this.tryReleaseAnt(state, antState);
        if (player != null) {
            Iterator<Entity> iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();
                if (entity instanceof AbstractAntEntity && entity instanceof Angerable) {
                    AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                    if (player.getPos().squaredDistanceTo(entity.getPos()) <= 16.0D) {
                        if (!this.isSmoked()) {
                            antEntity.setTarget(player);
                        } else {
                            antEntity.setCannotEnterNestTicks(400);
                        }
                    }
                }
            }
        }
    }

    private List<Entity> tryReleaseAnt(BlockState state, AntNestBlockEntity.AntState antState) {
        List<Entity> list = Lists.newArrayList();
        this.ants.removeIf((ant) -> {
            return this.releaseAnt(state, ant, list, antState);
        });
        return list;
    }

    public void tryEnterNest(Entity entity, boolean hasResource) {
        this.tryEnterNest(entity, hasResource, 0);
    }

    public int getAntCount() {
        return this.ants.size();
    }

    public static int getAcidLevel(BlockState state) {
        return state.get(AntNestBlock.ACID_LEVEL);
    }

    public boolean isSmoked() {
        return CampfireBlock.isLitCampfireInRange(this.world, this.getPos());
    }

    public void tryEnterNest(Entity entity, boolean hasResources, int ticksInNest) {
        if (this.ants.size() < 5) {
            entity.stopRiding();
            entity.removeAllPassengers();
            CompoundTag compoundTag = new CompoundTag();
            entity.saveToTag(compoundTag);
            this.ants.add(new AntNestBlockEntity.Ant(compoundTag, ticksInNest, hasResources ? 2400 : 600));
            if (this.world != null) {
                if (entity instanceof AbstractAntEntity) {
                    AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                    if (antEntity.hasResource() && (!this.hasResourcePos() || this.world.random.nextBoolean())) {
                        this.resourcePos = antEntity.getResourcePos();
                    }
                }

                BlockPos blockPos = this.getPos();
                this.world.playSound((PlayerEntity) null, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entity.remove();
        }
    }

    private boolean releaseAnt(BlockState state, AntNestBlockEntity.Ant ant, List<Entity> list, AntNestBlockEntity.AntState antState) {
        if ((this.world.isNight() || this.world.isRaining()) && antState != AntNestBlockEntity.AntState.EMERGENCY) {
            return false;
        } else {
            BlockPos blockPos = this.getPos();
            CompoundTag compoundTag = ant.entityData;
            compoundTag.remove("Passengers");
            compoundTag.remove("Leash");
            compoundTag.remove("UUID");
            Direction direction = (Direction) state.get(AntNestBlock.FACING);
            BlockPos blockPos2 = blockPos.offset(direction);
            boolean bl = !this.world.getBlockState(blockPos2).getCollisionShape(this.world, blockPos2).isEmpty();
            if (bl && antState != AntNestBlockEntity.AntState.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityWithPassengers(compoundTag, this.world, (entityx) -> {
                    return entityx;
                });
                if (entity != null) {
                    if (!entity.getType().isIn(RYSEntityTypeTags.ANT_NEST_INHABITORS)) {
                        return false;
                    } else {
                        if (entity instanceof AbstractAntEntity) {
                            AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                            if (this.hasResourcePos() && !antEntity.hasResource() && this.world.random.nextFloat() < 0.9F) {
                                antEntity.setResourcePos(this.resourcePos);
                            }

                            if (antState == AntNestBlockEntity.AntState.RESOURCE_DELIVERED) {
                                antEntity.onResourceDelivered();
                                if (state.isOf(RYSBlocks.ANT_NEST)) {
                                    int acidLevel = getAcidLevel(state);
                                    if (acidLevel < 5) {
                                        int toAdd = this.world.random.nextInt(100) == 0 ? 2 : 1;
                                        if (acidLevel + toAdd > 5) {
                                            toAdd--;
                                        }

                                        this.world.setBlockState(this.getPos(), state.with(AntNestBlock.ACID_LEVEL, acidLevel + toAdd));
                                    }
                                }
                            }

                            this.ageAnt(ant.ticksInNest, antEntity);
                            if (list != null) {
                                list.add(antEntity);
                            }

                            float f = entity.getWidth();
                            double d = bl ? 0.0D : 0.55D + (double) (f / 2.0F);
                            double x = (double) blockPos.getX() + 0.5D + d * (double) direction.getOffsetX();
                            double y = (double) blockPos.getY() + 0.5D - (double) (entity.getHeight() / 2.0F);
                            double z = (double) blockPos.getZ() + 0.5D + d * (double) direction.getOffsetZ();
                            entity.refreshPositionAndAngles(x, y, z, entity.yaw, entity.pitch);
                        }

                        this.world.playSound((PlayerEntity) null, blockPos, SoundEvents.BLOCK_BEEHIVE_EXIT,
                                SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return this.world.spawnEntity(entity);
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private void ageAnt(int ticks, AbstractAntEntity ant) {
        int i = ant.getBreedingAge();
        if (i < 0) {
            ant.setBreedingAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            ant.setBreedingAge(Math.max(0, i - ticks));
        }

        ant.setLoveTicks(Math.max(0, ant.getLoveTicks() - ticks));
        ant.resetResourcePickupTicks();
    }

    private boolean hasResourcePos() {
        return this.resourcePos != null;
    }

    private void tickAnts() {
        Iterator<AntNestBlockEntity.Ant> iterator = this.ants.iterator();

        AntNestBlockEntity.Ant ant;
        for (BlockState blockState = this.getCachedState(); iterator.hasNext(); ant.ticksInNest++) {
            ant = (AntNestBlockEntity.Ant) iterator.next();
            if (ant.ticksInNest > ant.minOccupationTicks) {
                AntNestBlockEntity.AntState antState = ant.entityData.getBoolean("HasResource")
                        ? AntNestBlockEntity.AntState.RESOURCE_DELIVERED
                        : AntNestBlockEntity.AntState.ANT_RELEASED;
                if (this.releaseAnt(blockState, ant, (List<Entity>) null, antState)) {
                    iterator.remove();
                }
            }
        }

    }

    public void tick() {
        if (!this.world.isClient) {
            this.tickAnts();
            BlockPos blockPos = this.getPos();
            if (this.ants.size() > 0 && this.world.getRandom().nextDouble() < 0.005D) {
                double d = (double) blockPos.getX() + 0.5D;
                double e = (double) blockPos.getY();
                double f = (double) blockPos.getZ() + 0.5D;
                this.world.playSound((PlayerEntity) null, d, e, f, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.ants.clear();
        ListTag listTag = tag.getList("Ants", 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            AntNestBlockEntity.Ant ant = new AntNestBlockEntity.Ant(compoundTag.getCompound("EntityData"), compoundTag.getInt("TicksInNest"), compoundTag.getInt("MinOccupationTicks"));
            this.ants.add(ant);
        }

        this.resourcePos = null;
        if (tag.contains("ResourcePos")) {
            this.resourcePos = NbtHelper.toBlockPos(tag.getCompound("ResourcePos"));
        }

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Ants", this.getAnts());
        if (this.hasResourcePos()) {
            tag.put("ResourcePos", NbtHelper.fromBlockPos(this.resourcePos));
        }

        return tag;
    }

    public ListTag getAnts() {
        ListTag listTag = new ListTag();
        Iterator<Ant> iterator = this.ants.iterator();

        while (iterator.hasNext()) {
            AntNestBlockEntity.Ant ant = (AntNestBlockEntity.Ant) iterator.next();
            ant.entityData.remove("UUID");
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("EntityData", ant.entityData);
            compoundTag.putInt("TicksInNest", ant.ticksInNest);
            compoundTag.putInt("MinOccupationTicks", ant.minOccupationTicks);
            listTag.add(compoundTag);
        }

        return listTag;
    }

    static class Ant {
        private final CompoundTag entityData;
        private int ticksInNest;
        private final int minOccupationTicks;

        private Ant(CompoundTag entityData, int ticksInNest, int minOccupationTicks) {
            entityData.remove("UUID");
            this.entityData = entityData;
            this.ticksInNest = ticksInNest;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public static enum AntState {
        RESOURCE_DELIVERED, ANT_RELEASED, EMERGENCY;
    }
}
