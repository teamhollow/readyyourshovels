package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class GardenAntEntityRenderer extends MobEntityRenderer<GardenAntEntity, GardenAntEntityModel<GardenAntEntity>> {
    public GardenAntEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new GardenAntEntityModel<GardenAntEntity>(), 0.25F);
    }

    @Override
    public Identifier getTexture(GardenAntEntity entity) {
        return RYSEntities.texture(GardenAntEntity.id + "/" + GardenAntEntity.id);
    }
}
