package net.teamhollow.readyyourshovels.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class CaveCarrotSoupItem extends Item {
    public CaveCarrotSoupItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
        ItemStack superStack = super.finishUsing(stack, world, entity);
        return entity instanceof PlayerEntity && ((PlayerEntity) entity).getAbilities().creativeMode ? superStack : new ItemStack(Items.BOWL);
    }
}
