package teamhollow.readyyourshovels.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import teamhollow.readyyourshovels.world.feature.structure.AntHillPieces;

public class AntHillStructure extends Structure<NoFeatureConfig> {
    public AntHillStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    /*public String getStructureName() {
        return ReadyYourShovels.MODID + ":hunterhouse";
    }*/

    public Structure.IStartFactory getStartFactory() {
        return AntHillStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }


    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox p_i225806_4_, int p_i225806_5_, long p_i225806_6_) {
            super(structure, chunkX, chunkZ, p_i225806_4_, p_i225806_5_, p_i225806_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biome, NoFeatureConfig p_230364_7_) {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);

            BlockPos blockpos = new BlockPos(x, y, z);

            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            AntHillPieces.addStructure(templateManagerIn, blockpos, rotation, this.components);
            this.recalculateStructureSize();
        }
    }
}
