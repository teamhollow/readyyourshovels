package net.teamhollow.readyyourshovels.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.teamhollow.readyyourshovels.registry.RYSBlocks;
import net.teamhollow.readyyourshovels.registry.RYSEntities;
import net.teamhollow.readyyourshovels.tileentity.AntNestTileEntity;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.entity.GardenAntEntity;

import java.util.Random;

public class DirtSurfaceFeature extends Feature<NoFeatureConfig> {
    public DirtSurfaceFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                int posY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ);

                for (int d = 0; d < 28; ++d) {
                    blockpos$mutable.setPos(posX, posY - d, posZ);

                    BlockState state = world.getBlockState(blockpos$mutable);
                    if (state.isIn(Blocks.STONE) || state.isIn(Blocks.GRANITE) || state.isIn(Blocks.DIORITE) || state.isIn(Blocks.ANDESITE) || state.isIn(Blocks.COAL_ORE) || state.isIn(Blocks.IRON_ORE) || state.isIn(Blocks.GOLD_ORE)) {
                        if (d >= 27) {
                            world.setBlockState(blockpos$mutable, RYSBlocks.REGOLITH.getDefaultState(), 2);
                        } else {
                            if (random.nextFloat() < 0.0075F && neigboringAreFree(blockpos$mutable.up(), world)) {
                                world.setBlockState(blockpos$mutable.up(), RYSBlocks.ANT_NEST.getDefaultState().with(AntNestBlock.FACING, AntNestBlock.getGenerationDirection(random)), 2);
                                TileEntity tileEntity = world.getTileEntity(blockpos$mutable.up());
                                if (tileEntity instanceof TileEntity) {
                                    AntNestTileEntity antNestBlockEntity = (AntNestTileEntity) tileEntity;
                                    int rand = 2 + random.nextInt(2);

                                    for (int i = 0; i < rand; ++i) {
                                        GardenAntEntity gardenAntEntity = new GardenAntEntity(RYSEntities.GARDEN_ANT, world.getWorld());
                                        antNestBlockEntity.tryEnterNest(gardenAntEntity, false, random.nextInt(599));
                                    }
                                }
                            }

                            world.setBlockState(blockpos$mutable, RYSBlocks.TOUGH_DIRT.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean neigboringAreFree(BlockPos pos, ISeedReader world) {
        for (BlockPos i : new BlockPos[]{pos, pos.up(), pos.north(), pos.east(), pos.south(), pos.west()}) {
            if (!(world.getBlockState(i).getBlock() instanceof AirBlock)) return false;
        }

        return true;
    }

}
