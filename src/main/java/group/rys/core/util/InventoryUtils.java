package group.rys.core.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryUtils {
	
	public static void addStackToInventory(IInventory inventory, ItemStack stack) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack_1 = inventory.getStackInSlot(i);
			
			if (stack.isEmpty()) {
				inventory.setInventorySlotContents(i, stack_1);
				return;
			}
			
			if (stack_1.isEmpty()) {
				inventory.setInventorySlotContents(i, stack);
				return;
			}
			
			if (canAddStacks(stack, stack_1)) {
				ItemStack stack_2 = new ItemStack(stack.getItem(), stack.getCount() + stack_1.getCount());
				inventory.setInventorySlotContents(i, stack_2);
				return;
			}
		}
	}
	
	public static void addStackToInventory(World world, BlockPos pos, ItemStack stack) {
//		addStackToInventory(ChestBlock.getInventory(state, world, pos, false), stack);
		addStackToInventory((IInventory) world.getTileEntity(pos), stack);
	}
	
	public static boolean canStoreStackInInventory(IInventory inventory, ItemStack stack) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack_1 = inventory.getStackInSlot(i);
			
			if (canAddStacks(stack, stack_1)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean canStoreStackInInventory(World world, BlockPos pos, ItemStack stack) {
//		return canStoreStackInInventory(ChestBlock.getInventory(state, world, pos, false), stack);
		return canStoreStackInInventory((IInventory) world.getTileEntity(pos), stack);
	}
	
	public static boolean canAddStacks(ItemStack stack_1, ItemStack stack_2) {
		if (stack_1.isEmpty() || stack_2.isEmpty()) {
			return true;
		}
		
		if (ItemStack.areItemsEqual(stack_1, stack_2) && stack_1.getCount() + stack_2.getCount() <= stack_1.getMaxStackSize()) {
			return true;
		}
		
		return false;
	}
	
}
