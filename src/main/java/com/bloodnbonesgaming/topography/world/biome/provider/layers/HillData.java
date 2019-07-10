package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.world.biome.Biome;

public class HillData {
	
	private Map<Integer, int[]> hills = new HashMap<Integer, int[]>();
	private Map<Integer, Integer> mutations = new HashMap<Integer, Integer>();
	
	public void setMutation(final int biome, final int mutation)
	{
		this.mutations.put(biome, mutation);
	}
	
	public Integer getMutation(final int biome)
	{
		return this.mutations.get(biome);
	}
	
	public void setHill(final int biome, final int[] hills)
	{
		this.hills.put(biome, hills);
	}
	
	public int[] getHills(final int biome)
	{
		return this.hills.get(biome);
	}
	
	public void addDefaultHills()
	{
		this.setHill(2, new int[]{17});
		this.setHill(4, new int[]{18});
		this.setHill(27, new int[]{28});
		this.setHill(29, new int[]{1});
		this.setHill(5, new int[]{19});
		this.setHill(32, new int[]{33});
		this.setHill(30, new int[]{31});
		this.setHill(1, new int[]{18, 4});
		this.setHill(12, new int[]{13});
		this.setHill(21, new int[]{22});
		this.setHill(0, new int[]{24});
		this.setHill(3, new int[]{34});
		this.setHill(35, new int[]{36});
		this.setHill(38, new int[]{37});
		this.setHill(39, new int[]{37});
		this.setHill(24, new int[]{4, 1, 24});
	}
	
	public void addDefaultMutations()
	{
		final Iterator<Biome> biomes = Biome.REGISTRY.iterator();
		
		while (biomes.hasNext())
		{
			final Biome biome = biomes.next();
			
			final Biome mutation = Biome.getMutationForBiome(biome);
			
			if (mutation != null)
			{
				this.setMutation(Biome.getIdForBiome(biome), Biome.getIdForBiome(mutation));
			}
		}
	}
	
	public void removeHill(final int biome)
	{
		if (this.hills.containsKey(biome))
		{
			this.hills.remove(biome);
		}
	}
	
	public void removeMutation(final int biome)
	{
		if (this.mutations.containsKey(biome))
		{
			this.mutations.remove(biome);
		}
	}
}