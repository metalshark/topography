package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;

public class RegistryHelper {
	
	private static RegistryHelper INSTANCE = new RegistryHelper();
	
	private DynamicRegistries.Impl implRegistries;
	
	public static void UpdateRegistries(DynamicRegistries.Impl impl) {
		INSTANCE.implRegistries = impl;
	}
	
	public static DynamicRegistries.Impl getRegistry() {
		return INSTANCE.implRegistries;
	}
	
	public static Registry<Biome> getBiomeRegistry() {
		return INSTANCE.implRegistries.getRegistry(Registry.BIOME_KEY);
	}
	
	public static Registry<Structure<?>> getStructureRegistry() {
		return INSTANCE.implRegistries.getRegistry(Registry.STRUCTURE_FEATURE_KEY);
	}
	
	public static void registerRecipe() {
		
	}
	
	public static Object get(MutableRegistry registry, String id) {
		RegistryKey key = RegistryKey.getOrCreateKey(registry.getRegistryKey(), new ResourceLocation(id));
		return registry.getOrThrow(key);
	}
}
