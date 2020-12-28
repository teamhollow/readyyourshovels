package net.teamhollow.readyyourshovels;

import static net.teamhollow.readyyourshovels.ReadyYourShovels.log;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.teamhollow.readyyourshovels.client.particle.DayrootParticle;
import net.teamhollow.readyyourshovels.client.particle.RYSCrackParticle;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntityRenderer;
import net.teamhollow.readyyourshovels.entity.peaty_slime.PeatySlimeEntityRenderer;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.init.RYSParticles;

@SuppressWarnings("unused")
public class ReadyYourShovelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        log("Initializing client");

        // entity renderers
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;
        INSTANCE.register(
                RYSEntities.GARDEN_ANT,
                (entityRenderDispatcher, context) -> new GardenAntEntityRenderer(entityRenderDispatcher)
        );
        INSTANCE.register(
                RYSEntities.PEATY_SLIME,
                (entityRenderDispatcher, context) -> new PeatySlimeEntityRenderer(entityRenderDispatcher)
        );

        // render layers
        BlockRenderLayerMap brlmInstance = BlockRenderLayerMap.INSTANCE;
        brlmInstance.putBlocks(RenderLayer.getCutout(), RYSBlocks.DAYROOT, RYSBlocks.DAYROOT_PLANT);

        // particles
        ParticleFactoryRegistry pfrInstance = ParticleFactoryRegistry.getInstance();
        pfrInstance.register(RYSParticles.ITEM_PEAT, new RYSCrackParticle.PeatFactory());
        pfrInstance.register(RYSParticles.DAYROOT, DayrootParticle.Factory::new);

        log("Initialized client");
    }
}
