package group.rys.core.registry;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import group.rys.common.tileentity.AnthillTileEntity;
import group.rys.common.tileentity.HuntingAnthillTileEntity;
import group.rys.core.util.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModTileEntities {
	
	public static final TileEntityType<AnthillTileEntity> anthill = create("anthill", TileEntityType.Builder.create(AnthillTileEntity::new, ModBlocks.anthill));
	public static final TileEntityType<HuntingAnthillTileEntity> hunting_anthill = create("hunting_anthill", TileEntityType.Builder.create(HuntingAnthillTileEntity::new, ModBlocks.hunting_anthill));


	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		
		registry.register(anthill);
		registry.register(hunting_anthill);
	}
	
	public static <T extends TileEntity> TileEntityType<T> create(String name, TileEntityType.Builder<T> builder) {
		Type<?> type = null;
		
		try {
			type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).getChoiceType(TypeReferences.BLOCK_ENTITY, name);
		} catch (IllegalArgumentException illegalstateexception) {
			if (SharedConstants.developmentMode) {
				throw illegalstateexception;
			}
			
			Reference.LOGGER.warn("No data fixer registered for block entity {}", (Object) name);
		}
		
		TileEntityType<T> tileEntity = builder.build(type);
		tileEntity.setRegistryName(Reference.MOD_ID, name);
		return tileEntity;
	}
	
}
