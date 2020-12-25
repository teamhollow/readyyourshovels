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
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

public class RYSConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> DIRT_CAVE = register(
        "dirt_cave",
        RYSFeatures.DIRT_CAVE.configure(new DefaultFeatureConfig())
            .spreadHorizontally()
            .repeat(20)
    );
    public static final ConfiguredFeature<?, ?> DAYROOT = register(
        "dayroot",
        RYSFeatures.DAYROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(128)
            .spreadHorizontally()
            .repeat(25)
    );

    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_MOSSY_COBBLESTONE = register(
        "tough_dirt_mossy_cobblestone",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.MOSSY_COBBLESTONE,
                33
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 80)))
        .spreadHorizontally()
        .repeat(5)
    );
    public static final ConfiguredFeature<?, ?> DEPOSIT_CLAY = register(
        "deposit_clay",
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
    public static final ConfiguredFeature<?, ?> DEPOSIT_PEAT = register(
        "deposit_peat",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.PEAT_DEPOSIT,
                12
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 128)))
        .spreadHorizontally()
        .repeat(20)
    );
    public static final ConfiguredFeature<?, ?> DEPOSIT_IRON = register(
        "deposit_iron",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.IRON_DEPOSIT,
                9
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 64)))
        .spreadHorizontally()
        .repeat(20)
    );
    public static final ConfiguredFeature<?, ?> DEPOSIT_GOLD = register(
        "deposit_gold",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.GOLD_DEPOSIT,
                6
            )
        )
        .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(0, 0, 32)))
        .spreadHorizontally()
        .repeat(2)
    );

    public static final ConfiguredStructureFeature<?, ?> ANT_HILL = register(
        AntHillFeature.id,
        RYSStructureFeatures.ANT_HILL.configure(DefaultFeatureConfig.DEFAULT)
    );

    public RYSConfiguredFeatures() {}

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }
    private static <FC extends FeatureConfig> ConfiguredStructureFeature<FC, ?> register(String id, ConfiguredStructureFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }

    private static class States {
        private static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.getDefaultState();
        private static final BlockState CLAY_DEPOSIT = RYSBlocks.CLAY_DEPOSIT.getDefaultState();
        private static final BlockState PEAT_DEPOSIT = RYSBlocks.PEAT_DEPOSIT.getDefaultState();
        private static final BlockState IRON_DEPOSIT = RYSBlocks.IRON_DEPOSIT.getDefaultState();
        private static final BlockState GOLD_DEPOSIT = RYSBlocks.GOLD_DEPOSIT.getDefaultState();
    }
    private static final class Rules {
        public static final RuleTest TOUGH_DIRT = new BlockMatchRuleTest(RYSBlocks.TOUGH_DIRT);
    }
}
