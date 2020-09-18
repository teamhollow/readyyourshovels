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
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "readyyourshovels";
    public static final String MOD_NAME = "Ready Your Shovels";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(Items.WOODEN_SHOVEL)
    );

    @Override
    public void onInitialize() {
        log("Initializing");

        new RYSBlocks();
        new RYSItems();

        log("Initialized");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
    public static void log(String message) {
        log(Level.INFO, message);
    }
}
