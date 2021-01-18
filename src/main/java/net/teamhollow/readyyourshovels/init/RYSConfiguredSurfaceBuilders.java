package net.teamhollow.readyyourshovels.init;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSConfiguredSurfaceBuilders {
    public static final ConfiguredSurfaceBuilder<?> TOUGH_DIRT = register("tough_dirt", RYSSurfaceBuilders.TOUGH_DIRT.withConfig(new TernarySurfaceConfig(
        Blocks.GRASS_BLOCK.getDefaultState(),
        RYSBlocks.TOUGH_DIRT.getDefaultState(),
        Blocks.GRAVEL.getDefaultState()
    )));

    public RYSConfiguredSurfaceBuilders() {}

    private static ConfiguredSurfaceBuilder<?> register(String id, ConfiguredSurfaceBuilder<?> surfaceBuilder) {
        return Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new Identifier(ReadyYourShovels.MOD_ID, id), surfaceBuilder);
    }
}
