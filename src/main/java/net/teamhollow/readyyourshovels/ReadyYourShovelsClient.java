package net.teamhollow.readyyourshovels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.client.init.RYSEntityModelLayers;
import net.teamhollow.readyyourshovels.client.particle.DayrootParticle;
import net.teamhollow.readyyourshovels.client.particle.RYSCrackParticle;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntityRenderer;
import net.teamhollow.readyyourshovels.entity.peaty_slime.PeatySlimeEntityRenderer;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.init.RYSParticles;

@SuppressWarnings("unused")
public class ReadyYourShovelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReadyYourShovels.log("Initializing client");

        new RYSEntityModelLayers();

        // entity renderers
        EntityRendererRegistry INSTANCE = EntityRendererRegistry.INSTANCE;
        INSTANCE.register(RYSEntities.GARDEN_ANT, GardenAntEntityRenderer::new);
        INSTANCE.register(RYSEntities.PEATY_SLIME, PeatySlimeEntityRenderer::new);

        // render layers
        BlockRenderLayerMap brlmInstance = BlockRenderLayerMap.INSTANCE;
        brlmInstance.putBlocks(RenderLayer.getCutout(), RYSBlocks.DAYROOT, RYSBlocks.DAYROOT_PLANT, RYSBlocks.TOUGHROOT, RYSBlocks.CAVE_CARROT);

        // particles
        ParticleFactoryRegistry pfrInstance = ParticleFactoryRegistry.getInstance();
        pfrInstance.register(RYSParticles.ITEM_COMBUSTING_PEAT, new RYSCrackParticle.CombustingPeatFactory());
        pfrInstance.register(RYSParticles.DAYROOT, DayrootParticle.Factory::new);

        ReadyYourShovels.log("Initialized client");
    }

    public static Identifier texture(String path) {
        return new Identifier(ReadyYourShovels.MOD_ID, "textures/" + path + ".png");
    }
}
