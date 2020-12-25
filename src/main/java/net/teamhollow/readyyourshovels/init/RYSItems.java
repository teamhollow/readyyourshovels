package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.item.vanilla.PublicMusicDiscItem;

@SuppressWarnings("unused")
public class RYSItems {
    public static final Item PEAT = register("peat");
    public static final Item MUSIC_DISC_COLLY = register("music_disc_colly", new PublicMusicDiscItem(
            12, RYSSoundEvents.MUSIC_DISC_COLLY,
            new FabricItemSettings()
                .maxCount(1)
                .rarity(Rarity.RARE)
                .group(ReadyYourShovels.ITEM_GROUP)
            )
    );

    //
    // FRUITS
    //

    public static final Item ROTTEN_APPLE = register(
        "rotten_apple",
        new Item(
            new FabricItemSettings()
                .food(new FoodComponent.Builder()
                .hunger(2).saturationModifier(0.1F)
                .statusEffect(
                    new StatusEffectInstance(
                        StatusEffects.HUNGER, 600, 0
                    ), 0.7F)
                .build()
            ).group(ReadyYourShovels.ITEM_GROUP)
        )
    );

    public static final Item ORANGE = register(
        "orange",
        new Item(
            new FabricItemSettings()
                .food(new FoodComponent.Builder()
                .hunger(4).saturationModifier(0.3F)
                .build()
            ).group(ReadyYourShovels.ITEM_GROUP)
        )
    );
    public static final Item ROTTEN_ORANGE = register(
        "rotten_orange",
        new Item(
            new FabricItemSettings()
                .food(new FoodComponent.Builder()
                .hunger(2).saturationModifier(0.1F)
                .statusEffect(
                    new StatusEffectInstance(
                        StatusEffects.HUNGER, 600, 0
                    ), 0.7F)
                .build()
            ).group(ReadyYourShovels.ITEM_GROUP)
        )
    );

    public static final Item APRICOT = register(
        "apricot",
        new Item(
            new FabricItemSettings()
                .food(new FoodComponent.Builder()
                .hunger(3).saturationModifier(0.3F)
                .build()
            ).group(ReadyYourShovels.ITEM_GROUP)
        )
    );
    public static final Item ROTTEN_APRICOT = register(
        "rotten_apricot",
        new Item(
            new FabricItemSettings()
                .food(new FoodComponent.Builder()
                .hunger(1).saturationModifier(0.1F)
                .statusEffect(
                    new StatusEffectInstance(
                        StatusEffects.HUNGER, 600, 0
                    ), 0.7F)
                .build()
            ).group(ReadyYourShovels.ITEM_GROUP)
        )
    );

    public RYSItems() {
        FuelRegistry.INSTANCE.add(RYSItems.PEAT, 800);
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ReadyYourShovels.MOD_ID, id), item);
    }
    private static Item register(String id) {
        return register(id, new Item(new FabricItemSettings().group(ReadyYourShovels.ITEM_GROUP)));
    }
}
