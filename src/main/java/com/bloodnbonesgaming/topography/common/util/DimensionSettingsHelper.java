package com.bloodnbonesgaming.topography.common.util;

import java.util.function.Supplier;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;

public class DimensionSettingsHelper {
	
	public Supplier<DimensionSettings> defaultOverworld() {
		return null;
	}
	
	public static DimensionSettings get(String id) throws Exception {
		RegistryKey<DimensionSettings> key = RegistryKey.getOrCreateKey(Registry.NOISE_SETTINGS_KEY, new ResourceLocation(id));
		DimensionSettings settings = WorldGenRegistries.NOISE_SETTINGS.getValueForKey(key);
		
		if (settings == null) {
			throw new Exception("No DimensionSettings register for id " + id);
		}
		return settings;
	}
}
