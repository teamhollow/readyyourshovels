package teamhollow.readyyourshovels.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import teamhollow.readyyourshovels.tileentity.AntNestTileEntity;

public class RYSTileEntity {
    public static final TileEntityType<AntNestTileEntity> ANT_NEST = TileEntityType.Builder.create(AntNestTileEntity::new, RYSBlocks.ANT_NEST).build(null);


    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> registry) {
        registry.getRegistry().register(ANT_NEST.setRegistryName("ant_nest"));
    }
}
