package net.teamhollow.readyyourshovels.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.teamhollow.readyyourshovels.world.feature.structure.AntHillPieces;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSStructurePieces {
    public static final IStructurePieceType ANT_HILL_PIECE = registerStructurePiece(new ResourceLocation(ReadyYourShovels.MOD_ID, "ant_hill"), AntHillPieces.Piece::new);

    public static <C extends IFeatureConfig> IStructurePieceType registerStructurePiece(ResourceLocation key, IStructurePieceType pieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, pieceType);
    }
}
