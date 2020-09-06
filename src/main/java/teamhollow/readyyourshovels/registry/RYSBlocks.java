package teamhollow.readyyourshovels.registry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovelsCore;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovelsCore.MODID)
public class RYSBlocks {
    public static final Block CLAY_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block DIRT_BRICK = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block GOLD_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(2.0F, 2.5F).sound(SoundType.GROUND));
    public static final Block IRON_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 2.0F).sound(SoundType.GROUND));


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> registry) {
        registry.getRegistry().register(CLAY_DEPOSIT.setRegistryName("clay_deposit"));
        registry.getRegistry().register(DIRT_BRICK.setRegistryName("dirt_bricks"));
        registry.getRegistry().register(GOLD_DEPOSIT.setRegistryName("gold_deposit"));
        registry.getRegistry().register(IRON_DEPOSIT.setRegistryName("iron_deposit"));
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> registry) {
        RYSItems.register(registry, new BlockItem(CLAY_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(DIRT_BRICK, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(GOLD_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(IRON_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    }
}
