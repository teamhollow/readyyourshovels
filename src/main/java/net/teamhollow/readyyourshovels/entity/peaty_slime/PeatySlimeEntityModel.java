package net.teamhollow.readyyourshovels.entity.peaty_slime;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.entity.Entity;

public class PeatySlimeEntityModel<T extends Entity> extends CompositeEntityModel<T> {
    private final ModelPart cube;

    public PeatySlimeEntityModel() {
        textureWidth = 64;
        textureHeight = 32;

        cube = new ModelPart(this);
        cube.setPivot(0.0F, 24.0F, 0.0F);
        cube.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(cube);
    }
}
