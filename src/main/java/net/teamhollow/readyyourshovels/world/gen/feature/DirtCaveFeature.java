package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.init.RYSBiomes;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class DirtCaveFeature extends Feature<DefaultFeatureConfig> {
    public DirtCaveFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int x = 0; x < 38; x++) {
            for (int z = 0; z < 38; z++) {
                blockPos.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
                if (!Objects.equals(world.method_31081(blockPos), Optional.of(RYSBiomes.PLAINS_MOUND))) return false;

                for (int y = 0; y <= 70; y++) {
                    blockPos.set(blockPos.getX(), y, blockPos.getZ());
                    BlockState state = world.getBlockState(blockPos);

                    if (state.isOf(Blocks.STONE)) {
                        world.setBlockState(blockPos, y <= 12 ? RYSBlocks.TOUGH_DIRT.getDefaultState() : RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
                    } else if (state.isOf(Blocks.IRON_ORE)) {
                        world.setBlockState(blockPos, RYSBlocks.IRON_DEPOSIT.getDefaultState(), 2);
                    } else if (state.isOf(Blocks.GOLD_ORE)) {
                        world.setBlockState(blockPos, RYSBlocks.GOLD_DEPOSIT.getDefaultState(), 2);
                    } else if (state.isOf(Blocks.COAL_ORE)) {
                        world.setBlockState(blockPos, RYSBlocks.PEAT_DEPOSIT.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}
