package net.teamhollow.readyyourshovels.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.KilnBlock;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.screen.KilnScreenHandler;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(RYSBlockEntities.KILN, pos, state, RecipeType.SMOKING);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container." + ReadyYourShovels.MOD_ID + ".kiln");
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        return fuel.isIn(RYSItemTags.PEAT) ? super.getFuelTime(fuel) / 2 : super.getFuelTime(fuel);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
