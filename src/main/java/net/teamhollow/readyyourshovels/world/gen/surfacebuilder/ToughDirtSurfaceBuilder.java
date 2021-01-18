package net.teamhollow.readyyourshovels.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class ToughDirtSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
    private static final BlockState TOUGH_DIRT = RYSBlocks.TOUGH_DIRT.getDefaultState();

    public ToughDirtSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig surfaceBlocks) {
        int chunkX = x & 15;
        int chunkZ = z & 15;
        BlockState blockState3 = TOUGH_DIRT;
        SurfaceConfig surfaceConfig = biome.getGenerationSettings().getSurfaceConfig();
        BlockState blockState4 = surfaceConfig.getUnderMaterial();
        BlockState blockState5 = surfaceConfig.getTopMaterial();
        BlockState blockState6 = blockState4;
        int noiseRandInt = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        boolean noiseRandBool = Math.cos(noise / 3.0D * 3.141592653589793D) > 0.0D;
        int q = -1;
        int loop = 0;
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for(int y = height; y >= 0; --y) {
            BlockState iDefaultBlock = defaultBlock;
            pos.set(chunkX, y, chunkZ);

            BlockState newDefaultBlock = RYSBlocks.TOUGH_DIRT.getDefaultState();
            if (chunk.getBlockState(pos).isOf(iDefaultBlock.getBlock())) chunk.setBlockState(pos, newDefaultBlock, false);

            if (loop < 15) {
                BlockState state = chunk.getBlockState(pos);
                iDefaultBlock = newDefaultBlock;

                if (state.isAir()) {
                    q = -1;
                } else if (state.isOf(iDefaultBlock.getBlock())) {
                    if (q == -1) {
                        if (noiseRandInt <= 0) {
                            blockState3 = Blocks.AIR.getDefaultState();
                            blockState6 = iDefaultBlock;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                            blockState3 = TOUGH_DIRT;
                            blockState6 = blockState4;
                        }

                        if (y < seaLevel && (blockState3 == null || blockState3.isAir())) {
                            blockState3 = defaultFluid;
                        }

                        q = noiseRandInt + Math.max(0, y - seaLevel);
                        if (y >= seaLevel - 1) {
                            if (y > 86 + noiseRandInt * 2) {
                                if (noiseRandBool) {
                                    chunk.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState(), false);
                                } else {
                                    chunk.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), false);
                                }
                            } else if (y > seaLevel + 3 + noiseRandInt) {
                                BlockState blockState10;
                                if (y >= 64 && y <= 127) {
                                    if (noiseRandBool) {
                                        blockState10 = GRASS_BLOCK;
                                    } else {
                                        blockState10 = TOUGH_DIRT;
                                    }
                                } else {
                                    blockState10 = TOUGH_DIRT;
                                }

                                chunk.setBlockState(pos, blockState10, false);
                            } else {
                                chunk.setBlockState(pos, blockState5, false);
                            }
                        } else {
                            chunk.setBlockState(pos, blockState6, false);
                            if (blockState6 == TOUGH_DIRT) {
                                chunk.setBlockState(pos, TOUGH_DIRT, false);
                            }
                        }
                    } else if (q > 0) {
                        q--;
                        chunk.setBlockState(pos, TOUGH_DIRT, false);
                    }

                    loop++;
                }
            }
        }

    }
}
