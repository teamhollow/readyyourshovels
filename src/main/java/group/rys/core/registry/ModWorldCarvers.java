package group.rys.core.registry;

import group.rys.common.world.gen.carver.ValleyCanyonRavineCarver;
import group.rys.core.util.Reference;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModWorldCarvers {
	public static final ValleyCanyonRavineCarver valley = create("valley", new ValleyCanyonRavineCarver(ProbabilityConfig::deserialize, 256));

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<WorldCarver<?>> event) {
		IForgeRegistry<WorldCarver<?>> registry = event.getRegistry();

        registry.register(valley);
	}
	
	public static <T extends WorldCarver<?>> T create(String name, T carver) {
		carver.setRegistryName(Reference.MOD_ID, name);
		return carver;
	}
	
	public static void registerWorldCarvers() {
		ForgeRegistries.BIOMES.forEach((Biome biome) -> {

			if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER))
			{
				biome.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(valley, new ProbabilityConfig(1)));
			}
		});
	}
	
}
