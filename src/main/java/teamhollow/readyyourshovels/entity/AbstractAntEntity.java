package teamhollow.readyyourshovels.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

public abstract class AbstractAntEntity extends AnimalEntity {
    protected AbstractAntEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

}
