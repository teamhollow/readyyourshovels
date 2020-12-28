package net.teamhollow.readyyourshovels;

import static net.teamhollow.readyyourshovels.ReadyYourShovels.log;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntityRenderer;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class ReadyYourShovelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        log("Initializing client");

        this.registerRenderers();

        BlockRenderLayerMap brlmInstance = BlockRenderLayerMap.INSTANCE;
        brlmInstance.putBlocks(RenderLayer.getCutout(), RYSBlocks.DAYROOT, RYSBlocks.DAYROOT_PLANT);

        log("Initialized client");
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
