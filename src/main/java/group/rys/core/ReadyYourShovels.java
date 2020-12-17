package group.rys.core;

import group.rys.common.world.gen.storage.loot.conditions.MatchHarvestLevel;
import group.rys.common.world.gen.storage.loot.conditions.MatchHarvestTool;
import group.rys.common.world.gen.storage.loot.functions.ApplyFortune;
import group.rys.core.event.ModEvents;
import group.rys.core.registry.ModFeatures;
import group.rys.core.registry.ModPotions;
import group.rys.core.registry.other.ModComposterItems;
import group.rys.core.registry.other.ModRenderers;
import group.rys.core.util.Reference;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class ReadyYourShovels {
	
	public static ReadyYourShovels instance;
	
	public ReadyYourShovels() {
		instance = this;
		
		MinecraftForge.EVENT_BUS.register(ModEvents.class);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
	}
	
	public void commonSetup(FMLCommonSetupEvent event) {
		ModFeatures.registerFeatures();
		ModComposterItems.registerItems();
		ModPotions.registerRecipes();
		
		LootConditionManager.registerCondition(new MatchHarvestLevel.Serializer());
		LootConditionManager.registerCondition(new MatchHarvestTool.Serializer());
		LootFunctionManager.registerFunction(new ApplyFortune.Serializer());
	}
	
	public void clientSetup(FMLClientSetupEvent event) {
		ModRenderers.register();
	}
	
}
