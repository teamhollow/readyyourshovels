package group.rys.common.entity;

import group.rys.core.registry.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public class QueenAntEntity extends HuntingAntEntity implements IRangedAttackMob {
    private static final DataParameter<BlockPos> INVENTORY = EntityDataManager.createKey(QueenAntEntity.class, DataSerializers.BLOCK_POS);


    public static final Predicate<LivingEntity> TARGET = (p_213616_0_) -> {
        return p_213616_0_ instanceof ZombieEntity || p_213616_0_ instanceof SpiderEntity || p_213616_0_.getType() == ModEntities.gatherer_ant;
    };

    public QueenAntEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDropChance(EquipmentSlotType.MAINHAND, 1.0F);
        this.moveController = new FlyingMovementController(this);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(true);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    public void setInventoryPosition(BlockPos pos) {
        this.dataManager.set(INVENTORY, pos);
    }

    public BlockPos getInventoryPosition() {
        return this.dataManager.get(INVENTORY);
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
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putInt("InvPosX", this.getInventoryPosition().getX());
        compound.putInt("InvPosY", this.getInventoryPosition().getY());
        compound.putInt("InvPosZ", this.getInventoryPosition().getZ());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);


        int i = compound.getInt("InvPosX");
        int j = compound.getInt("InvPosY");
        int k = compound.getInt("InvPosZ");

        this.setInventoryPosition(new BlockPos(i, j, k));
    }

    protected void registerAttributes() {
        super.registerAttributes();

        this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue((double) 0.5F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 0.8F, 80, 10.0F));
        this.goalSelector.addGoal(1, new QueenAntEntity.FindInventoryGoal(this));
        this.goalSelector.addGoal(2, new QueenAntEntity.UpdateInventoryGoal(this));
        this.goalSelector.addGoal(5, new QueenAntEntity.WalkAroundGoal(this));
        this.goalSelector.addGoal(6, new QueenAntEntity.WalkAroundInventoryGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp(HuntingAntEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, TARGET));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, true));
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

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isFlying() {
        return !this.onGround;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    public void tick() {
        super.tick();
    }

    public void livingTick() {

        super.livingTick();

        Vec3d vec3d = this.getMotion();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        }
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
                    i = 4;
                } else if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    i = 6;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    i = 8;
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

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {

        this.world.playSound((PlayerEntity) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (this.world.rand.nextFloat() * 0.4F + 0.8F));

        HuntingAntLarvaeEntity eggEntity = new HuntingAntLarvaeEntity(this.world, this);

        double d0 = target.posY + (double) target.getEyeHeight();
        double d1 = target.posX - this.posX;
        double d2 = d0 - eggEntity.posY;
        double d3 = target.posZ - this.posZ;

        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.03F;
        eggEntity.shoot(d1, d2 + (double) f, d3, 1.6F, 8.0F);

        this.world.addEntity(eggEntity);
    }
}
