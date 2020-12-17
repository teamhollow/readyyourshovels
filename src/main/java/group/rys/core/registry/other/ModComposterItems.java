package group.rys.core.registry.other;

import group.rys.core.registry.ModItems;
import net.minecraft.block.ComposterBlock;

public class ModComposterItems {
	
	public static void registerItems() {
		ComposterBlock.registerCompostable(0.5F, ModItems.dayroot);
		ComposterBlock.registerCompostable(0.65F, ModItems.orange);
		ComposterBlock.registerCompostable(0.65F, ModItems.apricot);
		ComposterBlock.registerCompostable(0.65F, ModItems.rotten_orange);
		ComposterBlock.registerCompostable(0.65F, ModItems.rotten_apricot);
	}
	
}
