package group.rys.core.registry;

import group.rys.common.item.ModMusicDiscItem;
import group.rys.core.registry.other.ModProperties;
import group.rys.core.util.Reference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Reference.MOD_ID)
public class ModItems {
	
	public static final BlockItem tough_dirt = null;
	public static final BlockItem dirt_bricks = null;
	public static final BlockItem smooth_dirt = null;
	public static final BlockItem regolith = null;
	public static final BlockItem clay_deposit = null;
	public static final BlockItem peat_deposit = null;
	public static final BlockItem iron_deposit = null;
	public static final BlockItem gold_deposit = null;
	public static final BlockItem peat_block = null;
	public static final BlockItem peat_bricks = null;
	public static final BlockItem tough_dirt_slab = null;
	public static final BlockItem tough_dirt_stairs = null;
	public static final BlockItem tough_dirt_wall = null;
	public static final BlockItem mossy_tough_dirt = null;
	public static final BlockItem mossy_tough_dirt_stairs = null;
	public static final BlockItem mossy_tough_dirt_slab = null;
	public static final BlockItem dirt_bricks_slab = null;
	public static final BlockItem dirt_bricks_stairs = null;
	public static final BlockItem dirt_bricks_wall = null;
	public static final BlockItem smooth_dirt_slab = null;
	public static final BlockItem smooth_dirt_stairs = null;
	public static final BlockItem planter_box = null;
    public static final BlockItem apple_fruit_sapling = null;
    public static final BlockItem orange_fruit_sapling = null;
    public static final BlockItem apricot_fruit_sapling = null;
	public static final BlockItem dayroot = null;
	public static final BlockItem anthill = null;
	
	public static final Item peat = null;
	public static Item orange = create("orange", new Item(ModProperties.orange));
	public static Item apricot = create("apricot", new Item(ModProperties.apricot));
	public static Item rotten_orange = create("rotten_orange", new Item(ModProperties.rotten_orange));
	public static Item rotten_apricot = create("rotten_apricot", new Item(ModProperties.rotten_apricot));
    public static Item colly_music_disc = create("colly_music_disc", new ModMusicDiscItem(12, ModSounds.RECORD_COLLY, ModProperties.record_colly));
	public static final Item ant_spawn_egg = null;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		// Blocks
		registry.register(create("tough_dirt", new BlockItem(ModBlocks.tough_dirt, ModProperties.item)));
		registry.register(create("dirt_bricks", new BlockItem(ModBlocks.dirt_bricks, ModProperties.item)));
		registry.register(create("smooth_dirt", new BlockItem(ModBlocks.smooth_dirt, ModProperties.item)));
		registry.register(create("regolith", new BlockItem(ModBlocks.regolith, ModProperties.item)));
		registry.register(create("clay_deposit", new BlockItem(ModBlocks.clay_deposit, ModProperties.item)));
		registry.register(create("peat_deposit", new BlockItem(ModBlocks.peat_deposit, ModProperties.item)));
		registry.register(create("iron_deposit", new BlockItem(ModBlocks.iron_deposit, ModProperties.item)));
		registry.register(create("gold_deposit", new BlockItem(ModBlocks.gold_deposit, ModProperties.item)));
		registry.register(create("peat_block", new BlockItem(ModBlocks.peat_block, ModProperties.item)));
		registry.register(create("peat_bricks", new BlockItem(ModBlocks.peat_bricks, ModProperties.item)));
		registry.register(create("tough_dirt_slab", new BlockItem(ModBlocks.tough_dirt_slab, ModProperties.item)));
		registry.register(create("tough_dirt_stairs", new BlockItem(ModBlocks.tough_dirt_stairs, ModProperties.item)));
		registry.register(create("tough_dirt_wall", new BlockItem(ModBlocks.tough_dirt_wall, ModProperties.item)));
		registry.register(create("mossy_tough_dirt", new BlockItem(ModBlocks.mossy_tough_dirt, ModProperties.item)));
		registry.register(create("mossy_tough_dirt_slab", new BlockItem(ModBlocks.mossy_tough_dirt_slab, ModProperties.item)));
		registry.register(create("mossy_tough_dirt_stairs", new BlockItem(ModBlocks.mossy_tough_dirt_stairs, ModProperties.item)));
		registry.register(create("dirt_bricks_slab", new BlockItem(ModBlocks.dirt_bricks_slab, ModProperties.item)));
		registry.register(create("dirt_bricks_stairs", new BlockItem(ModBlocks.dirt_bricks_stairs, ModProperties.item)));
		registry.register(create("dirt_bricks_wall", new BlockItem(ModBlocks.dirt_bricks_wall, ModProperties.item)));
		registry.register(create("smooth_dirt_slab", new BlockItem(ModBlocks.smooth_dirt_slab, ModProperties.item)));
		registry.register(create("smooth_dirt_stairs", new BlockItem(ModBlocks.smooth_dirt_stairs, ModProperties.item)));
		registry.register(create("planter_box", new BlockItem(ModBlocks.planter_box, ModProperties.item)));
        registry.register(create("apple_fruit_sapling", new BlockItem(ModBlocks.apple_fruit_sapling, ModProperties.item)));
        registry.register(create("orange_fruit_sapling", new BlockItem(ModBlocks.orange_fruit_sapling, ModProperties.item)));
        registry.register(create("apricot_fruit_sapling", new BlockItem(ModBlocks.apricot_fruit_sapling, ModProperties.item)));
		registry.register(create("dayroot", new BlockItem(ModBlocks.dayroot, ModProperties.item)));
		
		// TileEntities
		registry.register(create("anthill", new BlockItem(ModBlocks.anthill, ModProperties.item)));
		registry.register(create("hunting_anthill", new BlockItem(ModBlocks.hunting_anthill, ModProperties.item)));
		
		// Items
		registry.register(create("peat", new Item(ModProperties.item)));
		registry.register(orange);
		registry.register(apricot);
		registry.register(rotten_orange);
		registry.register(rotten_apricot);
        registry.register(colly_music_disc);
		registry.register(create("ant_spawn_egg", new SpawnEggItem(ModEntities.gatherer_ant, 4073251, 12531212, ModProperties.item)));
		registry.register(create("queen_ant_spawn_egg", new SpawnEggItem(ModEntities.queen_ant, 4073251, 12531212, ModProperties.item)));
		registry.register(create("hunting_ant_spawn_egg", new SpawnEggItem(ModEntities.hunting_ant, 4073251, 12531212, ModProperties.item)));
	}
	
	public static <T extends Item> T create(String name, T item) {
		item.setRegistryName(Reference.MOD_ID, name);

		if (item instanceof BlockItem) {
			Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
		}
		return item;
	}
	
}
