package group.rys.common.entity;

import group.rys.core.registry.ModBlocks;
import group.rys.core.registry.ModEntities;
import group.rys.core.util.NestUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

public class HuntingAntEntity extends CreatureEntity {
    private int lifetime;

    private static final DataParameter<BlockPos> INVENTORY = EntityDataManager.createKey(HuntingAntEntity.class, DataSerializers.BLOCK_POS);

    private static final DataParameter<Boolean> FED = EntityDataManager.createKey(HuntingAntEntity.class, DataSerializers.BOOLEAN);


    public static final Predicate<LivingEntity> TARGET = (p_213616_0_) -> {
        return p_213616_0_ instanceof ZombieEntity || p_213616_0_ instanceof SpiderEntity || p_213616_0_.getType() == ModEntities.gatherer_ant;
    };

    public HuntingAntEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDropChance(EquipmentSlotType.MAINHAND, 1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    public void setInventoryPosition(BlockPos pos) {
        this.dataManager.set(INVENTORY, pos);
    }

    public BlockPos getInventoryPosition() {
        return this.dataManager.get(INVENTORY);
    }

    public void setFed(boolean fed) {
        this.dataManager.set(FED, fed);
    }

    public boolean getFed() {
        return this.dataManager.get(FED);
    }

    public boolean hasInventory() {
        return this.getInventoryPosition() != BlockPos.ZERO;
    }

    public void resetInventoryPosition() {
        this.setInventoryPosition(BlockPos.ZERO);
    }

    protected void registerData() {
        super.registerData();


        this.dataManager.register(INVENTORY, BlockPos.ZERO);
        this.dataManager.register(FED, false);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putInt("Lifetime", this.lifetime);

        compound.putInt("InvPosX", this.getInventoryPosition().getX());
        compound.putInt("InvPosY", this.getInventoryPosition().getY());
        compound.putInt("InvPosZ", this.getInventoryPosition().getZ());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        this.lifetime = compound.getInt("Lifetime");

        int i = compound.getInt("InvPosX");
        int j = compound.getInt("InvPosY");
        int k = compound.getInt("InvPosZ");

        this.setInventoryPosition(new BlockPos(i, j, k));
    }

    protected void registerAttributes() {
        super.registerAttributes();

        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 0.8F, true));
        //this.goalSelector.addGoal(1, new HuntingAntEntity.FindInventoryGoal(this));
        //this.goalSelector.addGoal(2, new HuntingAntEntity.UpdateInventoryGoal(this));
        //this.goalSelector.addGoal(3, new HuntingAntEntity.StoreFedGoal(this));
        this.goalSelector.addGoal(5, new HuntingAntEntity.WalkAroundGoal(this));
        //this.goalSelector.addGoal(6, new HuntingAntEntity.WalkAroundInventoryGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, TARGET) {
            @Override
            public boolean shouldExecute() {
                return !getFed() && super.shouldExecute();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, true) {
            @Override
            public boolean shouldExecute() {
                return !getFed() && super.shouldExecute();
            }
        });
    }

    protected void playStepSound(BlockPos pos, BlockState state) {

    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    public void fall(float distance, float damageMultiplier) {

    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (!(this instanceof QueenAntEntity)) {
                if (!this.isNoDespawnRequired()) {
                    ++this.lifetime;
                }

                if (this.lifetime >= 2400) {
                    this.remove();
                }
            }
        }
    }

    public void tick() {
        super.tick();
    }

    @Override
    public void onKillEntity(LivingEntity p_70074_1_) {
        super.onKillEntity(p_70074_1_);
        this.setFed(true);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (entityIn instanceof LivingEntity) {
                int i = 0;
                if (this.world.getDifficulty() == Difficulty.EASY) {
                    i = 2;
                } else if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    i = 5;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    i = 6;
                }

                if (i > 0) {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, i * 20, 0));
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static class FindInventoryGoal extends Goal {

        private final HuntingAntEntity ant;

        public FindInventoryGoal(HuntingAntEntity ant) {
            this.ant = ant;
        }

        public boolean shouldExecute() {
            return !this.ant.hasInventory();
        }

        public void startExecuting() {
            if (this.shouldExecute()) {
                this.tick();
            }
        }

        public void tick() {
            BlockPos pos_1 = this.ant.getPosition();
            World world = this.ant.getEntityWorld();

            BlockPos pos_2 = null;

            for (BlockPos pos_3 : BlockPos.getAllInBoxMutable(pos_1.getX() - 16, pos_1.getY() - 16, pos_1.getZ() - 16, pos_1.getX() + 16, pos_1.getY() + 16, pos_1.getZ() + 16)) {
                if (world.getBlockState(pos_3).getBlock() == ModBlocks.hunting_anthill) {
                    pos_2 = pos_3;
                    break;
                }
            }

            if (pos_2 != null) {
                this.ant.setInventoryPosition(pos_2);
            }
        }

    }

    public static class UpdateInventoryGoal extends Goal {

        private final HuntingAntEntity ant;

        public UpdateInventoryGoal(HuntingAntEntity ant) {
            this.ant = ant;
        }

        public boolean shouldExecute() {
            return this.ant.hasInventory();
        }

        public void startExecuting() {
            if (this.shouldExecute()) {
                this.tick();
            }
        }

        public void tick() {
            World world = this.ant.getEntityWorld();
            BlockPos pos = this.ant.getInventoryPosition();

            if (world.getBlockState(pos).getBlock() != ModBlocks.hunting_anthill) {
                this.ant.resetInventoryPosition();
            }
        }

    }

    public static class WalkAroundGoal extends Goal {

        private final HuntingAntEntity ant;

        public WalkAroundGoal(HuntingAntEntity ant) {
            this.ant = ant;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return this.ant.getNavigator().noPath();
        }

        public boolean shouldContinueExecuting() {
            return !this.ant.getNavigator().noPath();
        }

        public void startExecuting() {
            World world = this.ant.world;

            int x = world.rand.nextInt(32) - 16;
            int y = world.rand.nextInt(32) - 16;
            int z = world.rand.nextInt(32) - 16;

            BlockPos pos_1 = this.ant.getPosition();
            BlockPos pos_2 = pos_1.add(x, y, z);

            if (world.isAirBlock(pos_2) && !world.hasWater(pos_2) && !world.isAirBlock(pos_2.down()) && !world.hasWater(pos_2.down())) {
                this.ant.getNavigator().setPath(this.ant.getNavigator().getPathToPos(pos_2, 1), 0.75D);
            }
        }

    }

    public static class WalkAroundInventoryGoal extends Goal {

        private final HuntingAntEntity ant;

        public WalkAroundInventoryGoal(HuntingAntEntity ant) {
            this.ant = ant;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return this.ant.hasInventory() && this.ant.getNavigator().noPath();
        }

        public boolean shouldContinueExecuting() {
            return !this.ant.getNavigator().noPath();
        }

        public void startExecuting() {
            World world = this.ant.world;

            int x = world.rand.nextInt(32) - 16;
            int y = world.rand.nextInt(32) - 16;
            int z = world.rand.nextInt(32) - 16;

            BlockPos pos_1 = this.ant.getInventoryPosition();
            BlockPos pos_2 = pos_1.add(x, y, z);

            if (world.isAirBlock(pos_2) && !world.hasWater(pos_2) && !world.isAirBlock(pos_2.down()) && !world.hasWater(pos_2.down())) {
                this.ant.getNavigator().setPath(this.ant.getNavigator().getPathToPos(pos_2, 1), 0.75D);
            }
        }

    }

    public static class StoreFedGoal extends Goal {

        private final HuntingAntEntity ant;

        public StoreFedGoal(HuntingAntEntity ant) {
            this.ant = ant;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return this.ant.getFed() && !this.ant.getHeldItemMainhand().isEmpty() && this.ant.hasInventory();
        }

        public void startExecuting() {
            World world = this.ant.world;
            BlockPos pos = this.ant.getInventoryPosition();
            BlockState state = world.getBlockState(pos);

            ItemStack stack = this.ant.getHeldItemMainhand();

            if (state.getBlock() == ModBlocks.hunting_anthill && this.ant.getFed()) {
                this.ant.getMoveHelper().setMoveTo(pos.getX(), pos.getY(), pos.getZ(), 0.75D);

                if (this.ant.getPosition().distanceSq(pos) <= 1) {
                    NestUtils.addFed(world, pos);
                }
            }
        }

    }

}
