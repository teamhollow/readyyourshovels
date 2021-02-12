package net.teamhollow.readyyourshovels.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.entity.peaty_slime.PeatySlimeEntity;
import net.teamhollow.readyyourshovels.mixin.client.EntityModelLayersInvoker;

@Environment(EnvType.CLIENT)
public class RYSEntityModelLayers {
    public static final EntityModelLayer GARDEN_ANT = registerMain(GardenAntEntity.id);
    public static final EntityModelLayer PEATY_SLIME = registerMain(PeatySlimeEntity.id);

    public RYSEntityModelLayers() {}

    private static EntityModelLayer registerMain(String id) {
        return EntityModelLayersInvoker.register(new Identifier(ReadyYourShovels.MOD_ID, id).toString(), "main");
    }
}
