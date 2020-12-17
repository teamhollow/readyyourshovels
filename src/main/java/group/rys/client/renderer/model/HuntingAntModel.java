package group.rys.client.renderer.model;

import group.rys.common.entity.HuntingAntEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HuntingAntModel extends EntityModel<HuntingAntEntity> {
    private final RendererModel Body;
    private final RendererModel Head;
    private final RendererModel bone;
    private final RendererModel bone2;
    private final RendererModel Thorax;
    private final RendererModel right_leg_set;
    private final RendererModel right_leg_for;
    private final RendererModel right_leg_back;
    private final RendererModel right_leg_mid;
    private final RendererModel left_leg_set;
    private final RendererModel left_leg_mid;
    private final RendererModel left_leg_for;
    private final RendererModel left_leg_back;
    private final RendererModel Rear;

    public HuntingAntModel() {
        textureWidth = 32;
        textureHeight = 32;

        Body = new RendererModel(this);
        Body.setRotationPoint(0.0F, 21.0F, 0.0F);

        Head = new RendererModel(this);
        Head.setRotationPoint(0.0F, -2.0F, -1.5F);
        Body.addChild(Head);
        Head.cubeList.add(new ModelBox(Head, 0, 9, -3.0F, -3.0F, -3.5F, 6, 4, 4, 0.0F, false));

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, -3.0F, -2.5F);
        Head.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 16, 9, -4.0F, -4.0F, 0.0F, 8, 4, 0, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 0.0F, -2.5F);
        setRotationAngle(bone2, 0.0F, 0.0F, 0.0F);
        Head.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 9, 17, -3.0F, 1.0F, 0.0F, 6, 2, 0, 0.0F, false));

        Thorax = new RendererModel(this);
        Thorax.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Thorax);
        Thorax.cubeList.add(new ModelBox(Thorax, 0, 17, -1.5F, -3.0F, -1.0F, 3, 3, 3, 0.0F, false));

        right_leg_set = new RendererModel(this);
        right_leg_set.setRotationPoint(-1.0F, -1.0F, 0.0F);
        setRotationAngle(right_leg_set, 0.0F, 0.0F, -0.3491F);
        Thorax.addChild(right_leg_set);

        right_leg_for = new RendererModel(this);
        right_leg_for.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg_set.addChild(right_leg_for);
        right_leg_for.cubeList.add(new ModelBox(right_leg_for, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        right_leg_back = new RendererModel(this);
        right_leg_back.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg_set.addChild(right_leg_back);
        right_leg_back.cubeList.add(new ModelBox(right_leg_back, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        right_leg_mid = new RendererModel(this);
        right_leg_mid.setRotationPoint(0.0F, 0.0F, 0.0F);
        right_leg_set.addChild(right_leg_mid);
        right_leg_mid.cubeList.add(new ModelBox(right_leg_mid, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_set = new RendererModel(this);
        left_leg_set.setRotationPoint(1.0F, -1.0F, 0.0F);
        Thorax.addChild(left_leg_set);

        left_leg_mid = new RendererModel(this);
        left_leg_mid.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg_set.addChild(left_leg_mid);
        left_leg_mid.cubeList.add(new ModelBox(left_leg_mid, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_for = new RendererModel(this);
        left_leg_for.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg_set.addChild(left_leg_for);
        left_leg_for.cubeList.add(new ModelBox(left_leg_for, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_back = new RendererModel(this);
        left_leg_back.setRotationPoint(0.0F, 0.0F, 0.0F);
        left_leg_set.addChild(left_leg_back);
        left_leg_back.cubeList.add(new ModelBox(left_leg_back, 9, 19, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        Rear = new RendererModel(this);
        setRotationAngle(Rear, -0.1745F, 0.0F, 0.0F);
        Body.addChild(Rear);
        Rear.cubeList.add(new ModelBox(Rear, 0, 0, -3.0F, -6.0F, 1.0F, 6, 4, 5, 0.0F, false));
    }

    public void render(HuntingAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.Body.render(scale);
    }

    public void setRotationAngles(HuntingAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.Head.rotateAngleX = headPitch * (((float) Math.PI) / 180.0F);
        this.Head.rotateAngleY = netHeadYaw * (((float) Math.PI) / 180.0F);

        setRotationAngle(this.bone, 0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.1F) - 0.0F, 0.0F);

        setRotationAngle(this.bone2, 0.5236F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F, (MathHelper.sin((limbSwing * 4.00F)) * (0.125F)) + 0.0F);


        setRotationAngle(left_leg_set, 0.0F, 0.0F, 0.0F);
        setRotationAngle(left_leg_for, -0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) -2.7053F, (-MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);
        setRotationAngle(left_leg_back, -0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) +3.1416F, (MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);
        setRotationAngle(left_leg_mid, -0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) +2.7053F, (-MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);

        setRotationAngle(right_leg_set, 0.0F,  0.0F, -0.0F);
        setRotationAngle(right_leg_for, 0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) - 0.5236F, (MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);
        setRotationAngle(right_leg_back, 0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 0.0F, (-MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);
        setRotationAngle(right_leg_mid, 0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 0.5236F, (MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);

    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
