package net.teamhollow.readyyourshovels.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.carver.RavineCarver;
import net.minecraft.world.gen.carver.RavineCarverConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class ToughDirtCanyonCarver extends RavineCarver {
    public static final String id = "tough_dirt_canyon";

    public ToughDirtCanyonCarver(Codec<RavineCarverConfig> configCodec) {
        super(configCodec);
        this.alwaysCarvableBlocks = ImmutableSet.of(RYSBlocks.TOUGH_DIRT, RYSBlocks.REGOLITH, Blocks.GRASS_BLOCK, Blocks.DIRT);
    }
}
