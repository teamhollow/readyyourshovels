package net.teamhollow.readyyourshovels.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class DirtSurfaceFeature extends Feature<DefaultFeatureConfig> {
    public DirtSurfaceFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable blockPos$Mutable = new BlockPos.Mutable();

        for (int i1 = 0; i1 < 16; ++i1) {
            for (int i2 = 0; i2 < 16; ++i2) {
                int x = pos.getX() + i1;
                int z = pos.getZ() + i2;
                int y = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z);

                for (int i3 = 4; i3 < 28; ++i3) {
                    blockPos$Mutable.set(x, y - i3, z);

                    BlockState state = world.getBlockState(blockPos$Mutable);
                    if ( state.isOf(Blocks.STONE)
                       || state.isOf(Blocks.GRANITE)
                       || state.isOf(Blocks.DIORITE)
                       || state.isOf(Blocks.ANDESITE)
                       || state.isOf(Blocks.COAL_ORE)
                       || state.isOf(Blocks.IRON_ORE)
                       || state.isOf(Blocks.GOLD_ORE)
                    ) {
                        if (i3 >= 27) {
                            world.setBlockState(blockPos$Mutable, RYSBlocks.REGOLITH.getDefaultState(), 2);
                        } else {
                            world.setBlockState(blockPos$Mutable, RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}
