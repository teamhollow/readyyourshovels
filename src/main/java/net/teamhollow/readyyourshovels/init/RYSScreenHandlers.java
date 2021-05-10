package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.screen.KilnScreenHandler;

public class RYSScreenHandlers {
    public static final ScreenHandlerType<KilnScreenHandler> KILN = ScreenHandlerRegistry.registerSimple(new Identifier(ReadyYourShovels.MOD_ID, "kiln"), KilnScreenHandler::new);
}
