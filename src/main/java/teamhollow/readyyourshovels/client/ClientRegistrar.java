package teamhollow.readyyourshovels.client;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import teamhollow.readyyourshovels.client.render.GardenAntRenderer;
import teamhollow.readyyourshovels.registry.RYSEntity;

public class ClientRegistrar {
    public static void setup(final FMLCommonSetupEvent event) {
        ClientRegistrar.renderEntity();
    }

    private static void renderEntity() {
        RenderingRegistry.registerEntityRenderingHandler(RYSEntity.GARDEN_ANT, GardenAntRenderer::new);
    }
}
