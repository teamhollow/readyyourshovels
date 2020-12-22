package net.teamhollow.readyyourshovels.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PeatItem extends Item {
    public PeatItem(Properties group) {
        super(group);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return 200;
    }
}
