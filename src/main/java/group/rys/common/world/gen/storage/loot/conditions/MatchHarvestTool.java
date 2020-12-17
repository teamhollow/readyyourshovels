package group.rys.common.world.gen.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import group.rys.core.util.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class MatchHarvestTool implements ILootCondition {
	
	public String harvestTool;
	
	public MatchHarvestTool(String harvestToolIn) {
		this.harvestTool = harvestToolIn;
	}
	
	public boolean test(LootContext context) {
		ItemStack stack = context.get(LootParameters.TOOL);
		String name = stack.getItem().getRegistryName().toString();
		return stack != null && name.contains(this.harvestTool);
	}
	
	public static class Serializer extends ILootCondition.AbstractSerializer<MatchHarvestTool> {
		
		public Serializer() {
			super(new ResourceLocation(Reference.MOD_ID, "match_harvest_tool"), MatchHarvestTool.class);
		}
		
		public void serialize(JsonObject json, MatchHarvestTool value, JsonSerializationContext context) {
			json.addProperty("harvest_tool", value.harvestTool);
		}
		
		public MatchHarvestTool deserialize(JsonObject json, JsonDeserializationContext context) {
			String harvestToolIn = json.get("harvest_tool").getAsString();
			return new MatchHarvestTool(harvestToolIn);
		}
		
	}
	
}
