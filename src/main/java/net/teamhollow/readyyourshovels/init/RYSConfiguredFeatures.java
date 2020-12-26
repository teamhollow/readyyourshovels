package net.teamhollow.readyyourshovels.init;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

public class RYSConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> DIRT_CAVE = register(
        "dirt_cave",
        RYSFeatures.DIRT_CAVE.configure(new DefaultFeatureConfig())
            .spreadHorizontally()
    );
    public static final ConfiguredFeature<?, ?> DIRT_CAVE_DAYROOT = register(
        "dirt_cave_dayroot",
        RYSFeatures.DIRT_CAVE_DAYROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(128)
            .spreadHorizontally()
            .repeat(25)
    );

    public static final ConfiguredFeature<?, ?> DIRT_CAVE_COBBLESTONE = register(
        "dirt_cave_cobblestone",
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
    public static final ConfiguredFeature<?, ?> DIRT_CAVE_CLAY_DEPOSIT = register(
        "dirt_cave_clay_deposit",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.CLAY_DEPOSIT,
                9
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 128)))
        .spreadHorizontally()
        .repeat(18)
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
        private static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.getDefaultState();
        private static final BlockState CLAY_DEPOSIT = RYSBlocks.CLAY_DEPOSIT.getDefaultState();
    }
    private static final class Rules {
        public static final RuleTest TOUGH_DIRT = new BlockMatchRuleTest(RYSBlocks.TOUGH_DIRT);
    }
}
