package net.teamhollow.readyyourshovels.init.compat;

import io.github.haykam821.columns.block.ColumnBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.item.RYSItemGroup;

@SuppressWarnings("unused")
public class RYSColumnBlocks {
    public static final Block TOUGH_DIRT_COLUMN = register("tough_dirt_column", RYSBlocks.TOUGH_DIRT);
    public static final Block DIRT_BRICK_COLUMN = register("dirt_brick_column", RYSBlocks.DIRT_BRICKS);
    public static final Block SMOOTH_DIRT_COLUMN = register("smooth_dirt_column", RYSBlocks.SMOOTH_DIRT);
    public static final Block PEAT_BRICK_COLUMN = register("peat_brick_column", RYSBlocks.PEAT_BRICKS);

    /*
     * REGISTRY
     */

    private static Block register(String id, Block base) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);

        Block registeredBlock = Registry.register(Registry.BLOCK, identifier, new ColumnBlock(FabricBlockSettings.copy(base)));
        Registry.register(Registry.ITEM, identifier, new BlockItem(registeredBlock, new FabricItemSettings().group(RYSItemGroup.INSTANCE)));

        return registeredBlock;
    }
}
