package group.rys.client.renderer;

import group.rys.client.renderer.model.HuntingAntModel;
import group.rys.common.entity.HuntingAntEntity;
import group.rys.core.util.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HuntingAntRenderer extends MobRenderer<HuntingAntEntity, HuntingAntModel> {

    public HuntingAntRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new HuntingAntModel(), 0.25F);
    }

    protected ResourceLocation getEntityTexture(HuntingAntEntity entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/hunting_ant.png");
    }

}
