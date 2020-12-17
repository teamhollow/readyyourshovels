package group.rys.core.registry;

import group.rys.common.world.gen.placement.CountChanceDepth;
import group.rys.common.world.gen.placement.CountChanceDepthConfig;
import group.rys.core.util.Reference;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModPlacements {
	
	public static final CountChanceDepth count_chance_depth = create("count_chance_depth", new CountChanceDepth(CountChanceDepthConfig::deserialize));
	
	@SubscribeEvent
	public static void registerPlacements(RegistryEvent.Register<Placement<?>> event) {
		IForgeRegistry<Placement<?>> registry = event.getRegistry();
		
		registry.register(count_chance_depth);
	}
	
	public static <T extends Placement<?>> T create(String name, T placement) {
		placement.setRegistryName(Reference.MOD_ID, name);
		return placement;
	}
	
}
