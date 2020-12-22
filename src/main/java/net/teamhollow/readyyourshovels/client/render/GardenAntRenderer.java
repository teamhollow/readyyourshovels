package net.teamhollow.readyyourshovels.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.teamhollow.readyyourshovels.client.model.GardenAntModel;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.entity.GardenAntEntity;

@OnlyIn(Dist.CLIENT)
public class GardenAntRenderer extends MobRenderer<GardenAntEntity, GardenAntModel<GardenAntEntity>> {
    private static final ResourceLocation ANT_TEXTURE = new ResourceLocation(ReadyYourShovels.MOD_ID, "textures/entity/ant/garden_ant.png");


    public GardenAntRenderer(EntityRendererManager entityRenderDispatcher) {
        super(entityRenderDispatcher, new GardenAntModel<>(), 0.25F);
    }

    @Override
    protected void preRenderCallback(GardenAntEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.isChild()) {
            matrixStackIn.scale(0.6F, 0.6F, 0.6F);
        }
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public ResourceLocation getEntityTexture(GardenAntEntity entity) {
        return ANT_TEXTURE;
    }
}
