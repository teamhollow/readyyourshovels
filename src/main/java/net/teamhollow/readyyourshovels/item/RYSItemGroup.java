package net.teamhollow.readyyourshovels.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.init.RYSBlocks;

public class RYSItemGroup {
    public static final ItemGroup INSTANCE = FabricItemGroupBuilder.build(
        new Identifier(ReadyYourShovels.MOD_ID, "item_group"),
        () -> RYSBlocks.TOUGH_DIRT.asItem().getDefaultStack()
    );
}
