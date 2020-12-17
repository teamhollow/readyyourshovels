package group.rys.core.event;

import group.rys.common.block.PlanterBoxBlock;
import group.rys.core.registry.ModItems;
import group.rys.core.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
	
	@SubscribeEvent
	public static void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
		IWorld world = event.getWorld();
		BlockPos pos = event.getPos().down();
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if (block instanceof PlanterBoxBlock) {
			event.setResult(Event.Result.DENY);
		}
	}
	
	@SubscribeEvent
	public static void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		
		if (item == ModItems.peat) {
			event.setBurnTime(800);
		}
		if (item == ModItems.peat_block) {
			event.setBurnTime(8000);
		}
	}
	
}
