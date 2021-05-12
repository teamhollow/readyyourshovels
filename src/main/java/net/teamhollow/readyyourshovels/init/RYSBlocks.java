package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.*;
import net.teamhollow.readyyourshovels.block.vanilla.PublicStairsBlock;
import net.teamhollow.readyyourshovels.init.compat.RYSColumnBlocks;
import net.teamhollow.readyyourshovels.item.RYSItemGroup;
import net.teamhollow.readyyourshovels.sound.RYSBlockSoundGroup;

import com.google.common.reflect.Reflection;
import java.util.function.ToIntFunction;

@SuppressWarnings({ "unused", "UnstableApiUsage" })
public class RYSBlocks {
    /*
     * TOUGH DIRT
     */

    public static final Block TOUGH_DIRT = register("tough_dirt", new Block(
            FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS).requiresTool()
            .strength(1.0F, 1.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block TOUGH_DIRT_SLAB = register("tough_dirt_slab", new SlabBlock(FabricBlockSettings.copy(TOUGH_DIRT)));
    public static final Block TOUGH_DIRT_STAIRS = register("tough_dirt_stairs", new PublicStairsBlock(TOUGH_DIRT.getDefaultState(), FabricBlockSettings.copy(TOUGH_DIRT)));
    public static final Block TOUGH_DIRT_WALL = register("tough_dirt_wall", new WallBlock(FabricBlockSettings.copy(TOUGH_DIRT)));

    /*
     * TOUGH DIRT COUNTERPARTS
     */

    public static final Block CLAY_DEPOSIT = register("clay_deposit", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS).requiresTool()
            .strength(1.0F, 1.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block GOLD_DEPOSIT = register("gold_deposit", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS, 2).requiresTool()
            .strength(2.0F, 2.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block IRON_DEPOSIT = register("iron_deposit", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS, 1).requiresTool()
            .strength(1.5F, 2.0F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block PEAT_DEPOSIT = register("peat_deposit", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS).requiresTool()
            .strength(1.0F, 1.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );

    public static final Block REGOLITH = register("regolith", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS, 2).requiresTool()
            .strength(2.0F, 2.5F).sounds(RYSBlockSoundGroup.REGOLITH)
        )
    );

    /*
     * DIRT BRICKS
     */

    public static final Block DIRT_BRICKS = register("dirt_bricks", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS).requiresTool()
            .strength(1.0F, 1.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block DIRT_BRICK_SLAB = register("dirt_brick_slab", new SlabBlock(FabricBlockSettings.copy(DIRT_BRICKS)));
    public static final Block DIRT_BRICK_STAIRS = register("dirt_brick_stairs", new PublicStairsBlock(DIRT_BRICKS.getDefaultState(), FabricBlockSettings.copy(DIRT_BRICKS)));
    public static final Block DIRT_BRICK_WALL = register("dirt_brick_wall", new WallBlock(FabricBlockSettings.copy(DIRT_BRICKS)));

    /*
     * SMOOTH DIRT
     */

    public static final Block SMOOTH_DIRT = register("smooth_dirt", new Block(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS).requiresTool()
            .strength(1.0F, 1.5F).sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );
    public static final Block SMOOTH_DIRT_SLAB = register("smooth_dirt_slab", new SlabBlock(FabricBlockSettings.copy(SMOOTH_DIRT)));
    public static final Block SMOOTH_DIRT_STAIRS = register("smooth_dirt_stairs", new PublicStairsBlock(SMOOTH_DIRT.getDefaultState(), FabricBlockSettings.copy(SMOOTH_DIRT)));
    public static final Block SMOOTH_DIRT_WALL = register("smooth_dirt_wall", new WallBlock(FabricBlockSettings.copy(SMOOTH_DIRT)));

    /*
     * PEAT BLOCKS
     */

    public static final Block PEAT_BLOCK = register("peat_block", new Block(
        FabricBlockSettings.of(Material.STONE, MapColor.BROWN)
            .breakByTool(FabricToolTags.PICKAXES).requiresTool()
            .strength(1.5F, 6.0F)
        )
    );
    public static final Block PEAT_BRICKS = register("peat_bricks", new Block(
        FabricBlockSettings.of(Material.STONE, MapColor.BROWN)
            .breakByTool(FabricToolTags.PICKAXES).requiresTool()
            .strength(2.0F, 6.0F)
        )
    );
    public static final Block PEAT_BRICK_SLAB = register("peat_brick_slab", new SlabBlock(FabricBlockSettings.copy(PEAT_BRICKS)));
    public static final Block PEAT_BRICK_STAIRS = register("peat_brick_stairs", new PublicStairsBlock(PEAT_BRICKS.getDefaultState(), FabricBlockSettings.copy(PEAT_BRICKS)));
    public static final Block PEAT_BRICK_WALL = register("peat_brick_wall", new WallBlock(FabricBlockSettings.copy(PEAT_BRICKS)));
    public static final Block CRACKED_PEAT_BRICKS = register("cracked_peat_bricks", new Block(
        FabricBlockSettings.of(Material.STONE, MapColor.BROWN)
            .breakByTool(FabricToolTags.PICKAXES).requiresTool()
            .strength(1.5F, 6.0F)
        )
    );
    public static final Block CHISELED_PEAT_BRICKS = register("chiseled_peat_bricks", new Block(
        FabricBlockSettings.of(Material.STONE, MapColor.BROWN)
            .breakByTool(FabricToolTags.PICKAXES).requiresTool()
            .strength(1.5F, 6.0F)
        )
    );

    /*
     * ANTS
     */

    public static final Block ANT_NEST = register("ant_nest", new AntNestBlock(
        FabricBlockSettings.of(Material.SOIL)
            .breakByTool(FabricToolTags.SHOVELS)
            .requiresTool()
            .hardness(1.0F)
            .resistance(1.5F)
            .sounds(RYSBlockSoundGroup.TOUGH_DIRT)
        )
    );

    /*
     * PLANTER BOXES
     */

    private static final AbstractBlock.Settings PLANTER_BOX_SETTINGS = FabricBlockSettings.of(Material.WOOD).strength(2.5F, 2.5F).sounds(BlockSoundGroup.WOOD);
    public static final Block OAK_PLANTER_BOX = register("oak_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block BIRCH_PLANTER_BOX = register("birch_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block SPRUCE_PLANTER_BOX = register("spruce_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block DARK_OAK_PLANTER_BOX = register("dark_oak_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block ACACIA_PLANTER_BOX = register("acacia_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block JUNGLE_PLANTER_BOX = register("jungle_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block CRIMSON_PLANTER_BOX = register("crimson_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));
    public static final Block WARPED_PLANTER_BOX = register("warped_planter_box", new PlanterBoxBlock(PLANTER_BOX_SETTINGS));

    /*
     * DAYROOT
     */

    public static final Block DAYROOT = register("dayroot", new DayrootBlock(
        FabricBlockSettings.of(Material.PLANT, MapColor.GREEN)
            .ticksRandomly().noCollision()
            .breakInstantly().luminance(13)
            .sounds(BlockSoundGroup.WEEPING_VINES)
        )
    );
    public static final Block DAYROOT_PLANT = register("dayroot_plant", new DayrootPlantBlock(FabricBlockSettings.copy(DAYROOT).luminance(state -> 1)), false);
    public static final Block DAYROOT_CROWN = register("dayroot_crown", new Block(
        FabricBlockSettings.of(Material.SOLID_ORGANIC, MapColor.BROWN)
            .strength(1.0F).luminance(1)
            .breakByTool(FabricToolTags.SHOVELS)
            .sounds(BlockSoundGroup.WART_BLOCK)
        )
    );

    /*
     * FOLIAGE
     */

    public static final Block TOUGHROOT = register("toughroot", new ToughrootBlock(
        FabricBlockSettings.of(Material.REPLACEABLE_PLANT, MapColor.OAK_TAN)
            .noCollision().breakInstantly()
            .sounds(BlockSoundGroup.GRASS)
            .luminance(1)
        )
    );

    public static final Block CAVE_CARROT = register("cave_carrot", new CaveCarrotBlock(
        FabricBlockSettings.of(Material.PLANT, MapColor.GREEN)
            .noCollision().breakInstantly()
            .sounds(BlockSoundGroup.GRASS)
        ), false
    );

    /*
     * FUNCTIONS
     */

    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return (state) -> (Boolean)state.get(Properties.LIT) ? litLevel : 0;
    }

    /*
     * REGISTRY
     */

    private static Block register(String id, Block block, boolean registerItem) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);

        Block registeredBlock = Registry.register(Registry.BLOCK, identifier, block);
        if (registerItem) {
            int maxCount = 64;
            if (block instanceof SignBlock) maxCount = 16;

            Registry.register(Registry.ITEM, identifier, new BlockItem(registeredBlock, new Item.Settings().maxCount(maxCount).group(RYSItemGroup.INSTANCE)));
        }

        return registeredBlock;
    }
    private static Block register(String id, Block block) {
        return register(id, block, true);
    }

    static {
        if (FabricLoader.getInstance().isModLoaded("columns")) {
            Reflection.initialize(RYSColumnBlocks.class);
        }
    }
}
