package net.teamhollow.readyyourshovels.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock implements Fertilizable {
    private CropBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
    private void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        for (Block block : new Block[]{ RYSBlocks.OAK_PLANTER_BOX, RYSBlocks.BIRCH_PLANTER_BOX, RYSBlocks.SPRUCE_PLANTER_BOX, RYSBlocks.DARK_OAK_PLANTER_BOX, RYSBlocks.ACACIA_PLANTER_BOX, RYSBlocks.JUNGLE_PLANTER_BOX, RYSBlocks.CRIMSON_PLANTER_BOX, RYSBlocks.WARPED_PLANTER_BOX }) {
            if (floor.isOf(block)) {
                cir.setReturnValue(true);
                break;
            }
        }
    }
}
