package net.teamhollow.readyyourshovels.sound;

import net.minecraft.sound.BlockSoundGroup;
import net.teamhollow.readyyourshovels.init.RYSSoundEvents;

public class RYSBlockSoundGroup {
    public static final BlockSoundGroup TOUGH_DIRT = new BlockSoundGroup(1.0F, 1.0F, RYSSoundEvents.BLOCK_TOUGH_DIRT_BREAK, RYSSoundEvents.BLOCK_TOUGH_DIRT_STEP, RYSSoundEvents.BLOCK_TOUGH_DIRT_PLACE, RYSSoundEvents.BLOCK_TOUGH_DIRT_HIT, RYSSoundEvents.BLOCK_TOUGH_DIRT_FALL);
    public static final BlockSoundGroup REGOLITH = new BlockSoundGroup(1.0F, 1.0F, RYSSoundEvents.BLOCK_REGOLITH_BREAK, RYSSoundEvents.BLOCK_REGOLITH_STEP, RYSSoundEvents.BLOCK_REGOLITH_PLACE, RYSSoundEvents.BLOCK_REGOLITH_HIT, RYSSoundEvents.BLOCK_REGOLITH_FALL);
}
