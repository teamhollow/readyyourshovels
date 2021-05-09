package net.teamhollow.readyyourshovels.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.RavineCarverConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCanyonCarver;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCaveCarver;

public class RYSCarvers {
    public static final Carver<CaveCarverConfig> TOUGH_DIRT_CAVE = register(ToughDirtCaveCarver.id, new ToughDirtCaveCarver(CaveCarverConfig.CAVE_CODEC));
    public static final Carver<RavineCarverConfig> TOUGH_DIRT_CANYON = register(ToughDirtCanyonCarver.id, new ToughDirtCanyonCarver(RavineCarverConfig.RAVINE_CODEC));

    private static <C extends CarverConfig, F extends Carver<C>> F register(String id, F carver) {
        return Registry.register(Registry.CARVER, new Identifier(ReadyYourShovels.MOD_ID, id), carver);
    }
}
