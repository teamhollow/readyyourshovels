package net.teamhollow.readyyourshovels.entity.ant.garden_ant;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GardenAntEntityModel<T extends GardenAntEntity> extends CompositeEntityModel<T> {
    private final ModelPart thorax;
    private final ModelPart head;
    private final ModelPart pinchers;
    private final ModelPart antennaLeft;
    private final ModelPart antennaRight;
    private final ModelPart abdomen;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftMidLeg;
    private final ModelPart rightMidLeg;
    private final ModelPart leftBackLeg;
    private final ModelPart rightBackLeg;

    public GardenAntEntityModel() {
        textureWidth = 32;
        textureHeight = 16;
        thorax = new ModelPart(this);
        thorax.setPivot(0.0F, 20.75F, 0.0F);
        thorax.setTextureOffset(7, 9).addCuboid(-1.5F, -1.25F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, 20.5F, -0.75F);
        head.setTextureOffset(16, 4).addCuboid(-2.0F, -3.5F, -3.75F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        pinchers = new ModelPart(this);
        pinchers.setPivot(0.0F, 0.0F, -3.25F);
        head.addChild(pinchers);
        setRotationAngle(pinchers, 0.4363F, 0.0F, 0.0F);
        pinchers.setTextureOffset(19, 12).addCuboid(-1.5F, -0.25F, -1.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

        antennaLeft = new ModelPart(this);
        antennaLeft.setPivot(-1.5F, -3.5F, -3.25F);
        head.addChild(antennaLeft);
        setRotationAngle(antennaLeft, -0.2618F, 0.4363F, 0.0F);
        antennaLeft.setTextureOffset(0, 4).addCuboid(0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        antennaRight = new ModelPart(this);
        antennaRight.setPivot(1.5F, -3.5F, -3.25F);
        head.addChild(antennaRight);
        setRotationAngle(antennaRight, -0.2618F, -0.4363F, 0.0F);
        antennaRight.setTextureOffset(0, 4).addCuboid(-0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        abdomen = new ModelPart(this);
        abdomen.setPivot(0.0F, 19.5F, 1.0F);
        setRotationAngle(abdomen, -0.4363F, 0.0F, 0.0F);
        abdomen.setTextureOffset(0, 0).addCuboid(-2.5F, -1.75F, 0.0F, 5.0F, 3.0F, 5.0F, 0.0F, false);

        leftFrontLeg = new ModelPart(this);
        leftFrontLeg.setPivot(-1.05F, 21.5F, -0.75F);
        setRotationAngle(leftFrontLeg, 0.0F, -0.4363F, -0.7854F);
        leftFrontLeg.setTextureOffset(0, 10).addCuboid(-3.9341F, -0.3232F, -0.3481F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightFrontLeg = new ModelPart(this);
        rightFrontLeg.setPivot(1.55F, 21.5F, -0.75F);
        setRotationAngle(rightFrontLeg, 0.0F, 0.4363F, 0.829F);
        rightFrontLeg.setTextureOffset(0, 10).addCuboid(-0.3649F, 0.053F, -0.4876F, 4.0F, 0.0F, 1.0F, 0.0F, false);

        leftMidLeg = new ModelPart(this);
        leftMidLeg.setPivot(-1.0F, 21.5F, 0.5F);
        setRotationAngle(leftMidLeg, 0.0F, 0.0F, -0.7854F);
        leftMidLeg.setTextureOffset(0, 10).addCuboid(-4.0F, -0.5F, -1.0F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightMidLeg = new ModelPart(this);
        rightMidLeg.setPivot(1.0F, 21.5F, -0.25F);
        setRotationAngle(rightMidLeg, 0.0F, 0.0F, 0.7854F);
        rightMidLeg.setTextureOffset(0, 10).addCuboid(0.0F, -0.5F, -0.25F, 4.0F, 0.0F, 1.0F, 0.0F, false);

        leftBackLeg = new ModelPart(this);
        leftBackLeg.setPivot(-1.55F, 21.5F, 0.5F);
        setRotationAngle(leftBackLeg, 0.0F, 0.3491F, -0.7854F);
        leftBackLeg.setTextureOffset(0, 10).addCuboid(-3.5437F, 0.0303F, -0.3789F, 4.0F, 0.0F, 1.0F, 0.0F, true);

        rightBackLeg = new ModelPart(this);
        rightBackLeg.setPivot(1.55F, 21.5F, 0.75F);
        setRotationAngle(rightBackLeg, 0.0F, -0.3491F, 0.7854F);
        rightBackLeg.setTextureOffset(0, 10).addCuboid(-0.5418F, 0.0303F, -0.6138F, 4.0F, 0.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        limbSwing /= 2;

        head.pitch = headPitch * (((float) Math.PI) / 180.0F);
        head.yaw = netHeadYaw * (((float) Math.PI) / 180.0F);

        setRotationAngle(antennaLeft, 0.5236F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F, (MathHelper.sin((limbSwing * 4.00F)) * (0.125F)) + 0.0F);
        setRotationAngle(antennaRight, 0.5236F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F, (MathHelper.sin((limbSwing * 4.00F)) * (0.125F)) + 0.0F);
        setRotationAngle(abdomen, -0.1745F, 0.0F, 0.0F);

        setRotationAngle(rightFrontLeg, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) + 0.5236F, (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);
        setRotationAngle(rightMidLeg, 0.0F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) + 0.0F   , (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);
        setRotationAngle(rightBackLeg, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.5236F, (MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) + 0.5236F);

        setRotationAngle(leftFrontLeg, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.5236F, (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);
        setRotationAngle(leftMidLeg, 0.0F, MathHelper.sin((limbSwing * 4.00F)) * (0.125F) - 0.0F   , (-MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);
        setRotationAngle(leftBackLeg, 0.0F, -MathHelper.sin((limbSwing * 4.00F)) * (0.125F) + 0.5236F, (MathHelper.sin((limbSwing * 4.00F)) * (0.25F)) - 0.5236F);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(thorax, head, abdomen, leftFrontLeg, rightFrontLeg, leftMidLeg, rightMidLeg, leftBackLeg, rightBackLeg);
    }

    public void setRotationAngle(ModelPart part, float pitch, float yaw, float roll) {
        part.pitch = pitch;
        part.yaw = yaw;
        part.roll = roll;
    }
}
