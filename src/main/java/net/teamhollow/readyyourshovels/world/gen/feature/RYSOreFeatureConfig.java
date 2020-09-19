package net.teamhollow.readyyourshovels.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

public class RYSOreFeatureConfig implements FeatureConfig {
    public static final Codec<RYSOreFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(RYSOreFeatureConfig.Target.field_24898.fieldOf("target").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.target;
        }), BlockState.CODEC.fieldOf("state").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.state;
        }), Codec.INT.fieldOf("size").withDefault(0).forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.size;
        })).apply(instance, RYSOreFeatureConfig::new);
    });
    public final RYSOreFeatureConfig.Target target;
    public final int size;
    public final BlockState state;

    public RYSOreFeatureConfig(RYSOreFeatureConfig.Target target, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = target;
    }

    public static enum Target implements StringIdentifiable {
        TOUGH_DIRT("tough_dirt", (blockState) -> {
            if (blockState == null) {
                return false;
            } else {
                return blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.GRANITE)
                        || blockState.isOf(Blocks.DIORITE) || blockState.isOf(Blocks.ANDESITE);
            }
        });

        public static final Codec<RYSOreFeatureConfig.Target> field_24898 = StringIdentifiable
                .method_28140(RYSOreFeatureConfig.Target::values, RYSOreFeatureConfig.Target::byName);
        private static final Map<String, RYSOreFeatureConfig.Target> nameMap = (Map<String, RYSOreFeatureConfig.Target>) Arrays.stream(values())
                .collect(Collectors.toMap(RYSOreFeatureConfig.Target::getName, (target) -> {
                    return target;
                }));
        private final String name;
        private final Predicate<BlockState> predicate;

        private Target(String name, Predicate<BlockState> predicate) {
            this.name = new Identifier(ReadyYourShovels.MOD_ID, name).toString();
            this.predicate = predicate;
        }

        public String getName() {
            return this.name;
        }

        public static RYSOreFeatureConfig.Target byName(String name) {
            return (RYSOreFeatureConfig.Target) nameMap.get(name);
        }

        public Predicate<BlockState> getCondition() {
            return this.predicate;
        }

        public String asString() {
            return this.name;
        }
    }
}
