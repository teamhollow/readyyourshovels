package group.rys.core.registry;

import group.rys.common.entity.GathererAntEntity;
import group.rys.common.entity.HuntingAntEntity;
import group.rys.common.entity.HuntingAntLarvaeEntity;
import group.rys.common.entity.QueenAntEntity;
import group.rys.core.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModEntities {
	
	public static final EntityType<GathererAntEntity> gatherer_ant = create("gatherer_ant", EntityType.Builder.create(GathererAntEntity::new, EntityClassification.MISC).size(0.3125F, 0.3125F));
	public static final EntityType<HuntingAntEntity> hunting_ant = create("hunting_ant", EntityType.Builder.create(HuntingAntEntity::new, EntityClassification.MISC).size(0.55F, 0.6F));
	public static final EntityType<QueenAntEntity> queen_ant = create("queen_ant", EntityType.Builder.create(QueenAntEntity::new, EntityClassification.MISC).size(0.6F, 0.65F));
	public static final EntityType<HuntingAntLarvaeEntity> hunting_ant_larvae = create("hunting_ant_larvae", EntityType.Builder.<HuntingAntLarvaeEntity>create(HuntingAntLarvaeEntity::new, EntityClassification.MISC).setCustomClientFactory(HuntingAntLarvaeEntity::new).size(0.5F, 0.5F));


	@SubscribeEvent
	public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		
		registry.register(gatherer_ant);
		registry.register(hunting_ant);
		registry.register(queen_ant);
		registry.register(hunting_ant_larvae);
	}
	
	public static <T extends Entity> EntityType<T> create(String name, EntityType.Builder<T> builder) {
		EntityType<T> entity = builder.build(name);
		entity.setRegistryName(Reference.MOD_ID, name);
		return entity;
	}
	
}
