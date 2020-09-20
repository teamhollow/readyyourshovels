package net.teamhollow.readyyourshovels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

@Mixin(AttachedStemBlock.class)
public abstract class AttachedStemBlockMixin extends PlantBlock {
    private AttachedStemBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (RYSBlocks.blockIsOfPlanterBox(floor))
            cir.setReturnValue(true);
    }
}
