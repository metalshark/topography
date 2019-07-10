package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class BiomeHelper {
	
	public static BiomeEntry generateBiomeEntry(final int id, final int weight)
	{
		final Biome biome = Biome.getBiomeForId(id);
		
		if (biome != null)
		{
			return new BiomeEntry(biome, weight);
		}
		else
		{
			Topography.instance.getLog().error("No biome can be found with id: " + id);
		}
		return null;
	}
}