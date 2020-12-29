package net.teamhollow.readyyourshovels.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

public class ToughrootBlock extends PlantBlock {
    public ToughrootBlock(Settings settings) {
        super(settings);
    }

    protected boolean canPlantBelow(BlockState ceiling) {
        return ceiling.isIn(RYSBlockTags.DIRT_LIKE);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return this.canPlantBelow(world.getBlockState(blockPos));
    }
}
