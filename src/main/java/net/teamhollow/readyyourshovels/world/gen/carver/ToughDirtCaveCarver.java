package net.teamhollow.readyyourshovels.world.gen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.CaveCarver;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class ToughDirtCaveCarver extends CaveCarver {
    public static final String id = "tough_dirt_cave";

    public ToughDirtCaveCarver(Codec<ProbabilityConfig> configCodec, int heightLimit) {
        super(configCodec, heightLimit);
        this.alwaysCarvableBlocks = ImmutableSet.of(RYSBlocks.TOUGH_DIRT);
    }
}
