package group.rys.client.renderer.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import group.rys.client.renderer.model.GathererAntModel;
import group.rys.common.entity.GathererAntEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class GathererAntHeldItemLayer extends LayerRenderer<GathererAntEntity, GathererAntModel> {
	
	public GathererAntHeldItemLayer(IEntityRenderer<GathererAntEntity, GathererAntModel> entityRendererIn) {
		super(entityRendererIn);
	}
	
	public void render(GathererAntEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
	      ItemStack itemstack = entityIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
	      
	      if (!itemstack.isEmpty()) {
	         GlStateManager.pushMatrix();
	         
	         GlStateManager.rotatef(netHeadYaw, 0.0F, 1.0F, 0.0F);
	         GlStateManager.rotatef(headPitch, 1.0F, 0.0F, 0.0F);
	         
//        	 GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
        	 GlStateManager.rotatef(87.5F, 1.0F, 0.0F, 0.0F);
        	 
	         GlStateManager.translatef(0.0F, 0.0F, -1.0F);
	         
	         Minecraft.getInstance().getItemRenderer().renderItem(itemstack, entityIn, ItemCameraTransforms.TransformType.GROUND, false);
	         GlStateManager.popMatrix();
	      }
	}
	
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
