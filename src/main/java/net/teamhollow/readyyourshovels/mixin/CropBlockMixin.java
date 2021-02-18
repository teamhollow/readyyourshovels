package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.block.PlanterBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (floor.getBlock() instanceof PlanterBoxBlock && PlanterBoxBlock.isWetAndSupportive(CropBlock.class.cast(this), floor)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getAvailableMoisture", at = @At("HEAD"), cancellable = true)
    private static void getAvailableMoisture(Block block, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        BlockState floor = world.getBlockState(pos.down());
        if (floor.getBlock() instanceof PlanterBoxBlock && PlanterBoxBlock.isWetAndSupportive(block, floor)) {
            cir.setReturnValue(3.0F);
        }
    }
}
