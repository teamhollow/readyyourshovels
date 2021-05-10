package net.teamhollow.readyyourshovels;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.teamhollow.readyyourshovels.init.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadyYourShovels implements ModInitializer {
    public static final String MOD_ID = "readyyourshovels";
    public static final String MOD_NAME = "Ready Your Shovels";

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        log("Initializing");

        Reflection.initialize(
            RYSParticles.class,
            RYSSoundEvents.class,
            RYSPointOfInterests.class,
            RYSStats.class,
            RYSScreenHandlers.class,

            RYSBlocks.class,
            RYSBlockEntities.class,
            RYSItems.class,
            RYSEntities.class,

            RYSFeatures.class,
            RYSStructureFeatures.class,
            RYSConfiguredFeatures.class,
            RYSCarvers.class,
            RYSConfiguredCarvers.class,
            RYSSurfaceBuilders.class,
            RYSConfiguredSurfaceBuilders.class,

            RYSBiomes.class
        );

        log("Initialized");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
