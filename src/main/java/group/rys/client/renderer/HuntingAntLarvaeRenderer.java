package group.rys.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import group.rys.client.renderer.model.HuntingAntLarvaeModel;
import group.rys.common.entity.HuntingAntLarvaeEntity;
import group.rys.core.util.Reference;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class HuntingAntLarvaeRenderer<T extends HuntingAntLarvaeEntity> extends EntityRenderer<T> {
    private final HuntingAntLarvaeModel field_203088_f = new HuntingAntLarvaeModel<>();

    public HuntingAntLarvaeRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translatef((float) x, (float) y, (float) z);
        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) + 180, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch) + 180, 0.0F, 0.0F, 1.0F);
        this.field_203088_f.singleRender(0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/ant_egg_projectile.png");
    }
}
