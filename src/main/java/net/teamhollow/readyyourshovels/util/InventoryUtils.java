package net.teamhollow.readyyourshovels.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class InventoryUtils {
    public static void addStackToInventory(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack oldStack = inventory.getStack(i);

            if (stack.isEmpty()) {
                inventory.setStack(i, oldStack);
                return;
            }

            if (oldStack.isEmpty()) {
                inventory.setStack(i, stack);
                return;
            }

            if (canAddStacks(stack, oldStack)) {
                ItemStack newStack = new ItemStack(stack.getItem(), stack.getCount() + oldStack.getCount());
                inventory.setStack(i, newStack);
                return;
            }
        }
    }

    public static void addStackToInventory(World world, BlockPos pos, ItemStack stack) {
        addStackToInventory((Inventory) Objects.requireNonNull(world.getBlockEntity(pos)), stack);
    }

    public static boolean canStoreStackInInventory(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack_1 = inventory.getStack(i);

            if (canAddStacks(stack, stack_1)) {
                return true;
            }
        }

        return false;
    }

    public static boolean canStoreStackInInventory(World world, BlockPos pos, ItemStack stack) {
        return canStoreStackInInventory((Inventory) Objects.requireNonNull(world.getBlockEntity(pos)), stack);
    }

    public static boolean canAddStacks(ItemStack oldStack, ItemStack newStack) {
        return (oldStack.isEmpty() || newStack.isEmpty()) || (ItemStack.areItemsEqual(oldStack, newStack) && oldStack.getCount() + newStack.getCount() <= oldStack.getMaxCount());
    }
}
