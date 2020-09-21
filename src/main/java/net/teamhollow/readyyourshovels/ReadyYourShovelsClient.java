package net.teamhollow.readyyourshovels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntityRenderer;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class ReadyYourShovelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.registerRenderers();
    }

    @Environment(EnvType.CLIENT)
    public void registerRenderers() {
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;

        INSTANCE.register(
            RYSEntities.GARDEN_ANT,
            (entityRenderDispatcher, context) -> new GardenAntEntityRenderer(entityRenderDispatcher)
        );
    }
}
