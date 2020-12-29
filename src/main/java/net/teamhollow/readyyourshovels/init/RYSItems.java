package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.item.vanilla.PublicMusicDiscItem;

@SuppressWarnings("unused")
public class RYSItems {
    public static final Item PEAT = register("peat");
    public static final Item COMBUSTING_PEAT = register("combusting_peat");

    public static final Item MUSIC_DISC_COLLY = register("music_disc_colly", new PublicMusicDiscItem(
            12, RYSSoundEvents.MUSIC_DISC_COLLY,
            new FabricItemSettings()
                .maxCount(1)
                .rarity(Rarity.RARE)
                .group(ReadyYourShovels.ITEM_GROUP)
            )
    );

    public RYSItems() {
        FuelRegistry frInstance = FuelRegistry.INSTANCE;
        frInstance.add(RYSItems.PEAT, 800);
        frInstance.add(RYSItems.COMBUSTING_PEAT, 1400);
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ReadyYourShovels.MOD_ID, id), item);
    }
    private static Item register(String id) {
        return register(id, new Item(new FabricItemSettings().group(ReadyYourShovels.ITEM_GROUP)));
    }
}
