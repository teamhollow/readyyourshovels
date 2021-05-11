package net.teamhollow.readyyourshovels.world.gen.surfacebuilder;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

import java.util.Random;

public class ToughDirtSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
    private static final BlockState TOUGH_DIRT = RYSBlocks.TOUGH_DIRT.getDefaultState();

    public ToughDirtSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int chunkX, int chunkZ, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, TernarySurfaceConfig surfaceConfig) {
        int x = chunkX & 15;
        int z = chunkZ & 15;
        BlockState toughDirt = TOUGH_DIRT;
        BlockState underMaterial = surfaceConfig.getUnderMaterial();
        BlockState topMaterial = surfaceConfig.getTopMaterial();
        BlockState setState = underMaterial;
        int noiseRandInt = (int)(noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        boolean noiseRandBool = Math.cos(noise / 3.0D * Math.PI) > 0.0D;
        int setY = -1;
        int loop = 0;
        BlockPos.Mutable pos = new BlockPos.Mutable();

        int bottomY = chunk.getBottomY();
        for (int y = height; y >= bottomY; --y) {
            BlockState iDefaultBlock = defaultBlock;
            pos.set(x, y, z);

            BlockState newDefaultBlock = y <= 12 && random.nextDouble() <= 0.76
                ? RYSBlocks.REGOLITH.getDefaultState()
                : y <= 16 && random.nextDouble() <= 0.4
                    ? RYSBlocks.REGOLITH.getDefaultState()
                    : RYSBlocks.TOUGH_DIRT.getDefaultState();
            if (chunk.getBlockState(pos).isOf(iDefaultBlock.getBlock())) {
                chunk.setBlockState(pos, newDefaultBlock, false);
            }

            if (loop < 15) {
                BlockState state = chunk.getBlockState(pos);
                iDefaultBlock = newDefaultBlock;

                if (state.isAir()) {
                    setY = -1;
                } else if (state.isOf(iDefaultBlock.getBlock())) {
                    if (setY == -1) {
                        if (noiseRandInt <= 0) {
                            toughDirt = Blocks.AIR.getDefaultState();
                            setState = iDefaultBlock;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                            toughDirt = TOUGH_DIRT;
                            setState = underMaterial;
                        }

                        if (y < seaLevel && (toughDirt == null || toughDirt.isAir())) {
                            toughDirt = defaultFluid;
                        }

                        setY = noiseRandInt + Math.max(0, y - seaLevel);
                        if (y >= seaLevel - 1) {
                            if (y > 86 + noiseRandInt * 2) {
                                if (noiseRandBool) {
                                    chunk.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState(), false);
                                } else {
                                    chunk.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), false);
                                }
                            } else if (y > seaLevel + 3 + noiseRandInt) {
                                BlockState layerState;
                                if (y >= 64 && y <= 127) {
                                    if (noiseRandBool) {
                                        layerState = GRASS_BLOCK;
                                    } else {
                                        layerState = TOUGH_DIRT;
                                    }
                                } else {
                                    layerState = TOUGH_DIRT;
                                }

                                chunk.setBlockState(pos, layerState, false);
                            } else {
                                chunk.setBlockState(pos, topMaterial, false);
                            }
                        } else {
                            chunk.setBlockState(pos, setState, false);
                        }
                    } else if (setY > 0) {
                        setY--;
                        chunk.setBlockState(pos, TOUGH_DIRT, false);
                    }

                    loop++;
                }
            }
        }
    }
}
