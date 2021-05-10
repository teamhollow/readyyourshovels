package net.teamhollow.readyyourshovels.block;

import me.andante.chord.block.CeilingPlantBlock;
import net.minecraft.block.BlockState;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

public class ToughrootBlock extends CeilingPlantBlock {
    public ToughrootBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantBelow(BlockState ceiling) {
        return ceiling.isIn(RYSBlockTags.DIRT_LIKE);
    }
}
