package net.teamhollow.readyyourshovels;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.teamhollow.readyyourshovels.client.ClientRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ReadyYourShovels.MOD_ID)
public class ReadyYourShovels {
    public static final String MOD_ID = "readyyourshovels";
    public static final String MOD_NAME = "Ready Your Shovels";

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SuppressWarnings("deprecation")
    public ReadyYourShovels() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRegistrar::setup));

        MinecraftForge.EVENT_BUS.register(this);
    }
}
