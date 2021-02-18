package net.teamhollow.readyyourshovels.structure;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSEntities;
import net.teamhollow.readyyourshovels.init.RYSStructureFeatures;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

import java.util.List;
import java.util.Random;

public class AntHillGenerator {
    private static final Identifier ANT_HILL = new Identifier(ReadyYourShovels.MOD_ID, AntHillFeature.id);

    public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
        pieces.add(new AntHillPiece(manager, pos, ANT_HILL, rotation));
    }

    public static class AntHillPiece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier template;

        public AntHillPiece(StructureManager structureManager, CompoundTag compoundTag) {
            super(RYSStructureFeatures.ANT_HILL_PIECE, compoundTag);
            this.template = new Identifier(compoundTag.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));
            this.initializeStructureData(structureManager);
        }

        public AntHillPiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
            super(RYSStructureFeatures.ANT_HILL_PIECE, 0);
            this.pos = pos;
            this.rotation = rotation;
            this.template = template;

            this.initializeStructureData(structureManager);
        }

        private void initializeStructureData(StructureManager structureManager) {
            Structure structure = structureManager.getStructureOrBlank(this.template);
            StructurePlacementData placementData = new StructurePlacementData()
                .setRotation(this.rotation)
                .setMirror(BlockMirror.NONE)
                .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, placementData);
        }

        @Override
        protected void writeNbt(CompoundTag tag) {
            super.writeNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rotation.name());
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
