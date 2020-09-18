package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSItems {
    public static final Item PEAT = register("peat");

    public RYSItems() {
        FuelRegistry.INSTANCE.add(RYSItems.PEAT, 800);
    }

    public static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ReadyYourShovels.MOD_ID, id), item);
    }
    public static Item register(String id) {
        return register(id, new Item(new Item.Settings().group(ReadyYourShovels.ITEM_GROUP)));
    }
}
