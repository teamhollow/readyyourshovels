package net.teamhollow.readyyourshovels;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.init.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadyYourShovels implements ModInitializer {
    public static final String MOD_ID = "readyyourshovels";
    public static final String MOD_NAME = "Ready Your Shovels";

    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(Items.WOODEN_SHOVEL)
    );

    @Override
    public void onInitialize() {
        log("Initializing");

        new RYSParticles();
        new RYSSoundEvents();
        new RYSPointOfInterests();

        new RYSBlocks();
        new RYSBlockEntities();
        new RYSItems();
        new RYSEntities();

        new RYSFeatures();
        new RYSStructureFeatures();
        new RYSConfiguredFeatures();
        new RYSCarvers();
        new RYSConfiguredCarvers();
        new RYSSurfaceBuilders();
        new RYSConfiguredSurfaceBuilders();

        new RYSBiomes();

        log("Initialized");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static Identifier texture(String path) {
        return new Identifier(MOD_ID, "textures/" + path + ".png");
    }
}
