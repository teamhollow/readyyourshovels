package group.rys.common.world.gen.placement;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.world.gen.placement.IPlacementConfig;

public class CountChanceDepthConfig implements IPlacementConfig {
	
	public final int count;
	public final float chance;
	public final int depth;
	
	public CountChanceDepthConfig(int count, float chance, int depth) {
		this.count = count;
		this.chance = chance;
		this.depth = depth;
	}
	
	public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
		return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("count"), ops.createInt(this.count), ops.createString("chance"), ops.createFloat(this.chance), ops.createString("depth"), ops.createInt(this.depth))));
	}
	
	public static CountChanceDepthConfig deserialize(Dynamic<?> p_214721_0_) {
		int count = p_214721_0_.get("count").asInt(0);
		float chance = p_214721_0_.get("chance").asFloat(0.0F);
		int depth = p_214721_0_.get("depth").asInt(0);
		
		return new CountChanceDepthConfig(count, chance, depth);
	}
	
}
