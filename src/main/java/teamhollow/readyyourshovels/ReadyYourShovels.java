package teamhollow.readyyourshovels;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import teamhollow.readyyourshovels.client.ClientRegistrar;

@Mod(ReadyYourShovels.MODID)
public class ReadyYourShovels {
    public static final String MODID = "readyyourshovels";

    @SuppressWarnings("deprecation")
    public ReadyYourShovels() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRegistrar::setup));

        MinecraftForge.EVENT_BUS.register(this);
    }
}
