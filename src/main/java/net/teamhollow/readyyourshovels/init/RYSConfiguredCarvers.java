package net.teamhollow.readyyourshovels.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCaveCarver;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCanyonCarver;

public class RYSConfiguredCarvers {
    public static final ConfiguredCarver<ProbabilityConfig> DIRT_CAVE = register(ToughDirtCaveCarver.id, RYSCarvers.TOUGH_DIRT_CAVE.configure(new ProbabilityConfig(0.14285715F)));
    public static final ConfiguredCarver<ProbabilityConfig> DIRT_CANYON = register(ToughDirtCanyonCarver.id, RYSCarvers.TOUGH_DIRT_CANYON.configure(new ProbabilityConfig(0.1F)));

    public RYSConfiguredCarvers() {}

    private static <WC extends CarverConfig> ConfiguredCarver<WC> register(String id, ConfiguredCarver<WC> configuredCarver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, new Identifier(ReadyYourShovels.MOD_ID, id), configuredCarver);
    }
}