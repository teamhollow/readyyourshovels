package group.rys.core.registry;

import group.rys.common.block.*;
import group.rys.core.registry.other.ModProperties;
import group.rys.core.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	
	public static final ToughDirtBlock tough_dirt = null;
	public static final ToughDirtBlock mossy_tough_dirt = null;
	public static final Block dirt_bricks = null;
	public static final Block smooth_dirt = null;
	public static final Block regolith = null;
	public static final Block clay_deposit = null;
	public static final Block peat_deposit = null;
	public static final Block iron_deposit = null;
	public static final Block gold_deposit = null;
	public static final Block peat_block = null;
	public static final Block peat_bricks = null;
	public static final SlabBlock tough_dirt_slab = null;
	public static final StairsBlock tough_dirt_stairs = null;
	public static final SlabBlock mossy_tough_dirt_slab = null;
	public static final StairsBlock mossy_tough_dirt_stairs = null;
	public static final WallBlock tough_dirt_wall = null;
	public static final SlabBlock dirt_bricks_slab = null;
	public static final StairsBlock dirt_bricks_stairs = null;
	public static final WallBlock dirt_bricks_wall = null;
	public static final SlabBlock smooth_dirt_slab = null;
	public static final StairsBlock smooth_dirt_stairs = null;
	public static final PlanterBoxBlock planter_box = null;
	public static final FruitSaplingBlock apple_fruit_sapling = null;
	public static final FruitSaplingBlock orange_fruit_sapling = null;
	public static final FruitSaplingBlock apricot_fruit_sapling = null;
	public static final FruitTreeBlock apple_fruit_tree = create("apple_fruit_tree", new FruitTreeBlock(Items.APPLE, Items.APPLE, ModItems.apple_fruit_sapling, ModProperties.fruit_tree));
	public static final FruitTreeBlock orange_fruit_tree = create("orange_fruit_tree", new FruitTreeBlock(ModItems.orange, ModItems.rotten_orange, ModItems.orange_fruit_sapling, ModProperties.fruit_tree));
	public static final FruitTreeBlock apricot_fruit_tree = create("apricot_fruit_tree", new FruitTreeBlock(ModItems.apricot, ModItems.rotten_apricot, ModItems.apricot_fruit_sapling, ModProperties.fruit_tree));

	public static final DayrootBlock dayroot = null;
	
	public static final AnthillBlock anthill = create("anthill", new AnthillBlock(ModProperties.tough_dirt));
	public static final HuntingAnthillBlock hunting_anthill = create("hunting_anthill", new HuntingAnthillBlock(ModProperties.tough_dirt));
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		// Blocks
		registry.register(create("tough_dirt", new ToughDirtBlock(ModProperties.tough_dirt)));
		registry.register(create("mossy_tough_dirt", new ToughDirtBlock(ModProperties.mossy_tough_dirt)));
		registry.register(create("dirt_bricks", new Block(ModProperties.dirt_bricks)));
		registry.register(create("smooth_dirt", new Block(ModProperties.smooth_dirt)));
		registry.register(create("regolith", new Block(ModProperties.regolith)));
		registry.register(create("clay_deposit", new Block(ModProperties.clay_deposit)));
		registry.register(create("peat_deposit", new Block(ModProperties.peat_deposit)));
		registry.register(create("iron_deposit", new Block(ModProperties.iron_deposit)));
		registry.register(create("gold_deposit", new Block(ModProperties.gold_deposit)));
		registry.register(create("peat_block", new Block(ModProperties.tough_dirt)));
		registry.register(create("peat_bricks", new Block(ModProperties.tough_dirt)));
		registry.register(create("tough_dirt_slab", new SlabBlock(ModProperties.tough_dirt)));
		registry.register(create("mossy_tough_dirt_slab", new SlabBlock(ModProperties.mossy_tough_dirt)));
		registry.register(create("tough_dirt_stairs", new StairsBlock(() -> tough_dirt.getDefaultState(), ModProperties.tough_dirt)));
		registry.register(create("mossy_tough_dirt_stairs", new StairsBlock(() -> mossy_tough_dirt.getDefaultState(), ModProperties.mossy_tough_dirt)));
		registry.register(create("tough_dirt_wall", new WallBlock(ModProperties.tough_dirt)));
		registry.register(create("dirt_bricks_slab", new SlabBlock(ModProperties.dirt_bricks)));
		registry.register(create("dirt_bricks_stairs", new StairsBlock(() -> dirt_bricks.getDefaultState(), ModProperties.dirt_bricks)));
		registry.register(create("dirt_bricks_wall", new WallBlock(ModProperties.dirt_bricks)));
		registry.register(create("smooth_dirt_slab", new SlabBlock(ModProperties.smooth_dirt)));
		registry.register(create("smooth_dirt_stairs", new StairsBlock(() -> smooth_dirt.getDefaultState(), ModProperties.smooth_dirt)));
		registry.register(create("planter_box", new PlanterBoxBlock(ModProperties.planter_box)));
		registry.register(create("apple_fruit_sapling", new FruitSaplingBlock(ModBlocks.apple_fruit_tree, ModProperties.fruit_sapling)));
		registry.register(create("orange_fruit_sapling", new FruitSaplingBlock(ModBlocks.orange_fruit_tree, ModProperties.fruit_sapling)));
		registry.register(create("apricot_fruit_sapling", new FruitSaplingBlock(ModBlocks.apricot_fruit_tree, ModProperties.fruit_sapling)));
		registry.register(apple_fruit_tree);
		registry.register(orange_fruit_tree);
		registry.register(apricot_fruit_tree);
		registry.register(create("dayroot", new DayrootBlock(ModProperties.dayroot)));
		
		// TileEntities
		registry.register(anthill);
		registry.register(hunting_anthill);
	}

	public static <T extends Block> T create(String name, T block) {
		block.setRegistryName(Reference.MOD_ID, name);
		return block;
	}
	
}
