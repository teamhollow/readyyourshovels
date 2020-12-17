package group.rys.client.renderer.model;
//Made with Blockbench
//Paste this code into your mod.

import group.rys.common.entity.HuntingAntLarvaeEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

public class HuntingAntLarvaeModel<T extends HuntingAntLarvaeEntity> extends EntityModel<T> {
    private final RendererModel Body;
    private final RendererModel Head;
    private final RendererModel bone3;
    private final RendererModel bone;
    private final RendererModel bone2;

    public HuntingAntLarvaeModel() {
        textureWidth = 32;
        textureHeight = 32;

        Body = new RendererModel(this);
        Body.setRotationPoint(0.0F, 21.0F, 0.0F);

        Head = new RendererModel(this);
        Head.setRotationPoint(0.0F, -2.0F, -1.5F);
        setRotationAngle(Head, 0.0873F, 0.0F, 0.0F);
        Body.addChild(Head);
        Head.cubeList.add(new ModelBox(Head, 0, 16, -2.0F, -4.0F, 2.5F, 4, 4, 3, 0.0F, false));
        Head.cubeList.add(new ModelBox(Head, 0, 0, -3.0F, -3.0F, -3.5F, 6, 4, 6, 0.0F, false));

        bone3 = new RendererModel(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        setRotationAngle(bone3, 0.1745F, 0.0F, 0.0F);
        Head.addChild(bone3);
        bone3.cubeList.add(new ModelBox(bone3, 0, 25, -2.0F, 0.0F, -2.0F, 4, 2, 5, 0.0F, false));

        bone = new RendererModel(this);
        bone.setRotationPoint(0.0F, -3.0F, -2.5F);
        setRotationAngle(bone, -0.6981F, 0.0F, 0.0F);
        Head.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 0, 10, -4.0F, -4.0F, 0.0F, 8, 4, 0, 0.0F, false));

        bone2 = new RendererModel(this);
        bone2.setRotationPoint(0.0F, 0.0F, -3.0F);
        setRotationAngle(bone2, -0.6109F, 0.0F, 0.0F);
        Head.addChild(bone2);
        bone2.cubeList.add(new ModelBox(bone2, 0, 14, -3.0F, 1.0F, 0.0F, 6, 2, 0, 0.0F, false));
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Body.render(f5);
    }

    public void singleRender(float scale) {
        Body.render(scale);
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}