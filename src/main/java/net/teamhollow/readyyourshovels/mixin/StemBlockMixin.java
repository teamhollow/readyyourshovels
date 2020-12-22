package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.teamhollow.readyyourshovels.registry.RYSBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StemBlock.class)
public abstract class StemBlockMixin extends BushBlock implements IGrowable {
    private StemBlockMixin(AbstractBlock.Properties settings) {
        super(settings);
    }

    @Inject(method = "isValidGround", at = @At("RETURN"), cancellable = true)
    private void isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (RYSBlocks.blockIsOfPlanterBox(state))
            cir.setReturnValue(true);
    }
}
