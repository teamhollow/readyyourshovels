package net.teamhollow.readyyourshovels.init;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.CountNoiseDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

public class RYSConfiguredFeatures {
    // dirt caves
    public static final ConfiguredFeature<?, ?> DIRT_CAVE = register(
        "dirt_cave",
        RYSFeatures.DIRT_CAVE.configure(new DefaultFeatureConfig())
            .spreadHorizontally()
            .repeat(30)
    );

    public static final ConfiguredFeature<?, ?> DIRT_CAVE_DAYROOT = register(
        "dirt_cave_dayroot",
        RYSFeatures.DIRT_CAVE_DAYROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(128)
            .spreadHorizontally()
            .repeat(8)
            .repeat(8)
    );
    public static final ConfiguredFeature<?, ?> DIRT_CAVE_TOUGHROOT = register(
        "dirt_cave_toughroot",
        RYSFeatures.DIRT_CAVE_TOUGHROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(128)
            .spreadHorizontally()
            .repeat(25)
    );

    // tough dirt replacements
    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_PATCH_COBBLESTONE = register(
        "tough_dirt_patch_cobblestone",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.COBBLESTONE,
                33
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 80)))
        .spreadHorizontally()
        .repeat(5)
    );
    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_DEPOSIT_CLAY = register(
        "tough_dirt_deposit_clay",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.CLAY_DEPOSIT,
                9
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 128)))
        .spreadHorizontally()
        .repeat(32)
    );

    // above ground
    public static final ConfiguredFeature<?, ?> PATCH_TOUGHROOT_STEM = register(
        "patch_toughroot_stem",
        Feature.RANDOM_PATCH.configure(
            new RandomPatchFeatureConfig.Builder(
                new SimpleBlockStateProvider(States.TOUGHROOT_STEM),
                SimpleBlockPlacer.INSTANCE
            ).tries(32).build()
        )
        .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE)
        .decorate(Decorator.COUNT_NOISE.configure(new CountNoiseDecoratorConfig(-0.8D, 5, 10)))
    );

    public static final ConfiguredStructureFeature<?, ?> ANT_HILL = register(AntHillFeature.id, RYSStructureFeatures.ANT_HILL.configure(DefaultFeatureConfig.DEFAULT));

    public RYSConfiguredFeatures() {}

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }
    private static <FC extends FeatureConfig> ConfiguredStructureFeature<FC, ?> register(String id, ConfiguredStructureFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }

    private static class States {
        private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.getDefaultState();
        private static final BlockState CLAY_DEPOSIT = RYSBlocks.CLAY_DEPOSIT.getDefaultState();
        private static final BlockState TOUGHROOT_STEM = RYSBlocks.TOUGHROOT_STEM.getDefaultState();
    }
    private static final class Rules {
        public static final RuleTest TOUGH_DIRT = new BlockMatchRuleTest(RYSBlocks.TOUGH_DIRT);
    }
}
