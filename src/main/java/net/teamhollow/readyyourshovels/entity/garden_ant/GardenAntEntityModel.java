package net.teamhollow.readyyourshovels.entity.garden_ant;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;

public class GardenAntEntityModel<T extends GardenAntEntity> extends CompositeEntityModel<T> {
    private final ModelPart thorax;
    private final ModelPart head;
    private final ModelPart antenna;
    private final ModelPart rear;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftMidLeg;
    private final ModelPart rightMidLeg;
    private final ModelPart leftBackLeg;
    private final ModelPart rightBackLeg;

    public GardenAntEntityModel() {
        textureWidth = 32;
        textureHeight = 32;

        thorax = new ModelPart(this);
        thorax.setPivot(0.0F, 20.75F, 0.5F);
        thorax.setTextureOffset(0, 8).addCuboid(-1.5F, -1.25F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, 20.5F, 0.0F);
        head.setTextureOffset(0, 0).addCuboid(-2.0F, -4.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        antenna = new ModelPart(this);
        antenna.setPivot(0.0F, -3.5F, -3.0F);
        head.addChild(antenna);
        setRotationAngle(antenna, 0.2618F, 0.0F, 0.0F);
        antenna.setTextureOffset(16, 13).addCuboid(-4.0F, -4.4489F, 0.3882F, 8.0F, 4.0F, 0.0F, 0.0F, false);

        rear = new ModelPart(this);
        rear.setPivot(0.0F, 20.5F, 1.5F);
        setRotationAngle(rear, -0.1745F, 0.0F, 0.0F);
        rear.setTextureOffset(18, 0).addCuboid(-2.0F, -2.9772F, -0.7605F, 4.0F, 3.0F, 3.0F, 0.0F, false);

        leftFrontLeg = new ModelPart(this);
        leftFrontLeg.setPivot(0.8F, 21.5F, -0.5F);
        setRotationAngle(leftFrontLeg, 0.0F, 0.4363F, 0.6981F);
        leftFrontLeg.setTextureOffset(20, 8).addCuboid(-0.8738F, -0.3991F, -0.6575F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        rightFrontLeg = new ModelPart(this);
        rightFrontLeg.setPivot(-0.8F, 21.5F, -0.5F);
        setRotationAngle(rightFrontLeg, 0.0F, -0.4363F, -0.6981F);
        rightFrontLeg.setTextureOffset(20, 8).addCuboid(-3.1262F, -0.3991F, -0.6575F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        leftMidLeg = new ModelPart(this);
        leftMidLeg.setPivot(1.0F, 21.5F, 0.5F);
        setRotationAngle(leftMidLeg, 0.0F, 0.0F, 0.6981F);
        leftMidLeg.setTextureOffset(20, 8).addCuboid(-0.9642F, -0.3991F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        rightMidLeg = new ModelPart(this);
        rightMidLeg.setPivot(-1.0F, 21.5F, 0.5F);
        setRotationAngle(rightMidLeg, 0.0F, 0.0F, -0.6981F);
        rightMidLeg.setTextureOffset(20, 8).addCuboid(-3.0358F, -0.3991F, -0.5F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        leftBackLeg = new ModelPart(this);
        leftBackLeg.setPivot(0.8F, 21.5F, 1.5F);
        setRotationAngle(leftBackLeg, 0.0F, -0.3491F, 0.6981F);
        leftBackLeg.setTextureOffset(20, 8).addCuboid(-0.906F, -0.3991F, -0.4202F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        rightBackLeg = new ModelPart(this);
        rightBackLeg.setPivot(-0.8F, 21.5F, 1.5F);
        setRotationAngle(rightBackLeg, 0.0F, 0.3491F, -0.6981F);
        rightBackLeg.setTextureOffset(20, 8).addCuboid(-3.094F, -0.3991F, -0.4202F, 4.0F, 1.0F, 1.0F, 0.0F, false);
}
    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;

        this.rightFrontLeg.pivotZ = -0.5F;
        this.leftFrontLeg.pivotZ = -0.5F;
        this.rightMidLeg.pivotZ = 0.5F;
        this.leftMidLeg.pivotZ = 0.5F;
        this.rightBackLeg.pivotZ = 1.5F;
        this.leftBackLeg.pivotZ = 1.5F;

        this.rightFrontLeg.pivotY = 21.5F;
        this.leftFrontLeg.pivotY = 21.5F;
        this.rightMidLeg.pivotY = 21.5F;
        this.leftMidLeg.pivotY = 21.5F;
        this.rightBackLeg.pivotY = 21.5F;
        this.leftBackLeg.pivotY = 21.5F;
        float f3 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + 0.0F) * 0.6F) * limbSwingAmount;
        float f4 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + (float) Math.PI) * 0.6F) * limbSwingAmount;
        float f5 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + ((float) Math.PI / 2F)) * 0.6F) * limbSwingAmount;
        float f6 = -(MathHelper.cos(limbSwing * 1.6662F * 2.0F + ((float) Math.PI * 1.5F)) * 0.6F) * limbSwingAmount;
        float f7 = 2 * (MathHelper.sin(limbSwing * 1.6662F * (1.0F)) * 0.6F) * limbSwingAmount;
        float f8 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        float f9 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        float f10 = 2 * (MathHelper.sin(limbSwing * 1.6662F + 0.0F) * 0.6F) * limbSwingAmount;
        this.rightFrontLeg.pivotY += f3;
        this.leftFrontLeg.pivotY += -f3;
        this.rightMidLeg.pivotY += f4;
        this.leftMidLeg.pivotY += -f5;
        this.rightBackLeg.pivotY += f6;
        this.leftBackLeg.pivotY += -f6;
        this.rightFrontLeg.pivotZ += f7;
        this.leftFrontLeg.pivotZ += f7;
        this.rightMidLeg.pivotZ += -f8;
        this.leftMidLeg.pivotZ += -f9;
        this.rightBackLeg.pivotZ += f10;
        this.leftBackLeg.pivotZ += f10;
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(thorax, head, rear, leftFrontLeg, rightFrontLeg, leftMidLeg, rightMidLeg, leftBackLeg, rightBackLeg);
    }

    public void setRotationAngle(ModelPart part, float pitch, float yaw, float roll) {
        part.pitch = pitch;
        part.yaw = yaw;
        part.roll = roll;
    }
}
