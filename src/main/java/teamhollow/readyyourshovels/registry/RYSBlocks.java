package teamhollow.readyyourshovels.registry;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamhollow.readyyourshovels.ReadyYourShovels;
import teamhollow.readyyourshovels.block.AntNestBlock;
import teamhollow.readyyourshovels.tag.RYSBlockTags;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ReadyYourShovels.MODID)
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

    public static final Block PEAT_BLOCK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));

    public static final Block PEAT_BRICK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
    public static final Block PEAT_BRICK_SLAB = new SlabBlock(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
    public static final Block PEAT_BRICK_STAIRS = new StairsBlock(PEAT_BRICK::getDefaultState, Block.Properties.create(Material.ROCK, MaterialColor.BROWN).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
    public static final Block PEAT_BRICK_WALL = new WallBlock(AbstractBlock.Properties.from(PEAT_BRICK));


    public static final Block OAK_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block BIRCH_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block SPRUCE_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block DARK_OAK_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block ACACIA_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block JUNGLE_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block CRIMSON_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
    public static final Block WARPED_PLANTER_BOX = new Block(Block.Properties
            .create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));

    public static final Block ANT_NEST = new AntNestBlock(Block.Properties.create(Material.EARTH).harvestTool(ToolType.SHOVEL).tickRandomly().setRequiresTool().harvestLevel(0).hardnessAndResistance(1.5F, 2.0F).sound(SoundType.GROUND));


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

        registry.getRegistry().register(PEAT_BLOCK.setRegistryName("peat_block"));

        registry.getRegistry().register(PEAT_BRICK.setRegistryName("peat_bricks"));
        registry.getRegistry().register(PEAT_BRICK_SLAB.setRegistryName("peat_brick_slab"));
        registry.getRegistry().register(PEAT_BRICK_STAIRS.setRegistryName("peat_brick_stairs"));
        registry.getRegistry().register(PEAT_BRICK_WALL.setRegistryName("peat_brick_wall"));

        registry.getRegistry().register(OAK_PLANTER_BOX.setRegistryName("oak_planter_box"));
        registry.getRegistry().register(BIRCH_PLANTER_BOX.setRegistryName("birch_planter_box"));
        registry.getRegistry().register(SPRUCE_PLANTER_BOX.setRegistryName("spruce_planter_box"));
        registry.getRegistry().register(DARK_OAK_PLANTER_BOX.setRegistryName("dark_oak_planter_box"));
        registry.getRegistry().register(ACACIA_PLANTER_BOX.setRegistryName("acacia_planter_box"));
        registry.getRegistry().register(JUNGLE_PLANTER_BOX.setRegistryName("jungle_planter_box"));
        registry.getRegistry().register(CRIMSON_PLANTER_BOX.setRegistryName("crimson_planter_box"));
        registry.getRegistry().register(WARPED_PLANTER_BOX.setRegistryName("warped_planter_box"));

        registry.getRegistry().register(ANT_NEST.setRegistryName("ant_nest"));
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

        RYSItems.register(registry, new BlockItem(PEAT_BLOCK, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(PEAT_BRICK, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(PEAT_BRICK_SLAB, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(PEAT_BRICK_STAIRS, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(PEAT_BRICK_WALL, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(OAK_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(BIRCH_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(SPRUCE_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(DARK_OAK_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(ACACIA_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(JUNGLE_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(CRIMSON_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
        RYSItems.register(registry, new BlockItem(WARPED_PLANTER_BOX, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

        RYSItems.register(registry, new BlockItem(ANT_NEST, (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    }

    public static boolean blockIsOfPlanterBox(BlockState blockState) {
        return blockState.isIn(RYSBlockTags.PLANTER_BOXES);
    }

    ;
}
