package group.rys.core.registry.other;

import group.rys.core.registry.ModEffects;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;

public class ModFoods {
	
	public static final Food orange = new Food.Builder().hunger(3).saturation(0.3F).effect(new EffectInstance(ModEffects.decrease_debuff, 50, 0, false, false), 1.0F).build();
	public static final Food apricot = new Food.Builder().hunger(3).saturation(0.3F).effect(new EffectInstance(ModEffects.increase_buff, 50, 0, false, false), 1.0F).build();
	public static final Food rotten_orange = new Food.Builder().hunger(3).saturation(0.3F).effect(new EffectInstance(ModEffects.increase_debuff, 50, 0, false, false), 1.0F).build();
	public static final Food rotten_apricot = new Food.Builder().hunger(3).saturation(0.3F).effect(new EffectInstance(ModEffects.decrease_buff, 50, 0, false, false), 1.0F).build();
	
}
