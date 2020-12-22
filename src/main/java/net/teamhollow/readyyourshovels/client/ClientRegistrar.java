package net.teamhollow.readyyourshovels.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.teamhollow.readyyourshovels.client.render.GardenAntRenderer;
import net.teamhollow.readyyourshovels.registry.RYSEntities;

@OnlyIn(Dist.CLIENT)
public class ClientRegistrar {
    public static void setup(final FMLCommonSetupEvent event) {
        ClientRegistrar.renderEntity();
    }

    private static void renderEntity() {
        RenderingRegistry.registerEntityRenderingHandler(RYSEntities.GARDEN_ANT, GardenAntRenderer::new);
    }
}
