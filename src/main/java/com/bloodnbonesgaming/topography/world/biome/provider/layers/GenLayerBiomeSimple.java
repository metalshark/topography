package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.List;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class GenLayerBiomeSimple extends GenLayer
{
	private final List<BiomeEntry> biomes;
	private final List<BiomeEntry> oceanBiomes;
	
	public GenLayerBiomeSimple(long seed, final GenLayer parent, final List<BiomeEntry> biomes, final List<BiomeEntry> oceanBiomes)
	{
		super(seed);
		this.parent = parent;
		this.biomes = biomes;
		this.oceanBiomes = oceanBiomes;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		final int arrayLength = width * depth;
		final int[] parentInts;
		final int[] returnInts = IntCache.getIntCache(arrayLength);
		
		if (parent != null)
		{
			parentInts = parent.getInts(chunkX, chunkZ, width, depth);
		}
		else
		{
			parentInts = IntCache.getIntCache(arrayLength);
			
			for (int i = 0; i < parentInts.length; i++)
			{
				parentInts[i] = 0;
			}
		}
		
		for (int z = 0; z < depth; z++)
		{
			for (int x = 0; x < width; x++)
			{
				this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
				final int index = x + z * width;
				final int currentBiome = parentInts[index];
				int warmth = currentBiome & -3841;
				
				switch (warmth) {
					case 0: {
						if (this.oceanBiomes.size() > 0) {
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(this.oceanBiomes).biome);
							break;
						}
					}
					case 1:
					case 2:
					case 3: 
					case 4: {
						if (this.biomes.size() > 0) {
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(this.biomes).biome);
							break;
						}
						if (this.oceanBiomes.size() > 0) {
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(this.oceanBiomes).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					default: {
						returnInts[index] = parentInts[index];
						break;
					}
				}
			}
		}
		return returnInts;
	}
	
	protected net.minecraftforge.common.BiomeManager.BiomeEntry getWeightedBiomeEntry(final List<BiomeEntry> biomeList)
    {
		boolean modded = false;
        //java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = net.minecraft.util.WeightedRandom.getTotalWeight(biomeList);
        int weight = modded?nextInt(totalWeight):nextInt(totalWeight / 10) * 10;
        return (net.minecraftforge.common.BiomeManager.BiomeEntry)net.minecraft.util.WeightedRandom.getRandomItem(biomeList, weight);
    }
}