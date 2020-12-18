package teamhollow.readyyourshovels.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.world.feature.structure.AntHillPieces;

public class RYSStructurePieces {
    public static final IStructurePieceType ANT_HILL_PIECE = registerStructurePiece(new ResourceLocation(ReadyYourShovels.MODID, "ant_hill"), AntHillPieces.Piece::new);

    public static <C extends IFeatureConfig> IStructurePieceType registerStructurePiece(ResourceLocation key, IStructurePieceType pieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, pieceType);
    }
}
