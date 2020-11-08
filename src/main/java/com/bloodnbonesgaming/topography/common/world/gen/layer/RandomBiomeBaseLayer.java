package com.bloodnbonesgaming.topography.common.world.gen.layer;

import java.util.List;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public class RandomBiomeBaseLayer implements IAreaTransformer0 {
	
	private final List<Biome> biomes;
	private final Registry<Biome> biomeRegistry;
	
	public RandomBiomeBaseLayer(List<Biome> biomes, Registry<Biome> biomeRegistry) {
		this.biomes = biomes;
		this.biomeRegistry = biomeRegistry;
	}

	@Override
	public int apply(INoiseRandom rand, int x, int z) {
		int i = rand.random(biomes.size());
		return biomeRegistry.getId(biomes.get(i));
	}
}
