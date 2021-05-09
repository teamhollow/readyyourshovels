package net.teamhollow.readyyourshovels.structure;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.init.RYSStructureFeatures;

import java.util.Map;
import java.util.Random;

public class AntHillGenerator {
    private static final Identifier IDENTIFIER = new Identifier(ReadyYourShovels.MOD_ID, "ant_hill");
    private static final Map<Identifier, BlockPos> RELATIVE_POSITIONS = ImmutableMap.of(IDENTIFIER, BlockPos.ORIGIN);

    public AntHillGenerator() {}

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, StructurePiecesHolder arg) {
        arg.addPiece(new AntHillGenerator.Piece(manager, IDENTIFIER, pos, rotation, 0));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation, int yOffset) {
            super(RYSStructureFeatures.ANT_HILL_PIECE, 0, manager, identifier, identifier.toString(), getPlacementData(rotation, identifier), getPos(identifier, pos, yOffset));
        }

        public Piece(ServerWorld world, NbtCompound tag) {
            super(RYSStructureFeatures.ANT_HILL_PIECE, tag, world, (identifier) -> getPlacementData(BlockRotation.valueOf(tag.getString("Rot")), identifier));
        }

        private static StructurePlacementData getPlacementData(BlockRotation blockRotation, Identifier identifier) {
            return new StructurePlacementData()
                .setRotation(blockRotation)
                .setMirror(BlockMirror.NONE)
                .setPosition(AntHillGenerator.RELATIVE_POSITIONS.get(identifier))
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        private static BlockPos getPos(Identifier identifier, BlockPos blockPos, int i) {
            return blockPos.add(AntHillGenerator.RELATIVE_POSITIONS.get(identifier)).down(i);
        }

        @Override
        public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
            Identifier identifier = new Identifier(this.field_31664);
            StructurePlacementData placementData = getPlacementData(this.placementData.getRotation(), identifier);
            BlockPos structurePos = AntHillGenerator.RELATIVE_POSITIONS.get(identifier);
            BlockPos transformedPos = this.pos.add(Structure.transform(placementData, new BlockPos(3 - structurePos.getX(), 0, -structurePos.getZ())));

            int y = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, transformedPos.getX(), transformedPos.getZ());
            BlockPos originalPos = this.pos;
            this.pos = this.pos.add(0, y - 90 - 1, 0);

            boolean generated = super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, pos);

            if (identifier.equals(AntHillGenerator.IDENTIFIER)) {
                BlockPos blockPos4 = this.pos.add(Structure.transform(placementData, new BlockPos(3, 0, 5)));
                BlockState blockState = world.getBlockState(blockPos4.down());
                if (!blockState.isAir() && !blockState.isOf(Blocks.LADDER)) {
                    world.setBlockState(blockPos4, Blocks.SNOW_BLOCK.getDefaultState(), 3);
                }
            }

            this.pos = originalPos;
            return generated;
        }

        @Override
        protected void writeNbt(ServerWorld world, NbtCompound nbt) {
            super.writeNbt(world, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if (metadata.startsWith("Nest")) {
                BlockRotation blockRotation = this.placementData.getRotation();
                BlockState blockState = RYSBlocks.ANT_NEST.getDefaultState();
                switch (metadata) {
                    case "NestWest":
                        blockState = blockState.with(AntNestBlock.FACING, blockRotation.rotate(Direction.WEST));
                        break;
                    case "NestEast":
                        blockState = blockState.with(AntNestBlock.FACING, blockRotation.rotate(Direction.EAST));
                        break;
                    case "NestSouth":
                        blockState = blockState.with(AntNestBlock.FACING, blockRotation.rotate(Direction.SOUTH));
                        break;
                    case "NestNorth":
                        blockState = blockState.with(AntNestBlock.FACING, blockRotation.rotate(Direction.NORTH));
                        break;
                }

                this.addNest(world, boundingBox, random, pos, blockState);
            }
        }

        protected void addNest(ServerWorldAccess world, BlockBox boundingBox, Random random, BlockPos pos, BlockState block) {
            if (boundingBox.contains(pos) && !world.getBlockState(pos).isOf(Blocks.CHEST)) {
                if (block == null) {
                    block = orientateChest(world, pos, RYSBlocks.ANT_NEST.getDefaultState());
                }

                world.setBlockState(pos, block, 2);
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof AntNestBlockEntity) {
                    AntNestBlockEntity antNestBlockEntity = (AntNestBlockEntity)blockEntity;
                    int j = 2 + random.nextInt(2);

                    for(int k = 0; k < j; ++k) {
                        GardenAntEntity entity = new GardenAntEntity(RYSEntities.GARDEN_ANT, world.toServerWorld());
                        antNestBlockEntity.tryEnterNest(entity, random.nextInt(599));
                    }
                }
            }
        }
    }
}
