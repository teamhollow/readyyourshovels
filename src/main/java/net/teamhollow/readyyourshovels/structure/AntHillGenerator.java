package net.teamhollow.readyyourshovels.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.init.RYSStructureFeatures;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

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
        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Template", this.template.toString());
            tag.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random, BlockBox boundingBox) {}
    }
}
