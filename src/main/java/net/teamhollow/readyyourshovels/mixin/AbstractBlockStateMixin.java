package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.teamhollow.readyyourshovels.block.PlanterBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "getModelOffset", at = @At("RETURN"), cancellable = true)
    public void modifyModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> info) {
        if (AbstractBlock.AbstractBlockState.class.cast(this).isOf(Blocks.SUGAR_CANE)) {
            BlockState floor = world.getBlockState(pos.down());

            if (floor.getBlock() instanceof PlanterBoxBlock) {
                info.setReturnValue(info.getReturnValue().subtract(0, 1/16D, 0));
            } else if (floor.getBlock() == Blocks.SUGAR_CANE) {
                info.setReturnValue(floor.getModelOffset(world, pos.down()));
            }
        }
    }
}
