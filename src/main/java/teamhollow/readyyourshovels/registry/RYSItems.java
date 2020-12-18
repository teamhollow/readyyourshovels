package teamhollow.readyyourshovels.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.item.PeatItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovels.MODID)
public class RYSItems {
    public static final Item PEAT = new PeatItem(new Item.Properties().group(ItemGroup.MISC));


    public static void register(RegistryEvent.Register<Item> registry, Item item, String id) {
        if (item instanceof BlockItem) {
            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        item.setRegistryName(new ResourceLocation(ReadyYourShovels.MODID, id));

        registry.getRegistry().register(item);
    }

    public static void register(RegistryEvent.Register<Item> registry, Item item) {

        if (item instanceof BlockItem && item.getRegistryName() == null) {
            item.setRegistryName(((BlockItem) item).getBlock().getRegistryName());

            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        registry.getRegistry().register(item);
    }

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> registry) {
        registry.getRegistry().register(PEAT.setRegistryName("peat"));
    }
}
