package group.rys.common.world.gen.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import group.rys.core.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class MatchHarvestLevel implements ILootCondition {
	
	private int harvestLevel;
	
	public MatchHarvestLevel(int harvestLevelIn) {
		this.harvestLevel = harvestLevelIn;
	}
	
	public boolean test(LootContext context) {
		ItemStack stack = context.get(LootParameters.TOOL);
		return stack != null && getLevel(stack) >= this.harvestLevel;
	}
	
	private static int getLevel(ItemStack stack) {
		for (int i = 1; i <= 4; i++) {
			Tag<Item> tier_i = ItemTags.getCollection().getOrCreate(new ResourceLocation(Reference.MOD_ID, "tier_" + i));
			
			if (tier_i.contains(stack.getItem())) {
				return i;
			}
		}
		
		return 0;
	}
	
	public static class Serializer extends ILootCondition.AbstractSerializer<MatchHarvestLevel> {
		
		public Serializer() {
			super(new ResourceLocation(Reference.MOD_ID, "match_harvest_level"), MatchHarvestLevel.class);
		}
		
		public void serialize(JsonObject json, MatchHarvestLevel value, JsonSerializationContext context) {
			json.addProperty("harvest_level", value.harvestLevel);
		}
		
		public MatchHarvestLevel deserialize(JsonObject json, JsonDeserializationContext context) {
			int harvestLevelIn = json.get("harvest_level").getAsInt();
			return new MatchHarvestLevel(harvestLevelIn);
		}
		
	}
	
}
