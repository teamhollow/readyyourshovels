package group.rys.client.renderer.model;

import group.rys.common.entity.GathererAntEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GathererAntModel extends EntityModel<GathererAntEntity> {
	private final RendererModel body;
	private final RendererModel head;
	private final RendererModel antennae;
	private final RendererModel thorax;
	private final RendererModel abdomen;
	private final RendererModel legs;
	private final RendererModel left_leg_set;
	private final RendererModel left_leg_1;
	private final RendererModel left_leg_2;
	private final RendererModel left_leg_3;
	private final RendererModel right_leg_set;
	private final RendererModel right_leg_1;
	private final RendererModel right_leg_2;
	private final RendererModel right_leg_3;
	private GathererAntEntity entityIn;
//	private final RendererModel item;

	public GathererAntModel() {
		textureWidth = 32;
		textureHeight = 32;

		body = new RendererModel(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);

		head = new RendererModel(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 0, -2.0F, -7.0F, -4.0F, 4, 4, 4, 0.0F, false));

		antennae = new RendererModel(this);
		antennae.setRotationPoint(0.0F, -6.0F, -3.0F);
//		setRotationAngles(antennae, 0.5236F, 0.0F, 0.0F);
		head.addChild(antennae);
		antennae.cubeList.add(new ModelBox(antennae, 16, 13, -4.0F, -5.0F, 0.0F, 8, 4, 0, 0.0F, false));

		thorax = new RendererModel(this);
		thorax.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(thorax);
		thorax.cubeList.add(new ModelBox(thorax, 0, 8, -1.5F, -4.0F, -1.0F, 3, 2, 3, 0.0F, false));

		abdomen = new RendererModel(this);
		abdomen.setRotationPoint(0.0F, -1.0F, 0.0F);
//		setRotationAngles(abdomen, -0.1745F, 0.0F, 0.0F);
		body.addChild(abdomen);
		abdomen.cubeList.add(new ModelBox(abdomen, 18, 0, -2.0F, -5.0F, 1.0F, 4, 3, 3, 0.0F, false));

		legs = new RendererModel(this);
		legs.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(legs);

		left_leg_set = new RendererModel(this);
		left_leg_set.setRotationPoint(0.0F, -2.0F, 0.0F);
//		setRotationAngles(left_legs, 0.0F, 0.0F, 0.1745F);
		legs.addChild(left_leg_set);

		left_leg_1 = new RendererModel(this);
		left_leg_1.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(left_leg_1, -0.5236F, -2.7053F, 0.5236F);
		left_leg_set.addChild(left_leg_1);
		left_leg_1.cubeList.add(new ModelBox(left_leg_1, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

		left_leg_2 = new RendererModel(this);
		left_leg_2.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(left_leg_2, -0.5236F, 3.1416F, 0.5236F);
		left_leg_set.addChild(left_leg_2);
		left_leg_2.cubeList.add(new ModelBox(left_leg_2, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

		left_leg_3 = new RendererModel(this);
		left_leg_3.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(left_leg_3, -0.5236F, 2.7053F, 0.5236F);
		left_leg_set.addChild(left_leg_3);
		left_leg_3.cubeList.add(new ModelBox(left_leg_3, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

		right_leg_set = new RendererModel(this);
		right_leg_set.setRotationPoint(0.0F, -2.0F, 0.0F);
//		setRotationAngles(right_legs, 0.0F, 0.0F, -0.1745F);
		legs.addChild(right_leg_set);

		right_leg_1 = new RendererModel(this);
		right_leg_1.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(right_leg_1, 0.5236F, -0.5236F, -0.5236F);
		right_leg_set.addChild(right_leg_1);
		right_leg_1.cubeList.add(new ModelBox(right_leg_1, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

		right_leg_2 = new RendererModel(this);
		right_leg_2.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(right_leg_2, 0.5236F, 0.0F, -0.5236F);
		right_leg_set.addChild(right_leg_2);
		right_leg_2.cubeList.add(new ModelBox(right_leg_2, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

		right_leg_3 = new RendererModel(this);
		right_leg_3.setRotationPoint(0.0F, 0.0F, 0.0F);
//		setRotationAngles(right_leg_3, 0.5236F, 0.5236F, -0.5236F);
		right_leg_set.addChild(right_leg_3);
		right_leg_3.cubeList.add(new ModelBox(right_leg_3, 20, 8, -4.0F, -0.5F, -0.5F, 4, 0, 1, 0.0F, false));

//		item = new RendererModel(this);
//		item.setRotationPoint(0.0F, -6.0F, 4.0F);
//		setRotationAngle(item, -0.2618F, 0.0F, 0.0F);
//		body.addChild(item);
//		item.cubeList.add(new ModelBox(item, 0, 0, -4.0F, 0.5F, -3.5F, 8, 0, 8, 0.0F, false));
	}

	public void render(GathererAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		body.render(scale);
	}

	public void setRotationAngles(GathererAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {



		head.rotateAngleX = headPitch * (((float) Math.PI) / 180.0F);
		head.rotateAngleY = netHeadYaw * (((float) Math.PI) / 180.0F);

		setRotationAngles(antennae, 0.5236F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F, (MathHelper.sin((limbSwing * 4.00F)) * (0.125F)) + 0.0F);
		setRotationAngles(abdomen, -0.1745F, 0.0F, 0.0F);


		setRotationAngles(left_leg_set, 0.0F, 0.0F, 0.0F);

		setRotationAngles(left_leg_1, -0.0F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 2.6180F, (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);
		setRotationAngles(left_leg_2, -0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 3.1416F, (MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);
		setRotationAngles(left_leg_3, -0.0F,  MathHelper.sin((limbSwing * 4.00F)) * (0.125F) + 2.6180F, (MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);

		setRotationAngles(right_leg_set, 0.0F, 0.0F, -0.0F);

		setRotationAngles(right_leg_1, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.5236F, (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);
		setRotationAngles(right_leg_2, 0.0F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F   , (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);
		setRotationAngles(right_leg_3, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) + 0.5236F, (MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);


	}



	public void setRotationAngles(RendererModel rendererModel, float x, float y, float z) {
		rendererModel.rotateAngleX = x;
		rendererModel.rotateAngleY = y;
		rendererModel.rotateAngleZ = z;
	}

//	> body
//    > head
//        > antennae
//    > thorax
//    > legs
//        > right_legs
//            > right_leg_1
//            > right_leg_2
//            > right_leg_3
//        > left_legs
//            > left_leg_1
//            > left_leg_2
//            > left_leg_3
//    > abdomen

}
