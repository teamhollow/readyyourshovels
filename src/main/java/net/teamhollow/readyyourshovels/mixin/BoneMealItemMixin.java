package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.block.FruitTreeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
    @Inject(method = "useOnFertilizable", at = @At("HEAD"), cancellable = true)
    private static void useOnFertilizable(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof FruitTreeBlock && (state.get(FruitTreeBlock.HALF) == DoubleBlockHalf.LOWER && !(state.get(FruitTreeBlock.AGE) <= 1)))
            cir.setReturnValue(false);
    }
}
