package net.teamhollow.readyyourshovels.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.tag.RYSBlockTags;

public class DirtCaveFeature extends Feature<DefaultFeatureConfig> {
    public DirtCaveFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                int posY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ);

                int height = 32; // height of cave
                for (int y = 0; y < height; y++) {
                    int offset = 7; // downwards y-offset of cave
                    blockPos.set(posX, posY - (y + offset), posZ);
                    BlockState state = world.getBlockState(blockPos);
                    BlockState newState = state;

                    if (newState.isIn(RYSBlockTags.TOUGH_DIRT_REPLACEABLE)) {
                        Block block = RYSBlocks.TOUGH_DIRT;

                        if (y >= height - 3) block = RYSBlocks.REGOLITH;
                        else if (newState.isIn(RYSBlockTags.ORE_TOUGH_DIRT_REPLACEABLE)) { // replace ores with deposits
                            switch (Registry.BLOCK.getId(newState.getBlock()).toString()) {
                                case "minecraft:coal_ore":
                                    block = RYSBlocks.PEAT_DEPOSIT;
                                    break;
                                case "minecraft:iron_ore":
                                    block = RYSBlocks.IRON_DEPOSIT;
                                    break;
                                case "minecraft:gold_ore":
                                    block = RYSBlocks.GOLD_DEPOSIT;
                                    break;
                            }
                        }

                        newState = block.getDefaultState();
                    }

                    if (state != newState) world.setBlockState(blockPos, newState, 2);
                }
            }
        }

        return true;
    }
}
