package net.teamhollow.readyyourshovels.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.world.gen.feature.AntHillFeature;

public class RYSConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> DIRT_CAVE_DAYROOT = register(
        "dirt_cave_dayroot",
        RYSFeatures.DIRT_CAVE_DAYROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(YOffset.fixed(-64), YOffset.fixed(128))
            .spreadHorizontally()
            .repeat(8)
            .repeat(8)
    );
    public static final ConfiguredFeature<?, ?> DIRT_CAVE_TOUGHROOT = register(
        "dirt_cave_toughroot",
        RYSFeatures.DIRT_CAVE_TOUGHROOT.configure(FeatureConfig.DEFAULT)
            .rangeOf(YOffset.fixed(-64), YOffset.fixed(128))
            .spreadHorizontally()
            .repeat(16)
            .repeat(16)
    );

    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_PATCH_COBBLESTONE = register(
        "tough_dirt_patch_cobblestone",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.COBBLESTONE,
                33
            )
        )
        .rangeOf(YOffset.getBottom(), YOffset.fixed(79))
        .spreadHorizontally()
        .repeat(4)
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
        .rangeOf(YOffset.fixed(0), YOffset.getTop())
        .spreadHorizontally()
        .repeat(12)
    );
    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_DEPOSIT_GOLD = register(
        "tough_dirt_deposit_gold",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.GOLD_DEPOSIT,
                9
            )
        )
        .averageDepth(YOffset.fixed(-16), 48)
        .spreadHorizontally()
        .repeat(4)
    );
    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_DEPOSIT_IRON = register(
        "tough_dirt_deposit_iron",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.IRON_DEPOSIT,
                9
            )
        )
        .averageDepth(YOffset.fixed(32), 48)
        .spreadHorizontally()
        .repeat(20)
    );
    public static final ConfiguredFeature<?, ?> TOUGH_DIRT_DEPOSIT_PEAT = register(
        "tough_dirt_deposit_peat",
        Feature.ORE.configure(
            new OreFeatureConfig(
                Rules.TOUGH_DIRT,
                States.PEAT_DEPOSIT,
                17
            )
        )
        .averageDepth(YOffset.fixed(96), 96)
        .spreadHorizontally()
        .repeat(20)
    );

    public static final ConfiguredFeature<?, ?> PATCH_TOUGHROOT_STEM = register(
        "patch_toughroot_stem",
        Feature.SIMPLE_RANDOM_SELECTOR.configure(
            new SimpleRandomFeatureConfig(
                ImmutableList.of(
                    () -> Feature.NO_BONEMEAL_FLOWER.configure(
                        new RandomPatchFeatureConfig.Builder(
                            new SimpleBlockStateProvider(States.TOUGHROOT_STEM),
                            SimpleBlockPlacer.INSTANCE
                        ).tries(64).build()
                    )
                )
            )
        )
        .repeat(UniformIntDistribution.of(-1, 4))
        .decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE)
        .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP)
        .repeat(5)
    );
    public static final ConfiguredFeature<?, ?> PATCH_CAVE_CARROT = register(
        "patch_cave_carrot",
        Feature.SIMPLE_RANDOM_SELECTOR.configure(
            new SimpleRandomFeatureConfig(
                ImmutableList.of(
                    () -> Feature.NO_BONEMEAL_FLOWER.configure(
                        new RandomPatchFeatureConfig.Builder(
                            new SimpleBlockStateProvider(States.CAVE_CARROT),
                            SimpleBlockPlacer.INSTANCE
                        ).tries(64).build()
                    )
                )
            )
        )
        .rangeOf(YOffset.getBottom(), YOffset.fixed(63))
        .repeat(UniformIntDistribution.of(-3, 4))
        .decorate(ConfiguredFeatures.Decorators.SPREAD_32_ABOVE)
        .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP)
        .repeat(5)
    );

    public static final ConfiguredStructureFeature<?, ?> ANT_HILL = register(AntHillFeature.id, RYSStructureFeatures.ANT_HILL.configure(DefaultFeatureConfig.DEFAULT));

    public RYSConfiguredFeatures() {}

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }
    private static <FC extends FeatureConfig> ConfiguredStructureFeature<FC, ?> register(String id, ConfiguredStructureFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(ReadyYourShovels.MOD_ID, id), configuredFeature);
    }

    private static final class States {
        private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.getDefaultState();
        private static final BlockState CLAY_DEPOSIT = RYSBlocks.CLAY_DEPOSIT.getDefaultState();
        private static final BlockState GOLD_DEPOSIT = RYSBlocks.GOLD_DEPOSIT.getDefaultState();
        private static final BlockState IRON_DEPOSIT = RYSBlocks.IRON_DEPOSIT.getDefaultState();
        private static final BlockState PEAT_DEPOSIT = RYSBlocks.PEAT_DEPOSIT.getDefaultState();
        private static final BlockState TOUGHROOT_STEM = RYSBlocks.TOUGHROOT_STEM.getDefaultState();
        private static final BlockState CAVE_CARROT = RYSBlocks.CAVE_CARROT.getDefaultState();
    }
    private static final class Rules {
        public static final RuleTest TOUGH_DIRT = new BlockMatchRuleTest(RYSBlocks.TOUGH_DIRT);
    }
}
