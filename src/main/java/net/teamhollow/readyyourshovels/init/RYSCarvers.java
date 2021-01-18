package net.teamhollow.readyyourshovels.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCaveCarver;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCanyonCarver;

public class RYSCarvers {
    public static final Carver<ProbabilityConfig> TOUGH_DIRT_CAVE = register(ToughDirtCaveCarver.id, new ToughDirtCaveCarver(ProbabilityConfig.CODEC, 256));
    public static final Carver<ProbabilityConfig> TOUGH_DIRT_CANYON = register(ToughDirtCanyonCarver.id, new ToughDirtCanyonCarver(ProbabilityConfig.CODEC));

    public RYSCarvers() {}

    private static <C extends CarverConfig, F extends Carver<C>> F register(String id, F carver) {
        return Registry.register(Registry.CARVER, new Identifier(ReadyYourShovels.MOD_ID, id), carver);
    }
}
