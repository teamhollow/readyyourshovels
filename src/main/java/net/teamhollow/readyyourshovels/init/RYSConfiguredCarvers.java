package net.teamhollow.readyyourshovels.init;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.TrapezoidFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.RavineCarverConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCanyonCarver;
import net.teamhollow.readyyourshovels.world.gen.carver.ToughDirtCaveCarver;

public class RYSConfiguredCarvers {
    public static final ConfiguredCarver<CarverConfig> DIRT_CAVE = register(ToughDirtCaveCarver.id, RYSCarvers.TOUGH_DIRT_CAVE.configure(new CarverConfig(0.14285715F, CarverDebugConfig.create(false, Blocks.POLISHED_BLACKSTONE_BUTTON.getDefaultState()))));
    public static final ConfiguredCarver<RavineCarverConfig> DIRT_CANYON = register(ToughDirtCanyonCarver.id, RYSCarvers.TOUGH_DIRT_CANYON.configure(new RavineCarverConfig(
        0.1625F,
        CarverDebugConfig.create(false, Blocks.DARK_OAK_BUTTON.getDefaultState()),
        YOffset.fixed(10), YOffset.fixed(67), UniformIntDistribution.of(3),
        UniformFloatProvider.create(0.75F, 0.25F), UniformFloatProvider.create(-0.125F, 0.25F), TrapezoidFloatProvider.create(0.0F, 6.0F, 2.0F), 3,
        UniformFloatProvider.create(0.75F, 0.25F), 1.0F, 0.0F
    )));

    public RYSConfiguredCarvers() {}

    private static <WC extends CarverConfig> ConfiguredCarver<WC> register(String id, ConfiguredCarver<WC> configuredCarver) {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_CARVER, new Identifier(ReadyYourShovels.MOD_ID, id), configuredCarver);
    }
}
