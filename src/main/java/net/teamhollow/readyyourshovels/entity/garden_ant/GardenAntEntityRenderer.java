package net.teamhollow.readyyourshovels.entity.garden_ant;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class GardenAntEntityRenderer extends MobEntityRenderer<GardenAntEntity, GardenAntEntityModel<GardenAntEntity>> {
    public GardenAntEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new GardenAntEntityModel<GardenAntEntity>(), 0.25F);
    }

    @Override
    public Identifier getTexture(GardenAntEntity entity) {
        return RYSEntities.texture("ant/" + GardenAntEntity.id);
    }

    @Override
    protected void scale(GardenAntEntity entity, MatrixStack matrices, float amount) {
        if (entity.isBaby()) {
            amount = 0.6F;
            matrices.scale(amount, amount, amount);
        } else super.scale(entity, matrices, amount);
    }
}
