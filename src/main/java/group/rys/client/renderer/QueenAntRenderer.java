package group.rys.client.renderer;

import group.rys.client.renderer.model.QueenAntModel;
import group.rys.common.entity.QueenAntEntity;
import group.rys.core.util.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QueenAntRenderer extends MobRenderer<QueenAntEntity, QueenAntModel> {

    public QueenAntRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new QueenAntModel(), 0.25F);
    }

    protected ResourceLocation getEntityTexture(QueenAntEntity entity) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/queen_ant.png");
    }

}
