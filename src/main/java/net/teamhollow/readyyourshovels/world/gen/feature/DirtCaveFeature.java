package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
                if (!Objects.equals(world.method_31081(blockPos), Optional.of(RYSBiomes.FOREST_MOUND))) return false;

                int bottomOffset = 15;
                int height = 50;
                for (int y = 0; y <= height; y++) {
                    blockPos.set(blockPos.getX(), y + bottomOffset, blockPos.getZ());
                    BlockState state = world.getBlockState(blockPos);

                    if (y <= 3 && !state.isAir()) {
                        world.setBlockState(blockPos, RYSBlocks.REGOLITH.getDefaultState() , 2);
                    } else if (state.isOf(Blocks.STONE) || (state.isOf(Blocks.ANDESITE) || state.isOf(Blocks.DIORITE) || state.isOf(Blocks.GRANITE))) {
                        world.setBlockState(blockPos, RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
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
