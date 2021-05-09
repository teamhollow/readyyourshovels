package net.teamhollow.readyyourshovels.entity.peaty_slime;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.teamhollow.readyyourshovels.init.RYSEntities;

@Environment(EnvType.CLIENT)
public class PeatySlimeEyesFeatureRenderer<T extends PeatySlimeEntity> extends FeatureRenderer<T, PeatySlimeEntityModel<T>> {
    public PeatySlimeEyesFeatureRenderer(FeatureRendererContext<T, PeatySlimeEntityModel<T>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertices.getBuffer(
            RenderLayer.getEyes(
                RYSEntities.texture("slime/" + PeatySlimeEntity.id + "_eyes" + (
                        entity.isOnFire()
                            ? "_combusting"
                            : ""
                    )
                )
            )
        );
        this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
