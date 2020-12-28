package net.teamhollow.readyyourshovels.entity.peaty_slime;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.entity.LivingEntity;
import net.teamhollow.readyyourshovels.init.RYSEntities;

@Environment(EnvType.CLIENT)
public class PeatySlimeEyesFeatureRenderer<T extends LivingEntity> extends EyesFeatureRenderer<T, PeatySlimeEntityModel<T>> {
    public PeatySlimeEyesFeatureRenderer(FeatureRendererContext<T, PeatySlimeEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return RenderLayer.getEyes(RYSEntities.texture("slime/"+ PeatySlimeEntity.id + "_eyes"));
    }
}
