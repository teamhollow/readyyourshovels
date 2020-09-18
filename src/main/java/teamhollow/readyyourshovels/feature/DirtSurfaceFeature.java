package teamhollow.readyyourshovels.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import teamhollow.readyyourshovels.registry.RYSBlocks;

import java.util.Random;

public class DirtSurfaceFeature extends Feature<NoFeatureConfig> {
    public DirtSurfaceFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean func_230362_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int k = pos.getX() + i;
                int l = pos.getZ() + j;
                int i1 = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, k, l);


                Biome biome = world.getBiome(blockpos$mutable);
                for (int d = 4; d < 28; ++d) {
                    blockpos$mutable.setPos(k, i1 - d, l);

                    BlockState state = world.getBlockState(blockpos$mutable);
                    if (state.isIn(Blocks.STONE) || state.isIn(Blocks.GRANITE) || state.isIn(Blocks.DIORITE) || state.isIn(Blocks.ANDESITE) || state.isIn(Blocks.COAL_ORE) || state.isIn(Blocks.IRON_ORE) || state.isIn(Blocks.GOLD_ORE)) {
                        if (d >= 27) {
                            world.setBlockState(blockpos$mutable, RYSBlocks.REGOLITH.getDefaultState(), 2);
                        } else {
                            world.setBlockState(blockpos$mutable, RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}
