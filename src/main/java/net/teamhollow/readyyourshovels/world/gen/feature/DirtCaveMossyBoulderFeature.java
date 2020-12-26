package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ForestRockFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class DirtCaveMossyBoulderFeature extends ForestRockFeature {
    public DirtCaveMossyBoulderFeature(Codec<SingleStateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, SingleStateFeatureConfig singleStateFeatureConfig) {
        return blockPos.getY() < 58 && super.generate(structureWorldAccess, chunkGenerator, random, blockPos, singleStateFeatureConfig);
    }
}
