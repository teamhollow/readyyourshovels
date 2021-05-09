package net.teamhollow.readyyourshovels.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class ToughDirtCaveCarver extends CaveCarver {
    public static final String id = "tough_dirt_cave";

    public ToughDirtCaveCarver(Codec<CaveCarverConfig> configCodec) {
        super(configCodec);
        this.alwaysCarvableBlocks = ImmutableSet.of(RYSBlocks.TOUGH_DIRT, RYSBlocks.REGOLITH, Blocks.GRASS_BLOCK, Blocks.DIRT);
    }
}
