package group.rys.common.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class SphereConfig implements IFeatureConfig {
	
	public final BlockState target;
	public final BlockState state;
	public final int radius;
	public final float integrity;
	
	public SphereConfig(BlockState target, BlockState state, int radius, float integrity) {
		this.target = target;
		this.state = state;
		this.radius = radius;
		this.integrity = integrity;
	}
	
	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(
				ops.createString("target"), BlockState.serialize(ops, this.target).getValue(),
				ops.createString("state"), BlockState.serialize(ops, this.state).getValue(),
				ops.createString("radius"), ops.createInt(this.radius),
				ops.createString("integrity"), ops.createFloat(this.integrity)
		)));
	}
	
	public static <T> SphereConfig deserialize(Dynamic<T> p_214707_0_) {
		BlockState target = p_214707_0_.get("target").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		BlockState state = p_214707_0_.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
		int radius = p_214707_0_.get("radius").asInt(0);
		float integrity = p_214707_0_.get("integrity").asFloat(0.0F);
		
		return new SphereConfig(target, state, radius, integrity);
	}
	
}
