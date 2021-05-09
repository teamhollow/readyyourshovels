package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.teamhollow.readyyourshovels.structure.AntHillGenerator;

public class AntHillFeature extends StructureFeature<DefaultFeatureConfig> {
    public static final String id = "ant_hill";

    public AntHillFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return AntHillFeature.Start::new;
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> feature, ChunkPos chunkPos, int references, long seed) {
            super(feature, chunkPos, references, seed);
        }

        @Override
        public void init(DynamicRegistryManager registryManager, ChunkGenerator generator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig config, HeightLimitView world) {
            int x = chunkPos.getStartX() * 16;
            int z = chunkPos.getStartZ() * 16;
            BlockPos blockPos = new BlockPos(x, generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world), z);
            BlockRotation blockRotation = BlockRotation.random(this.random);
            AntHillGenerator.addPieces(structureManager, blockPos, blockRotation, this);
            this.setBoundingBoxFromChildren();
        }
    }
}
