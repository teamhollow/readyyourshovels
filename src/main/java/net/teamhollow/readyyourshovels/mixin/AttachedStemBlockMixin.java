package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.teamhollow.readyyourshovels.registry.RYSBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttachedStemBlock.class)
public class AttachedStemBlockMixin extends BushBlock {
    private AttachedStemBlockMixin(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Inject(method = "isValidGround", at = @At("RETURN"), cancellable = true)
    private void isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (RYSBlocks.blockIsOfPlanterBox(state))
            cir.setReturnValue(true);
    }
}
