package group.rys.client.renderer.model;

import group.rys.common.entity.QueenAntEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;

public class QueenAntModel extends EntityModel<QueenAntEntity> {
    private final RendererModel Body;
    private final RendererModel Head;
    private final RendererModel bone;
    private final RendererModel bone2;
    private final RendererModel Thorax;
    private final RendererModel wing_set;
    private final RendererModel left_wing;
    private final RendererModel right_wing;
    private final RendererModel right_leg_set;
    private final RendererModel right_leg_for;
    private final RendererModel right_leg_back;
    private final RendererModel right_leg_mid;
    private final RendererModel left_leg_set;
    private final RendererModel left_leg_mid;
    private final RendererModel left_leg_for;
    private final RendererModel left_leg_back;
    private final RendererModel Rear;

    public QueenAntModel() {
        textureWidth = 64;
        textureHeight = 64;

        Body = new RendererModel(this);
        Body.setRotationPoint(0.0F, 21.0F, 0.0F);

        Head = new RendererModel(this);
        Head.setRotationPoint(0.0F, -2.0F, -1.5F);
        setRotationAngle(Head, 0.2618F, 0.0F, 0.0F);
        Body.addChild(Head);
        Head.cubeList.add(new ModelBox(Head, 0, 18, -3.0F, -5.0F, -4.5F, 6, 5, 5, 0.0F, false));

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, -3.0F, -2.5F);
        setRotationAngle(bone, 0.5236F, 0.0F, 0.0F);
        Head.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 22, 0, -4.0F, -6.0F, 0.0F, 8, 4, 0, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 0.0F, -3.0F);
        setRotationAngle(bone2, -0.6109F, 0.0F, 0.0F);
        Head.addChild(bone2);

        Thorax = new RendererModel(this);
        Thorax.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.addChild(Thorax);
        Thorax.cubeList.add(new ModelBox(Thorax, 22, 22, -2.5F, -4.0F, -1.0F, 5, 4, 5, 0.0F, false));

        wing_set = new RendererModel(this);
        wing_set.setRotationPoint(0.0F, 0.0F, 0.0F);
        Thorax.addChild(wing_set);

        left_wing = new RendererModel(this);
        left_wing.setRotationPoint(2.0F, -4.0F, 0.0F);
        setRotationAngle(left_wing, -0.2618F, 0.0F, -0.3491F);
        wing_set.addChild(left_wing);
        left_wing.cubeList.add(new ModelBox(left_wing, 0, 12, 0.0F, 0.0F, -1.0F, 8, 0, 6, 0.0F, false));

        right_wing = new RendererModel(this);
        right_wing.setRotationPoint(-2.0F, -4.0F, 0.0F);
        setRotationAngle(right_wing, -0.2618F, 0.0F, 0.3491F);
        wing_set.addChild(right_wing);
        right_wing.cubeList.add(new ModelBox(right_wing, 16, 16, -8.0F, 0.0F, -1.0F, 8, 0, 6, 0.0F, false));

        right_leg_set = new RendererModel(this);
        right_leg_set.setRotationPoint(-1.0F, -1.0F, 0.0F);
        setRotationAngle(right_leg_set, 0.6109F, 0.2618F, -0.3491F);
        Thorax.addChild(right_leg_set);

        right_leg_for = new RendererModel(this);
        right_leg_for.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right_leg_for, 0.5236F, -0.5236F, -0.5236F);
        right_leg_set.addChild(right_leg_for);
        right_leg_for.cubeList.add(new ModelBox(right_leg_for, 21, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        right_leg_back = new RendererModel(this);
        right_leg_back.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right_leg_back, 0.5236F, 0.5236F, -0.5236F);
        right_leg_set.addChild(right_leg_back);
        right_leg_back.cubeList.add(new ModelBox(right_leg_back, 21, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        right_leg_mid = new RendererModel(this);
        right_leg_mid.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(right_leg_mid, 0.5236F, 0.0F, -0.5236F);
        right_leg_set.addChild(right_leg_mid);
        right_leg_mid.cubeList.add(new ModelBox(right_leg_mid, 20, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_set = new RendererModel(this);
        left_leg_set.setRotationPoint(1.0F, -1.0F, 0.0F);
        setRotationAngle(left_leg_set, 0.6109F, -0.2618F, 0.3491F);
        Thorax.addChild(left_leg_set);

        left_leg_mid = new RendererModel(this);
        left_leg_mid.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left_leg_mid, -0.5236F, 3.1416F, 0.5236F);
        left_leg_set.addChild(left_leg_mid);
        left_leg_mid.cubeList.add(new ModelBox(left_leg_mid, 21, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_for = new RendererModel(this);
        left_leg_for.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left_leg_for, -0.5236F, -2.7053F, 0.5236F);
        left_leg_set.addChild(left_leg_for);
        left_leg_for.cubeList.add(new ModelBox(left_leg_for, 21, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        left_leg_back = new RendererModel(this);
        left_leg_back.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(left_leg_back, -0.5236F, 2.7053F, 0.5236F);
        left_leg_set.addChild(left_leg_back);
        left_leg_back.cubeList.add(new ModelBox(left_leg_back, 21, 4, -5.0F, 0.5F, -0.5F, 5, 0, 1, 0.0F, false));

        Rear = new RendererModel(this);
        Rear.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(Rear, -0.1745F, 0.0F, 0.0F);
        Body.addChild(Rear);
        Rear.cubeList.add(new ModelBox(Rear, 0, 0, -4.0F, -8.0F, 3.0F, 8, 6, 6, 0.0F, false));
    }

    public void render(QueenAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Body.render(scale);
    }

    public void setRotationAngles(QueenAntEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.Head.rotateAngleX = headPitch * (((float) Math.PI) / 180.0F);
        this.Head.rotateAngleY = netHeadYaw * (((float) Math.PI) / 180.0F);

        setRotationAngle(this.bone, 0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.1F) - 0.0F, 0.0F);

        setRotationAngle(this.bone2, 0.5236F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F, (MathHelper.sin((limbSwing * 4.00F)) * (0.125F)) + 0.0F);


        setRotationAngle(left_leg_set, 0.0F, 0.0F, 0.0F);
        setRotationAngle(left_leg_for, -0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) - 2.7053F, (-MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);
        setRotationAngle(left_leg_back, -0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 3.1416F, (MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);
        setRotationAngle(left_leg_mid, -0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 2.7053F, (-MathHelper.sin((limbSwing)) * (0.25F)) + 0.5236F);

        setRotationAngle(right_leg_set, 0.0F, 0.0F, -0.0F);
        setRotationAngle(right_leg_for, 0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) - 0.5236F, (MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);
        setRotationAngle(right_leg_back, 0.5236F, -MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 0.0F, (-MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);
        setRotationAngle(right_leg_mid, 0.5236F, MathHelper.sin(limbSwing * 4.00F) * (0.2F) + 0.5236F, (MathHelper.sin((limbSwing)) * (0.25F)) - 0.5236F);

        if (entityIn.isFlying()) {
            setRotationAngle(left_wing, -0.2618F, 0.0F, -0.3491F - MathHelper.sin(ageInTicks * 1.2F) * (0.5F));
            setRotationAngle(right_wing, -0.2618F, 0.0F, 0.3491F + MathHelper.sin(ageInTicks * 1.2F) * (0.5F));
        }

    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}