package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GardenAntEntity extends PathAwareEntity {
    public static final String id = "garden_ant";
    public static final EntityType.Builder<GardenAntEntity> builder = EntityType.Builder
        .create(GardenAntEntity::new, SpawnGroup.CREATURE)
        .setDimensions(0.5F, 0.5F)
        .maxTrackingRange(8);
    public static final int[] spawnEggColors = { 5065037, 9433559 };

    private int lifetime;
    public GardenAntEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.experiencePoints = 0;
    }

    public static Ingredient TEMPT_ITEMS = Ingredient.ofItems(Items.SUGAR, Items.COOKIE, Items.CAKE, Items.HONEY_BLOCK, Items.HONEYCOMB, Items.HONEY_BOTTLE, Items.HONEY_BLOCK, Items.PUMPKIN_PIE, Items.SWEET_BERRIES);

    public static DefaultAttributeContainer.Builder createGardenAntAttributes() {
            return MobEntity.createMobAttributes()
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20D)
                    .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 7.0D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(2, new TemptGoal(this,1.1D, TEMPT_ITEMS, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this,2.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, BeeEntity.class, 6));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6));
        this.goalSelector.add(6, new LookAroundGoal(this));
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        super.readCustomDataFromTag(compound);
        this.lifetime = compound.getInt("Lifetime");
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.world.isClient) {
            for (int i = 0; i < 2; ++i) {
                ++this.lifetime;

                if (this.lifetime >= 4800) {
                    this.remove();
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_GRASS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_ENDERMAN_TELEPORT;
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        super.playStepSound(pos, blockState);
        this.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, 0.15F, 1);
    }

}
