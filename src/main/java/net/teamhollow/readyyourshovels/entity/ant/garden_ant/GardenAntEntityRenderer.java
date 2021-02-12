package net.teamhollow.readyyourshovels.entity.ant.garden_ant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.client.init.RYSEntityModelLayers;
import net.teamhollow.readyyourshovels.init.RYSEntities;

@Environment(EnvType.CLIENT)
public class GardenAntEntityRenderer extends MobEntityRenderer<GardenAntEntity, GardenAntEntityModel<GardenAntEntity>> {
    public GardenAntEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new GardenAntEntityModel<>(context.getPart(RYSEntityModelLayers.GARDEN_ANT)), 0.25F);
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
