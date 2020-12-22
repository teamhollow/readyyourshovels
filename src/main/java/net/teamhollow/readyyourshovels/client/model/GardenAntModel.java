package net.teamhollow.readyyourshovels.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.teamhollow.readyyourshovels.entity.GardenAntEntity;

public class GardenAntModel<T extends GardenAntEntity> extends SegmentedModel<T> {
    private final ModelRenderer thorax;
    private final ModelRenderer head;
    private final ModelRenderer pinchers;
    private final ModelRenderer antennaLeft;
    private final ModelRenderer antennaRight;
    private final ModelRenderer abdomen;
    private final ModelRenderer leftFrontLeg;
    private final ModelRenderer rightFrontLeg;
    private final ModelRenderer leftMidLeg;
    private final ModelRenderer rightMidLeg;
    private final ModelRenderer leftBackLeg;
    private final ModelRenderer rightBackLeg;

    public GardenAntModel() {
        textureWidth = 32;
        textureHeight = 16;
        thorax = new ModelRenderer(this);
        thorax.setRotationPoint(0.0F, 20.75F, 0.0F);
        thorax.setTextureOffset(7, 9).addBox(-1.5F, -1.25F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 20.5F, -0.75F);
        head.setTextureOffset(16, 4).addBox(-2.0F, -3.5F, -3.75F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        pinchers = new ModelRenderer(this);
        pinchers.setRotationPoint(0.0F, 0.0F, -3.25F);
        head.addChild(pinchers);
        setRotationAngle(pinchers, 0.4363F, 0.0F, 0.0F);
        pinchers.setTextureOffset(19, 12).addBox(-1.5F, -0.25F, -1.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

        antennaLeft = new ModelRenderer(this);
        antennaLeft.setRotationPoint(-1.5F, -3.5F, -3.25F);
        head.addChild(antennaLeft);
        setRotationAngle(antennaLeft, -0.2618F, 0.4363F, 0.0F);
        antennaLeft.setTextureOffset(0, 4).addBox(0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        antennaRight = new ModelRenderer(this);
        antennaRight.setRotationPoint(1.5F, -3.5F, -3.25F);
        head.addChild(antennaRight);
        setRotationAngle(antennaRight, -0.2618F, -0.4363F, 0.0F);
        antennaRight.setTextureOffset(0, 4).addBox(-0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        abdomen = new ModelRenderer(this);
        abdomen.setRotationPoint(0.0F, 19.5F, 1.0F);
        setRotationAngle(abdomen, -0.4363F, 0.0F, 0.0F);
        abdomen.setTextureOffset(0, 0).addBox(-2.5F, -1.75F, 0.0F, 5.0F, 3.0F, 5.0F, 0.0F, false);

        leftFrontLeg = new ModelRenderer(this);
        leftFrontLeg.setRotationPoint(-1.05F, 21.5F, -0.75F);
        setRotationAngle(leftFrontLeg, 0.0F, -0.4363F, -0.7854F);
        leftFrontLeg.setTextureOffset(0, 10).addBox(-3.9341F, -0.3232F, -0.3481F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightFrontLeg = new ModelRenderer(this);
        rightFrontLeg.setRotationPoint(1.55F, 21.5F, -0.75F);
        setRotationAngle(rightFrontLeg, 0.0F, 0.4363F, 0.829F);
        rightFrontLeg.setTextureOffset(0, 10).addBox(-0.3649F, 0.053F, -0.4876F, 4.0F, 0.0F, 1.0F, 0.0F, false);

        leftMidLeg = new ModelRenderer(this);
        leftMidLeg.setRotationPoint(-1.0F, 21.5F, 0.5F);
        setRotationAngle(leftMidLeg, 0.0F, 0.0F, -0.7854F);
        leftMidLeg.setTextureOffset(0, 10).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightMidLeg = new ModelRenderer(this);
        rightMidLeg.setRotationPoint(1.0F, 21.5F, -0.25F);
        setRotationAngle(rightMidLeg, 0.0F, 0.0F, 0.7854F);
        rightMidLeg.setTextureOffset(0, 10).addBox(0.0F, -0.5F, -0.25F, 4.0F, 0.0F, 1.0F, 0.0F, false);

        leftBackLeg = new ModelRenderer(this);
        leftBackLeg.setRotationPoint(-1.55F, 21.5F, 0.5F);
        setRotationAngle(leftBackLeg, 0.0F, 0.3491F, -0.7854F);
        leftBackLeg.setTextureOffset(0, 10).addBox(-3.5437F, 0.0303F, -0.3789F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightBackLeg = new ModelRenderer(this);
        rightBackLeg.setRotationPoint(1.55F, 21.5F, 0.75F);
        setRotationAngle(rightBackLeg, 0.0F, -0.3491F, 0.7854F);
        rightBackLeg.setTextureOffset(0, 10).addBox(-0.5418F, 0.0303F, -0.6138F, 4.0F, 0.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;

        this.rightFrontLeg.rotationPointZ = -0.75F;
        this.leftFrontLeg.rotationPointZ = -0.75F;
        this.rightMidLeg.rotationPointZ = -0.25F;
        this.leftMidLeg.rotationPointZ = 0.5F;
        this.rightBackLeg.rotationPointZ = 0.75F;
        this.leftBackLeg.rotationPointZ = 0.5F;

        this.rightFrontLeg.rotateAngleY = 21.5F;
        this.leftFrontLeg.rotateAngleY = 21.5F;
        this.rightMidLeg.rotateAngleY = 21.5F;
        this.leftMidLeg.rotateAngleY = 21.5F;
        this.rightBackLeg.rotateAngleY = 21.5F;
        this.leftBackLeg.rotateAngleY = 21.5F;
        float f3 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + 0.0F) * 0.6F) * limbSwingAmount;
        float f4 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + (float) Math.PI) * 0.6F) * limbSwingAmount;
        float f5 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.6F) * limbSwingAmount;
        float f6 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + ((float) Math.PI * 1.5F)) * 0.6F) * limbSwingAmount;
        float f7 = 2 * (MathHelper.sin(limbSwing * 1.6662F * (1.0F)) * 0.6F) * limbSwingAmount;
        float f8 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        float f9 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        float f10 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        this.rightFrontLeg.rotateAngleY += f3;
        this.leftFrontLeg.rotateAngleY += -f3;
        this.rightMidLeg.rotateAngleY += f4;
        this.leftMidLeg.rotateAngleY += -f5;
        this.rightBackLeg.rotateAngleY += f6;
        this.leftBackLeg.rotateAngleY += -f6;
        this.rightFrontLeg.rotationPointZ += f7;
        this.leftFrontLeg.rotationPointZ += f7;
        this.rightMidLeg.rotationPointZ += -f8;
        this.leftMidLeg.rotationPointZ += -f9;
        this.rightBackLeg.rotationPointZ += f10;
        this.leftBackLeg.rotationPointZ += f10;
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(thorax, head, abdomen, leftFrontLeg, rightFrontLeg, leftMidLeg, rightMidLeg, leftBackLeg, rightBackLeg);
    }

    public void setRotationAngle(ModelRenderer part, float pitch, float yaw, float roll) {
        part.rotateAngleX = pitch;
        part.rotateAngleY = yaw;
        part.rotateAngleZ = roll;
    }
}
