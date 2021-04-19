package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectHelper {
	public void addPotion(LivingEntity entity, String id, int duration, int amplification, boolean ambient, boolean showParticles) {
		Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(id));
		entity.addPotionEffect(new EffectInstance(effect, duration, amplification, ambient, showParticles));
	}
}
