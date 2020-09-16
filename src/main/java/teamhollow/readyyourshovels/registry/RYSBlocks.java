package teamhollow.readyyourshovels.registry;

import net.minecraft.block.*;
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
    public static final Block DIRT_BRICK_SLAB = new SlabBlock(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block DIRT_BRICK_STAIRS = new StairsBlock(DIRT_BRICK::getDefaultState, Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block DIRT_BRICK_WALL = new WallBlock(AbstractBlock.Properties.from(DIRT_BRICK));



    public static final Block GOLD_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F, 2.5F).sound(SoundType.GROUND));
    public static final Block IRON_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(1).hardnessAndResistance(1.5F, 2.0F).sound(SoundType.GROUND));
    public static final Block PEAT_DEPOSIT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block REGOLITH = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(2).hardnessAndResistance(2.0F,2.5F).sound(SoundType.GROUND));

    public static final Block SMOOTH_DIRT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block SMOOTH_DIRT_SLAB = new SlabBlock(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block SMOOTH_DIRT_STAIRS = new StairsBlock(SMOOTH_DIRT::getDefaultState, Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block SMOOTH_DIRT_WALL = new WallBlock(AbstractBlock.Properties.from(SMOOTH_DIRT));

    public static final Block TOUGH_DIRT = new Block(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block TOUGH_DIRT_SLAB = new SlabBlock(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block TOUGH_DIRT_STAIRS = new StairsBlock(TOUGH_DIRT::getDefaultState, Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.0F, 1.5F).sound(SoundType.GROUND));
    public static final Block TOUGH_DIRT_WALL = new WallBlock(AbstractBlock.Properties.from(TOUGH_DIRT));



    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> registry) {
        registry.getRegistry().register(CLAY_DEPOSIT.setRegistryName("clay_deposit"));

        registry.getRegistry().register(DIRT_BRICK.setRegistryName("dirt_bricks"));
        registry.getRegistry().register(DIRT_BRICK_SLAB.setRegistryName("dirt_bricks_slab"));
        registry.getRegistry().register(DIRT_BRICK_STAIRS.setRegistryName("dirt_bricks_stairs"));
        registry.getRegistry().register(DIRT_BRICK_WALL.setRegistryName("dirt_bricks_wall"));

        registry.getRegistry().register(GOLD_DEPOSIT.setRegistryName("gold_deposit"));
        registry.getRegistry().register(IRON_DEPOSIT.setRegistryName("iron_deposit"));
        registry.getRegistry().register(PEAT_DEPOSIT.setRegistryName("peat_deposit"));
        registry.getRegistry().register(REGOLITH.setRegistryName("regolith"));

        registry.getRegistry().register(SMOOTH_DIRT.setRegistryName("smooth_dirt"));
        registry.getRegistry().register(SMOOTH_DIRT_SLAB.setRegistryName("smooth_dirt_slab"));
        registry.getRegistry().register(SMOOTH_DIRT_STAIRS.setRegistryName("smooth_dirt_stairs"));
        registry.getRegistry().register(SMOOTH_DIRT_WALL.setRegistryName("smooth_dirt_wall"));

        registry.getRegistry().register(TOUGH_DIRT.setRegistryName("tough_dirt"));
        registry.getRegistry().register(TOUGH_DIRT_SLAB.setRegistryName("tough_dirt_slab"));
        registry.getRegistry().register(TOUGH_DIRT_STAIRS.setRegistryName("tough_dirt_stairs"));
        registry.getRegistry().register(TOUGH_DIRT_WALL.setRegistryName("tough_dirt_wall"));

    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> registry) {
        RYSItems.register(registry, new BlockItem(CLAY_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(DIRT_BRICK, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(DIRT_BRICK_SLAB, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(DIRT_BRICK_STAIRS, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(DIRT_BRICK_WALL, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(GOLD_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(IRON_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(PEAT_DEPOSIT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(REGOLITH, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(SMOOTH_DIRT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(SMOOTH_DIRT_SLAB, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(SMOOTH_DIRT_STAIRS, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(SMOOTH_DIRT_WALL, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(TOUGH_DIRT, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(TOUGH_DIRT_SLAB, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(TOUGH_DIRT_STAIRS, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(TOUGH_DIRT_WALL, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

    }
}
