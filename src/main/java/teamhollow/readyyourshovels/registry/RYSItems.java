package teamhollow.readyyourshovels.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovelsCore;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovelsCore.MODID)
public class RYSItems {
    public static void register(RegistryEvent.Register<Item> registry, Item item, String id) {
        if (item instanceof BlockItem) {
            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        item.setRegistryName(new ResourceLocation(ReadyYourShovelsCore.MODID, id));

        registry.getRegistry().register(item);
    }

    public static void register(RegistryEvent.Register<Item> registry, Item item) {

        if (item instanceof BlockItem && item.getRegistryName() == null) {
            item.setRegistryName(((BlockItem) item).getBlock().getRegistryName());

            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        registry.getRegistry().register(item);
    }
}
