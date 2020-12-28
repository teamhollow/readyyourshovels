package net.teamhollow.readyyourshovels.entity.peaty_slime;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.teamhollow.readyyourshovels.init.RYSEntities;

@Environment(EnvType.CLIENT)
public class PeatySlimeEntityRenderer extends MobEntityRenderer<PeatySlimeEntity, PeatySlimeEntityModel<PeatySlimeEntity>> {
    public PeatySlimeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new PeatySlimeEntityModel<>(), 0.25F);
        this.addFeature(new PeatySlimeEyesFeatureRenderer<>(this));
    }

    @Override
    public void render(PeatySlimeEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (float)entity.getSize();
        super.render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(PeatySlimeEntity entity, MatrixStack matrixStack, float amount) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float x = (float)entity.getSize();
        float y = MathHelper.lerp(amount, entity.lastStretch, entity.stretch) / (x * 0.5F + 1.0F);
        float z = 1.0F / (y + 1.0F);
        matrixStack.scale(z * x, 1.0F / z * x, z * x);
    }

    @Override
    public Identifier getTexture(PeatySlimeEntity entity) {
        return RYSEntities.texture("slime/" + PeatySlimeEntity.id);
    }
}
