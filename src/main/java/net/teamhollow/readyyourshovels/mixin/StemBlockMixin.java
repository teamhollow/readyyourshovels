package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.block.PlanterBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StemBlock.class)
public class StemBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (floor.getBlock() instanceof PlanterBoxBlock && PlanterBoxBlock.isWetAndSupportive(StemBlock.class.cast(this), floor)) {
            cir.setReturnValue(true);
        }
    }
}
