package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.teamhollow.readyyourshovels.block.PlanterBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
    private void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState floor = world.getBlockState(pos.down());
        if (floor.getBlock() instanceof PlanterBoxBlock && PlanterBoxBlock.isWetAndSupportive(SugarCaneBlock.class.cast(this), floor)) {
            cir.setReturnValue(true);
        }
    }
}
