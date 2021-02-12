package net.teamhollow.readyyourshovels.entity.ant.garden_ant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings("all")
@Environment(EnvType.CLIENT)
public class GardenAntEntityModel<T extends GardenAntEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
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

    public GardenAntEntityModel(ModelPart root) {
        this.root = root;
        this.thorax = root.getChild("thorax");
        this.head = root.getChild("head");
        this.pinchers = this.head.getChild("pinchers");
        this.antennaLeft = this.head.getChild("antenna_left");
        this.antennaRight = this.head.getChild("antenna_right");
        this.abdomen = root.getChild("abdomen");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftMidLeg = root.getChild("left_mid_leg");
        this.rightMidLeg = root.getChild("right_mid_leg");
        this.leftBackLeg = root.getChild("left_back_leg");
        this.rightBackLeg = root.getChild("right_back_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
            "thorax",
            ModelPartBuilder.create()
                .uv(7, 9)
                .cuboid(-1.5F, -1.25F, -1.5F, 3.0F, 2.0F, 3.0F),
            ModelTransform.pivot(0.0F, 20.75F, 0.0F)
        );

        ModelPartData headData = modelPartData.addChild(
            "head",
            ModelPartBuilder.create()
                .uv(16, 4)
                .cuboid(-2.0F, -3.5F, -3.75F, 4.0F, 4.0F, 4.0F),
            ModelTransform.of(0.0F, 20.5F, -0.75F, 0.0F, 0.0F, (float)Math.toRadians(-3.25F))
        );

        headData.addChild(
        "pinchers",
            ModelPartBuilder.create()
                .uv(19, 12)
                .cuboid(-1.5F, -0.25F, -1.25F, 3.0F, 0.0F, 1.0F),
            ModelTransform.of(0.0F, 0.0F, -3.25F, 0.4363F, 0.0F, 0.0F)
        );

        headData.addChild(
        "antenna_left",
            ModelPartBuilder.create()
                .uv(0, 4)
                .cuboid(0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F),
            ModelTransform.of(-1.5F, -3.5F, -3.25F, -0.2618F, 0.4363F, 0.0F)
        );

        headData.addChild(
        "antenna_right",
            ModelPartBuilder.create()
                .uv(0, 4)
                .cuboid(-0.25F, -1.5F, -4.25F, 0.0F, 2.0F, 4.0F),
            ModelTransform.of(1.5F, -3.5F, -3.25F, -0.2618F, -0.4363F, 0.0F)
        );

        modelPartData.addChild(
        "abdomen",
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-2.5F, -1.75F, 0.0F, 5.0F, 3.0F, 5.0F),
            ModelTransform.of(0.0F, 19.5F, 1.0F, -0.4363F, 0.0F, 0.0F)
        );

        modelPartData.addChild(
        "left_front_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(-3.9341F, -0.3232F, -0.3481F, 4.0F, 0.0F, 1.0F)
                .mirrored(),
            ModelTransform.of(-1.05F, 21.5F, -0.75F, 0.0F, -0.4363F, -0.7854F)
        );

        modelPartData.addChild(
        "right_front_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(-0.3649F, 0.053F, -0.4876F, 4.0F, 0.0F, 1.0F),
            ModelTransform.of(1.55F, 21.5F, -0.75F, 0.0F, 0.4363F, 0.829F)
        );

        modelPartData.addChild(
        "left_mid_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(-4.0F, -0.5F, -1.0F, 4.0F, 0.0F, 1.0F)
                .mirrored(),
            ModelTransform.of(-1.0F, 21.5F, 0.5F, 0.0F, 0.0F, -0.7854F)
        );

        modelPartData.addChild(
        "right_mid_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(0.0F, -0.5F, -0.25F, 4.0F, 0.0F, 1.0F),
            ModelTransform.of(1.0F, 21.5F, -0.25F, 0.0F, 0.0F, 0.7854F)
        );

        modelPartData.addChild(
        "left_back_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(-3.5437F, 0.0303F, -0.3789F, 4.0F, 0.0F, 1.0F)
                .mirrored(),
            ModelTransform.of(-1.55F, 21.5F, 0.5F, 0.0F, 0.3491F, -0.7854F)
        );

        modelPartData.addChild(
        "right_back_leg",
            ModelPartBuilder.create()
                .uv(0, 10)
                .cuboid(-0.5418F, 0.0303F, -0.6138F, 4.0F, 0.0F, 1.0F),
            ModelTransform.of(1.55F, 21.5F, 0.75F, 0.0F, -0.3491F, 0.7854F)
        );

        return TexturedModelData.of(modelData, 32, 16);
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
    public ModelPart getPart() {
        return this.root;
    }

    public void setRotationAngle(ModelPart part, float pitch, float yaw, float roll) {
        part.pitch = pitch;
        part.yaw = yaw;
        part.roll = roll;
    }
}
