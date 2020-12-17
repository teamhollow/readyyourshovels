package group.rys.client.renderer;

import group.rys.client.renderer.layer.GathererAntHeldItemLayer;
import group.rys.client.renderer.model.GathererAntModel;
import group.rys.common.entity.GathererAntEntity;
import group.rys.core.util.Reference;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GathererAntRenderer extends MobRenderer<GathererAntEntity, GathererAntModel> {
	
	public GathererAntRenderer(EntityRendererManager p_i50961_1_) {
		super(p_i50961_1_, new GathererAntModel(), 0.25F);
		this.addLayer(new GathererAntHeldItemLayer(this));
	}
	
	protected ResourceLocation getEntityTexture(GathererAntEntity entity) {
		return new ResourceLocation(Reference.MOD_ID, "textures/entity/ant.png");
	}
	
}
