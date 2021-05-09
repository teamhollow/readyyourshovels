package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.structure.AntHillGenerator;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

public class RYSStructureFeatures {
    public static final StructurePieceType ANT_HILL_PIECE = register(AntHillFeature.id, AntHillGenerator.Piece::new);
    public static final StructureFeature<DefaultFeatureConfig> ANT_HILL = register(
        FabricStructureBuilder.create(
            new Identifier(ReadyYourShovels.MOD_ID, AntHillFeature.id),
            new AntHillFeature(DefaultFeatureConfig.CODEC)
        )
        .step(GenerationStep.Feature.SURFACE_STRUCTURES)
        .defaultConfig(32, 8, 238429572)
        .adjustsSurface()
    );

    private static StructurePieceType register(String id, StructurePieceType structurePieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(ReadyYourShovels.MOD_ID, id), structurePieceType);
    }
    private static StructureFeature<DefaultFeatureConfig> register(FabricStructureBuilder<DefaultFeatureConfig, ?> structureBuilder) {
        return structureBuilder.register();
    }
}
