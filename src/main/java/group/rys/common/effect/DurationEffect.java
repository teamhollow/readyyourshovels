package group.rys.common.effect;

import group.rys.core.registry.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public class DurationEffect extends Effect {
	
	public DurationEffect(EffectType typeIn, int colorIn) {
		super(typeIn, colorIn);
	}
	
	public void performEffect(LivingEntity entity, int amplifier) {
		int time = (amplifier + 1) * 8;
		
		entity.getActivePotionMap().forEach((Effect effect, EffectInstance effectInstance) -> {
			if (effect != this && !effectInstance.isAmbient() && !effectInstance.isShowIcon()) {
				if (effect.getEffectType() == EffectType.HARMFUL) {
					if (this == ModEffects.decrease_debuff) {
						if (effectInstance.getDuration() - time >= 0) {
							entity.getActivePotionMap().replace(effect, new EffectInstance(effect, effectInstance.getDuration() - time, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isShowIcon()));
						}
					}
					
					if (this == ModEffects.increase_debuff) {
						entity.getActivePotionMap().replace(effect, new EffectInstance(effect, effectInstance.getDuration() + time, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isShowIcon()));
					}
				}
				
				if (effect.getEffectType() == EffectType.BENEFICIAL) {
					if (this == ModEffects.increase_buff) {
						entity.getActivePotionMap().replace(effect, new EffectInstance(effect, effectInstance.getDuration() + time, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isShowIcon()));
					}
					
					if (this == ModEffects.decrease_buff) {
						if (effectInstance.getDuration() - time >= 0) {
							entity.getActivePotionMap().replace(effect, new EffectInstance(effect, effectInstance.getDuration() - time, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isShowIcon()));
						}
					}
				}
			}
		});
		
		super.performEffect(entity, amplifier);
	}
	
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
}
