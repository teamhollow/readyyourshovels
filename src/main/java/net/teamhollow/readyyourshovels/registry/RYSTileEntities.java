package net.teamhollow.readyyourshovels.registry;

import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.tileentity.AntNestTileEntity;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ReadyYourShovels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RYSTileEntities {
    public static final TileEntityType<AntNestTileEntity> ANT_NEST = TileEntityType.Builder.create(AntNestTileEntity::new, RYSBlocks.ANT_NEST).build(Util.attemptDataFix(TypeReferences.BLOCK_ENTITY, "ant_nest"));

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> registry) {
        registry.getRegistry().register(ANT_NEST.setRegistryName("ant_nest"));
    }
}
