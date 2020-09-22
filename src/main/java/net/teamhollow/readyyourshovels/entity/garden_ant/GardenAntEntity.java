package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.entity.AbstractAntEntity;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class GardenAntEntity extends AbstractAntEntity {
    public static final String id = "garden_ant";
    public static final EntityType.Builder<GardenAntEntity> builder = EntityType.Builder
            .create(GardenAntEntity::new, SpawnGroup.CREATURE).setDimensions(0.5F, 0.5F).maxTrackingRange(8);
    public static final int[] spawnEggColors = { 5065037, 9433559 };

    public static DefaultAttributeContainer.Builder createGardenAntAttributes() {
        return AbstractAntEntity.createAntAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 7.0D);
    }

    public GardenAntEntity(EntityType<? extends AbstractAntEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PassiveEntity createChild(PassiveEntity mate) {
        return RYSEntities.GARDEN_ANT.create(this.world);
    }
}
