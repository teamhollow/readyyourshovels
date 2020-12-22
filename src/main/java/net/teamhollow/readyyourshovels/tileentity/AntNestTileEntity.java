package net.teamhollow.readyyourshovels.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.teamhollow.readyyourshovels.registry.RYSBlocks;
import net.teamhollow.readyyourshovels.registry.RYSTileEntities;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class AntNestTileEntity extends TileEntity implements ITickableTileEntity {
    private final List<AntNestTileEntity.Ant> ants = Lists.newArrayList();
    @Nullable
    private BlockPos resourcePos = null;

    public AntNestTileEntity() {
        super(RYSTileEntities.ANT_NEST);
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        if (this.isNearFire()) {
            assert this.world != null;
            this.angerAnts(null, this.world.getBlockState(this.getPos()), AntNestTileEntity.State.EMERGENCY);
        }

        super.markDirty();
    }

    public boolean isNearFire() {
        if (this.world == null) {
            return false;
        } else {
            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1))) {
                if (this.world.getBlockState(blockpos).getBlock() instanceof FireBlock) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean hasNoAnts() {
        return this.ants.isEmpty();
    }

    public boolean isFullOfAnts() {
        return this.ants.size() == 3;
    }

    public void angerAnts(@Nullable PlayerEntity p_226963_1_, BlockState p_226963_2_, AntNestTileEntity.State p_226963_3_) {
        List<Entity> list = this.tryReleaseBee(p_226963_2_, p_226963_3_);
        if (p_226963_1_ != null) {
            for (Entity entity : list) {
                if (entity instanceof AbstractAntEntity) {
                    AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                    if (p_226963_1_.getPositionVec().squareDistanceTo(entity.getPositionVec()) <= 16.0D) {
                        if (!this.isSmoked()) {
                            antEntity.setAttackTarget(p_226963_1_);
                        } else {
                            antEntity.setCannotEnterNestTicks(400);
                        }
                    }
                }
            }
        }

    }

    private List<Entity> tryReleaseBee(BlockState p_226965_1_, AntNestTileEntity.State p_226965_2_) {
        List<Entity> list = Lists.newArrayList();
        this.ants.removeIf((p_226966_4_) -> this.func_235651_a_(p_226965_1_, p_226966_4_, list, p_226965_2_));
        return list;
    }

    public void tryEnterNest(Entity p_226961_1_, boolean p_226961_2_) {
        this.tryEnterNest(p_226961_1_, p_226961_2_, 0);
    }

    public int getBeeCount() {
        return this.ants.size();
    }

    public static int getHoneyLevel(BlockState p_226964_0_) {
        return p_226964_0_.get(BeehiveBlock.HONEY_LEVEL);
    }

    public boolean isSmoked() {
        assert this.world != null;
        return CampfireBlock.isSmokingBlockAt(this.world, this.getPos());
    }

    public void tryEnterNest(Entity p_226962_1_, boolean p_226962_2_, int p_226962_3_) {
        if (this.ants.size() < 3) {
            p_226962_1_.stopRiding();
            p_226962_1_.removePassengers();
            CompoundNBT compoundnbt = new CompoundNBT();
            p_226962_1_.writeUnlessPassenger(compoundnbt);
            this.ants.add(new AntNestTileEntity.Ant(compoundnbt, p_226962_3_, p_226962_2_ ? 2400 : 600));
            if (this.world != null) {
                if (p_226962_1_ instanceof AbstractAntEntity) {
                    AbstractAntEntity antEntity = (AbstractAntEntity) p_226962_1_;
                    if (antEntity.hasResource() && (!this.hasResourcePos() || this.world.rand.nextBoolean())) {
                        this.resourcePos = antEntity.getResourcePos();
                    }
                }

                BlockPos blockpos = this.getPos();
                this.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            p_226962_1_.remove();
        }
    }

    private boolean func_235651_a_(BlockState p_235651_1_, AntNestTileEntity.Ant p_235651_2_, @Nullable List<Entity> p_235651_3_, AntNestTileEntity.State p_235651_4_) {
        assert this.world != null;
        if ((this.world.isNightTime() || this.world.isRaining()) && p_235651_4_ != AntNestTileEntity.State.EMERGENCY) {
            return false;
        } else {
            BlockPos blockpos = this.getPos();
            CompoundNBT compoundnbt = p_235651_2_.entityData;
            compoundnbt.remove("Passengers");
            compoundnbt.remove("Leash");
            compoundnbt.remove("UUID");
            Direction direction = p_235651_1_.get(BeehiveBlock.FACING);
            BlockPos blockpos1 = blockpos.offset(direction);
            boolean flag = !this.world.getBlockState(blockpos1).getCollisionShape(this.world, blockpos1).isEmpty();
            if (flag && p_235651_4_ != AntNestTileEntity.State.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityAndExecute(compoundnbt, this.world, (p_226960_0_) -> p_226960_0_);
                if (entity != null) {
                    if (!entity.getType().isContained(EntityTypeTags.BEEHIVE_INHABITORS)) {
                        return false;
                    } else {
                        if (entity instanceof AbstractAntEntity) {
                            AbstractAntEntity antEntity = (AbstractAntEntity) entity;
                            if (this.hasResourcePos() && !antEntity.hasResource() && this.world.rand.nextFloat() < 0.9F) {
                                antEntity.setResourcePos(this.resourcePos);
                            }

                            if (p_235651_4_ == AntNestTileEntity.State.HONEY_DELIVERED) {
                                antEntity.onResourceDelivered();
                                if (p_235651_1_.isIn(RYSBlocks.ANT_NEST)) {
                                    int i = getHoneyLevel(p_235651_1_);
                                    if (i < 5) {
                                        int j = this.world.rand.nextInt(100) == 0 ? 2 : 1;
                                        if (i + j > 5) {
                                            --j;
                                        }

                                        this.world.setBlockState(this.getPos(), p_235651_1_.with(BeehiveBlock.HONEY_LEVEL, i + j));
                                    }
                                }
                            }

                            this.func_235650_a_(p_235651_2_.ticksInHive, antEntity);
                            if (p_235651_3_ != null) {
                                p_235651_3_.add(antEntity);
                            }

                            float f = entity.getWidth();
                            double d3 = flag ? 0.0D : 0.55D + (double) (f / 2.0F);
                            double d0 = (double) blockpos.getX() + 0.5D + d3 * (double) direction.getXOffset();
                            double d1 = (double) blockpos.getY() + 0.5D - (double) (entity.getHeight() / 2.0F);
                            double d2 = (double) blockpos.getZ() + 0.5D + d3 * (double) direction.getZOffset();
                            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
                        }

                        this.world.playSound(null, blockpos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return this.world.addEntity(entity);
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private void func_235650_a_(int p_235650_1_, AbstractAntEntity p_235650_2_) {
        int i = p_235650_2_.getGrowingAge();
        if (i < 0) {
            p_235650_2_.setGrowingAge(Math.min(0, i + p_235650_1_));
        } else if (i > 0) {
            p_235650_2_.setGrowingAge(Math.max(0, i - p_235650_1_));
        }

        p_235650_2_.setInLove(Math.max(0, p_235650_2_.func_234178_eO_() - p_235650_1_));
        //p_235650_2_.resetTicksWithoutNectar();
    }

    private boolean hasResourcePos() {
        return this.resourcePos != null;
    }

    private void tickAnts() {
        Iterator<AntNestTileEntity.Ant> iterator = this.ants.iterator();

        AntNestTileEntity.Ant beehivetileentity$bee;
        for (BlockState blockstate = this.getBlockState(); iterator.hasNext(); beehivetileentity$bee.ticksInHive++) {
            beehivetileentity$bee = iterator.next();
            if (beehivetileentity$bee.ticksInHive > beehivetileentity$bee.minOccupationTicks) {
                AntNestTileEntity.State beehivetileentity$state = beehivetileentity$bee.entityData.getBoolean("HasNectar") ? AntNestTileEntity.State.HONEY_DELIVERED : AntNestTileEntity.State.ANT_RELEASED;
                if (this.func_235651_a_(blockstate, beehivetileentity$bee, null, beehivetileentity$state)) {
                    iterator.remove();
                }
            }
        }

    }

    public void tick() {
        assert this.world != null;
        if (!this.world.isRemote) {
            this.tickAnts();
            BlockPos blockpos = this.getPos();
            if (this.ants.size() > 0 && this.world.getRandom().nextDouble() < 0.005D) {
                double d0 = (double) blockpos.getX() + 0.5D;
                double d1 = blockpos.getY();
                double d2 = (double) blockpos.getZ() + 0.5D;
                this.world.playSound(null, d0, d1, d2, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.ants.clear();
        ListNBT listnbt = nbt.getList("Ants", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            AntNestTileEntity.Ant beehivetileentity$bee = new AntNestTileEntity.Ant(compoundnbt.getCompound("EntityData"), compoundnbt.getInt("TicksInHive"), compoundnbt.getInt("MinOccupationTicks"));
            this.ants.add(beehivetileentity$bee);
        }

        this.resourcePos = null;
        if (nbt.contains("ResourcePos")) {
            this.resourcePos = NBTUtil.readBlockPos(nbt.getCompound("ResourcePos"));
        }

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("Ants", this.getAnts());
        if (this.hasResourcePos()) {
            compound.put("ResourcePos", NBTUtil.writeBlockPos(this.resourcePos));
        }

        return compound;
    }

    public ListNBT getAnts() {
        ListNBT listnbt = new ListNBT();

        for (AntNestTileEntity.Ant beehivetileentity$bee : this.ants) {
            beehivetileentity$bee.entityData.remove("UUID");
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.put("EntityData", beehivetileentity$bee.entityData);
            compoundnbt.putInt("TicksInHive", beehivetileentity$bee.ticksInHive);
            compoundnbt.putInt("MinOccupationTicks", beehivetileentity$bee.minOccupationTicks);
            listnbt.add(compoundnbt);
        }

        return listnbt;
    }

    static class Ant {
        private final CompoundNBT entityData;
        private int ticksInHive;
        private final int minOccupationTicks;

        private Ant(CompoundNBT nbt, int ticksInHive, int minOccupationTicks) {
            nbt.remove("UUID");
            this.entityData = nbt;
            this.ticksInHive = ticksInHive;
            this.minOccupationTicks = minOccupationTicks;
        }
    }

    public enum State {
        HONEY_DELIVERED,
        ANT_RELEASED,
        EMERGENCY
    }
}
