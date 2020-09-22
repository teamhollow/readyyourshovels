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
    private BlockPos flowerPos = null;

    public AntNestBlockEntity() {
        super(RYSBlockEntities.ANT_NEST);
    }

    public void markDirty() {
        if (this.isNearFire()) {
            this.angerAnts((PlayerEntity) null, this.world.getBlockState(this.getPos()),
                    AntNestBlockEntity.AntState.EMERGENCY);
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
                if (entity instanceof AbstractAntEntity) {
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

    public void tryEnterNest(Entity entity, boolean hasNectar) {
        this.tryEnterHive(entity, hasNectar, 0);
    }

    public int getAntCount() {
        return this.ants.size();
    }

    public boolean isSmoked() {
        return CampfireBlock.isLitCampfireInRange(this.world, this.getPos());
    }

    // protected void sendDebugData() {
    //     DebugInfoSender.sendBeehiveDebugData(this); TODO
    // }

    public void tryEnterHive(Entity entity, boolean hasResources, int ticksInHive) {
        if (this.ants.size() < 5) {
            entity.stopRiding();
            entity.removeAllPassengers();
            CompoundTag compoundTag = new CompoundTag();
            entity.saveToTag(compoundTag);
            this.ants.add(new AntNestBlockEntity.Ant(compoundTag, ticksInHive, hasResources ? 2400 : 600));
            if (this.world != null) {
                if (entity instanceof AbstractAntEntity) {
                    AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                    if (antEntity.hasFlower() && (!this.hasFlowerPos() || this.world.random.nextBoolean())) {
                        this.flowerPos = antEntity.getFlowerPos();
                    }
                }

                BlockPos blockPos = this.getPos();
                this.world.playSound((PlayerEntity) null, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            entity.remove();
        }
    }

    private boolean releaseAnt(BlockState state, AntNestBlockEntity.Ant ant, List<Entity> list,
            AntNestBlockEntity.AntState antState) {
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
                            if (this.hasFlowerPos() && !antEntity.hasFlower() && this.world.random.nextFloat() < 0.9F) {
                                antEntity.setFlowerPos(this.flowerPos);
                            }

                            // if (antState == AntNestBlockEntity.AntState.HONEY_DELIVERED) {
                            //     antEntity.onHoneyDelivered();
                            //     if (state.getBlock().isIn(BlockTags.BEEHIVES)) {
                            //         int i = getHoneyLevel(state);
                            //         if (i < 5) {
                            //             int j = this.world.random.nextInt(100) == 0 ? 2 : 1;
                            //             if (i + j > 5) {
                            //                 --j;
                            //             }

                            //             this.world.setBlockState(this.getPos(),
                            //                     (BlockState) state.with(AntNestBlock.HONEY_LEVEL, i + j));
                            //         }
                            //     }
                            // }

                            this.ageAnt(ant.ticksInHive, antEntity);
                            if (list != null) {
                                list.add(antEntity);
                            }

                            float f = entity.getWidth();
                            double d = bl ? 0.0D : 0.55D + (double) (f / 2.0F);
                            double e = (double) blockPos.getX() + 0.5D + d * (double) direction.getOffsetX();
                            double g = (double) blockPos.getY() + 0.5D - (double) (entity.getHeight() / 2.0F);
                            double h = (double) blockPos.getZ() + 0.5D + d * (double) direction.getOffsetZ();
                            entity.refreshPositionAndAngles(e, g, h, entity.yaw, entity.pitch);
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
        ant.resetPollinationTicks();
    }

    private boolean hasFlowerPos() {
        return this.flowerPos != null;
    }

    private void tickAnts() {
        Iterator<AntNestBlockEntity.Ant> iterator = this.ants.iterator();

        AntNestBlockEntity.Ant ant;
        for (BlockState blockState = this.getCachedState(); iterator.hasNext(); ant.ticksInHive++) {
            ant = (AntNestBlockEntity.Ant) iterator.next();
            if (ant.ticksInHive > ant.minOccupationTicks) {
                AntNestBlockEntity.AntState antState = ant.entityData.getBoolean("HasNectar")
                        ? AntNestBlockEntity.AntState.HONEY_DELIVERED
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
                this.world.playSound((PlayerEntity) null, d, e, f, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS,
                        1.0F, 1.0F);
            }

            // this.sendDebugData(); TODO
        }
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.ants.clear();
        ListTag listTag = tag.getList("Ants", 10);

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            AntNestBlockEntity.Ant ant = new AntNestBlockEntity.Ant(compoundTag.getCompound("EntityData"),
                    compoundTag.getInt("TicksInHive"), compoundTag.getInt("MinOccupationTicks"));
            this.ants.add(ant);
        }

        this.flowerPos = null;
        if (tag.contains("FlowerPos")) {
            this.flowerPos = NbtHelper.toBlockPos(tag.getCompound("FlowerPos"));
        }

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Ants", this.getAnts());
        if (this.hasFlowerPos()) {
            tag.put("FlowerPos", NbtHelper.fromBlockPos(this.flowerPos));
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
            compoundTag.putInt("TicksInHive", ant.ticksInHive);
            compoundTag.putInt("MinOccupationTicks", ant.minOccupationTicks);
            listTag.add(compoundTag);
        }

        return listTag;
    }

    static class Ant {
        private final CompoundTag entityData;
        private int ticksInHive;
        private final int minOccupationTicks;

        private Ant(CompoundTag entityData, int ticksInHive, int minOccupationTicks) {
            entityData.remove("UUID");
            this.entityData = entityData;
            this.ticksInHive = ticksInHive;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public static enum AntState {
        HONEY_DELIVERED, ANT_RELEASED, EMERGENCY;
    }
}
