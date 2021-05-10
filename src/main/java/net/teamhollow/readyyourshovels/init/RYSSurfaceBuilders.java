package net.teamhollow.readyyourshovels.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.surfacebuilder.ToughDirtSurfaceBuilder;

public class RYSSurfaceBuilders {
    public static final SurfaceBuilder<TernarySurfaceConfig> TOUGH_DIRT = register("tough_dirt", new ToughDirtSurfaceBuilder(TernarySurfaceConfig.CODEC));

    private static <C extends SurfaceConfig, F extends SurfaceBuilder<C>> F register(String id, F surfaceBuilder) {
        return Registry.register(Registry.SURFACE_BUILDER, new Identifier(ReadyYourShovels.MOD_ID, id), surfaceBuilder);
    }
}
