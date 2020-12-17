package group.rys.common.world.gen.feature.structure;

import group.rys.core.util.Reference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class AnthillPieces {

    private static final ResourceLocation anthill = new ResourceLocation(Reference.MOD_ID, "ant_hill");

    private static final IStructurePieceType ANTHILL = IStructurePieceType.register(AnthillPieces.Piece::new, Reference.MOD_ID + ".ant_hill");

    public static void addPieces(TemplateManager templateManagerIn, BlockPos blockpos, Rotation rotation, List<StructurePiece> components, Random rand) {
        components.add(new AnthillPieces.Piece(templateManagerIn, anthill, blockpos, rotation));
    }

    public static class Piece extends TemplateStructurePiece {

        private final ResourceLocation templateLocation;
        private final Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation templateLocationIn, BlockPos blockpos, Rotation rotation) {
            super(ANTHILL, 0);
            this.templateLocation = templateLocationIn;
            this.rotation = rotation;
            this.templatePosition = blockpos;
            this.loadTemplate(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT compoundnbt) {
            super(ANTHILL, 0);
            this.templateLocation = new ResourceLocation(compoundnbt.getString("Template"));
            this.rotation = Rotation.valueOf(compoundnbt.getString("Rot"));
            this.loadTemplate(templateManagerIn);
        }

        private void loadTemplate(TemplateManager templateManagerIn) {
            Template template = templateManagerIn.getTemplateDefaulted(this.templateLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(this.rotation).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.templateLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {

        }

        public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkpos) {
            BlockPos blockpos_1 = this.templatePosition;
            int i = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos_1.getX(), blockpos_1.getZ());

            this.templatePosition = this.templatePosition.add(0, i - 99, 0);

            boolean flag = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn, chunkpos);

            this.templatePosition = blockpos_1;
            return flag;
        }

    }

}
