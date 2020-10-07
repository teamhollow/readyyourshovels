package teamhollow.readyyourshovels.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import teamhollow.readyyourshovels.registry.RYSEntity;
import teamhollow.readyyourshovels.registry.RYSSoundEvents;

public class GardenAntEntity extends AbstractAntEntity {

    public GardenAntEntity(EntityType<? extends AbstractAntEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createGardenAntAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.20D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 7.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return RYSSoundEvents.ENTITY_GARDEN_ANT_AMBIENT;
    }

    @Override
    public GardenAntEntity func_241840_a(ServerWorld world, AgeableEntity p_241840_2_) {
        return RYSEntity.GARDEN_ANT.create(world);
    }
}
