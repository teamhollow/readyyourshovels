package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.ReadyYourShovelsClient;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.entity.peaty_slime.PeatySlimeEntity;
import net.teamhollow.readyyourshovels.item.RYSItemGroup;

public class RYSEntities {
    public static final EntityType<GardenAntEntity> GARDEN_ANT = register(
        GardenAntEntity.id,
        FabricEntityTypeBuilder.createMob()
            .entityFactory(GardenAntEntity::new)
            .spawnGroup(SpawnGroup.CREATURE)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
           .trackRangeBlocks(8),
        new int[]{ 0x4C2A24, 0x558938 }
    );

    public static final EntityType<PeatySlimeEntity> PEATY_SLIME = register(
        PeatySlimeEntity.id,
        FabricEntityTypeBuilder.createMob()
            .entityFactory(PeatySlimeEntity::new)
            .spawnGroup(SpawnGroup.MONSTER)
            .dimensions(EntityDimensions.changing(2.04F, 2.04F))
            .spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PeatySlimeEntity::canMobSpawn)
            .trackRangeBlocks(10),
        new int[]{ 0x703100, 0xF5AB41 }
    );

    static {
        registerDefaultAttributes(GARDEN_ANT, GardenAntEntity.createAntAttributes());
        registerDefaultAttributes(PEATY_SLIME, PeatySlimeEntity.createPeatySlimeAttributes());
    }

    private static <T extends MobEntity> EntityType<T> register(String id, EntityType<T> entityType, int[] spawnEggColors) {
        if (spawnEggColors != null)
            Registry.register(Registry.ITEM, new Identifier(ReadyYourShovels.MOD_ID, id + "_spawn_egg"), new SpawnEggItem(entityType, spawnEggColors[0], spawnEggColors[1], new Item.Settings().maxCount(64).group(RYSItemGroup.INSTANCE)));

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ReadyYourShovels.MOD_ID, id), entityType);
    }
    private static <T extends MobEntity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> entityType, int[] spawnEggColors) {
        return register(id, entityType.build(), spawnEggColors);
    }

    public static void registerDefaultAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer.Builder builder) {
        FabricDefaultAttributeRegistry.register(type, builder);
    }

    public static Identifier texture(String path) {
        return ReadyYourShovelsClient.texture("entity/" + path);
    }
}
