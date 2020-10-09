package teamhollow.readyyourshovels.registry;


import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;

import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovels.MODID)
public class RYSPointOfInterests {

    public static final PointOfInterestType ANT_NEST = new PointOfInterestType("unemployed", getAllStates(RYSBlocks.ANT_NEST), 1, 1);

    public static Set<BlockState> getAllStates(Block blockIn) {
        return ImmutableSet.copyOf(blockIn.getStateContainer().getValidStates());
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<PointOfInterestType> registry) {
        PointOfInterestType.registerBlockStates(ANT_NEST);
        registry.getRegistry().register(ANT_NEST.setRegistryName("ant_nest"));
    }
}
