package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntity;

public class RYSEntities {
    public static final EntityType<GardenAntEntity> GARDEN_ANT = register(
        GardenAntEntity.id,
        GardenAntEntity.builder,
        GardenAntEntity.spawnEggColors
    );

    public RYSEntities() {
        registerDefaultAttributes(GARDEN_ANT, GardenAntEntity.createGardenAntAttributes());
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> entityType,
            int[] spawnEggColors) {
        EntityType<T> builtEntityType = entityType.build(id);

        if (spawnEggColors != null)
            RYSItems.register(id + "_spawn_egg", new SpawnEggItem(builtEntityType, spawnEggColors[0], spawnEggColors[1],
                    new Item.Settings().maxCount(64).group(ReadyYourShovels.ITEM_GROUP)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ReadyYourShovels.MOD_ID, id), builtEntityType);
    }

    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

    public static Identifier texture(String path) {
        return ReadyYourShovels.texture("entity/" + path);
    }
}
