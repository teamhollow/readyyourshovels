package group.rys.common.world.gen.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import group.rys.core.util.Reference;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class ApplyFortune extends LootFunction {
	
	private final int bonus;
	private final RandomValueRange range;
	
	protected ApplyFortune(ILootCondition[] conditionsIn, int bonusIn, RandomValueRange rangeIn) {
		super(conditionsIn);
		this.bonus = bonusIn;
		this.range = rangeIn;
	}
	
	protected ItemStack doApply(ItemStack stack, LootContext context) {
		ItemStack stack_1 = context.get(LootParameters.TOOL);
		
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack_1);
		
		stack.setCount(context.getRandom().nextInt((int) range.getMax()) + (int) range.getMin() + (fortune * this.bonus));
		
		return stack;
	}
	
	public static class Serializer extends LootFunction.Serializer<ApplyFortune> {
		
		public Serializer() {
			super(new ResourceLocation(Reference.MOD_ID, "apply_fortune"), ApplyFortune.class);
		}
		
		public void serialize(JsonObject object, ApplyFortune functionClazz, JsonSerializationContext serializationContext) {
			super.serialize(object, functionClazz, serializationContext);
			object.addProperty("bonus", functionClazz.bonus);
			object.add("count", serializationContext.serialize(functionClazz.range));
		}
		
		public ApplyFortune deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
			int bonusIn = object.get("bonus").getAsInt();
			RandomValueRange rangeIn = JSONUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class);
			return new ApplyFortune(conditionsIn, bonusIn, rangeIn);
		}
		
	}
	
}
