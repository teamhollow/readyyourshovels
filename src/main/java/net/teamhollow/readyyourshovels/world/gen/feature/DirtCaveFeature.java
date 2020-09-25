package net.teamhollow.readyyourshovels.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;

public class DirtCaveFeature extends Feature<DefaultFeatureConfig> {
    public DirtCaveFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                int posY = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ);

                for (int y = 4; y < 28; y++) {
                    blockPos.set(posX, posY - y, posZ);

                    BlockState state = world.getBlockState(blockPos);
                    if (  state.isOf(Blocks.STONE)
                       || state.isOf(Blocks.GRANITE)
                       || state.isOf(Blocks.DIORITE)
                       || state.isOf(Blocks.ANDESITE)
                       || state.isOf(Blocks.COAL_ORE)
                       || state.isOf(Blocks.IRON_ORE)
                       || state.isOf(Blocks.GOLD_ORE)
                    ) {
                        if (y >= 27) {
                            world.setBlockState(blockPos, RYSBlocks.REGOLITH.getDefaultState(), 2);
                        } else {
                            if (random.nextFloat() < 0.0075F && neigboringAreFree(blockPos.up(), world)) {
                                world.setBlockState(blockPos.up(), RYSBlocks.ANT_NEST.getDefaultState().with(AntNestBlock.FACING, AntNestBlock.getRandomGenerationDirection(random)), 2);
                                BlockEntity blockEntity = world.getBlockEntity(blockPos.up());
                                if (blockEntity instanceof AntNestBlockEntity) {
                                    AntNestBlockEntity antNestBlockEntity = (AntNestBlockEntity) blockEntity;
                                    int rand = 2 + random.nextInt(2);

                                    for (int i = 0; i < rand; ++i) {
                                        GardenAntEntity gardenAntEntity = new GardenAntEntity(RYSEntities.GARDEN_ANT, world.getWorld());
                                        antNestBlockEntity.tryEnterNest(gardenAntEntity, false, random.nextInt(599));
                                    }
                                }
                            }

                            world.setBlockState(blockPos, RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean neigboringAreFree(BlockPos pos, ServerWorldAccess world) {
        for (BlockPos i : new BlockPos[]{ pos, pos.up(), pos.north(), pos.east(), pos.south(), pos.west() }) {
            if (!(world.getBlockState(i).getBlock() instanceof AirBlock)) return false;
        }

        return true;
    }
}
