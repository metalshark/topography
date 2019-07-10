package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.Map;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerRiverMixSimple extends GenLayer
{
	private final GenLayer biomePatternGeneratorChain;
	private final GenLayer riverPatternGeneratorChain;
    private final Map<Integer, Integer> riverBiomes;
	
    public GenLayerRiverMixSimple(long seed, final GenLayer biomePatternGeneratorChain, final GenLayer riverPatternGeneratorChain, final Map<Integer, Integer> riverBiomes)
    {
    	super(seed);
		
		this.biomePatternGeneratorChain = biomePatternGeneratorChain;
		this.riverPatternGeneratorChain = riverPatternGeneratorChain;
		this.riverBiomes = riverBiomes;
    }

    public void initWorldGenSeed(long seed)
    {
        this.biomePatternGeneratorChain.initWorldGenSeed(seed);
        this.riverPatternGeneratorChain.initWorldGenSeed(seed);
        super.initWorldGenSeed(seed);
    }

    public int[] getInts(final int chunkX, final int chunkZ, final int width, final int depth)
    {
        int[] biomeInts = this.biomePatternGeneratorChain.getInts(chunkX, chunkZ, width, depth);
        int[] riverInts = this.riverPatternGeneratorChain.getInts(chunkX, chunkZ, width, depth);
        int[] returnInts = IntCache.getIntCache(width * depth);

		for (int i = 0; i < width * depth; i++)
		{
			if (riverInts[i] == Biome.getIdForBiome(Biomes.RIVER))
			{
				if (!this.riverBiomes.isEmpty())
				{
					final Integer river = this.riverBiomes.get(biomeInts[i]);

					if (river != null)
					{
						if (river > -1)
						{
							returnInts[i] = river;
						}
						else
						{
							returnInts[i] = biomeInts[i];
						}
					}
					else
					{
						returnInts[i] = biomeInts[i];
					}
				}
				else
				{
					returnInts[i] = biomeInts[i];
				}
			}
			else
			{
				returnInts[i] = biomeInts[i];
			}
		}
		return returnInts;
	}
}