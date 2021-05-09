package net.teamhollow.readyyourshovels.init;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.AntNestBlock;

public class RYSPointOfInterests {
    public static final PointOfInterestType ANT_NEST = register(AntNestBlock.id, getAllStatesOf(RYSBlocks.ANT_NEST), 0, 1);

    private static Set<BlockState> getAllStatesOf(Block block) {
        return ImmutableSet.copyOf(block.getStateManager().getStates());
    }

    private static PointOfInterestType register(String id, Set<BlockState> workStationStates, int ticketCount, int searchDistance) {
        return PointOfInterestHelper.register(new Identifier(ReadyYourShovels.MOD_ID, id), ticketCount, searchDistance, workStationStates);
    }
}
