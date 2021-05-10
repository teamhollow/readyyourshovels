package net.teamhollow.readyyourshovels.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovelsClient;
import net.teamhollow.readyyourshovels.client.gui.screen.screen.recipebook.KilnRecipeBookScreen;
import net.teamhollow.readyyourshovels.screen.KilnScreenHandler;

@Environment(EnvType.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnScreenHandler> {
    private static final Identifier TEXTURE = ReadyYourShovelsClient.texture("gui/container/kiln");

    public KilnScreen(KilnScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new KilnRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
