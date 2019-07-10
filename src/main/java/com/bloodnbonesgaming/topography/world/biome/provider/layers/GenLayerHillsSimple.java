package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.List;
import java.util.Map;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerHillsSimple extends GenLayer
{
	private final Map<Integer, List<Integer>> hills;
	
	public GenLayerHillsSimple(long seed, final GenLayer parent, final Map<Integer, List<Integer>> hills)
	{
		super(seed);
		this.parent = parent;
		this.hills = hills;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);
        final boolean nullData = this.hills.size() == 0;
        
        for (int z = 0; z < depth; z++)
        {
            for (int x = 0; x < width; x++)
            {
            	this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
            	
            	final int parentIndex = x + 1 + (z + 1) * (width + 2);
            	final int currentBiome = parentInts[parentIndex];
            	
            	if (!nullData)
            	{
            		if (this.nextInt(3) != 0)
            		{
            			returnInts[x + z * width] = currentBiome;
            		}
            		else
            		{
            			final List<Integer> hills = this.hills.get(currentBiome);
            			
            			if (hills != null)
            			{
            				int hill = hills.get(nextInt(hills.size()));
            				final int biome1 = parentInts[x + 1 + (z) * (width + 2)];
        					final int biome2 = parentInts[x + 2 + (z + 1) * (width + 2)];
                            final int biome3 = parentInts[x + (z + 1) * (width + 2)];
                            final int biome4 = parentInts[x + 1 + (z + 2) * (width + 2)];
        					int count = 0;
        					
        					if (biome1 == currentBiome)
                            {
                                ++count;
                            }

                            if (biome2 == currentBiome)
                            {
                                ++count;
                            }

                            if (biome3 == currentBiome)
                            {
                                ++count;
                            }

                            if (biome4 == currentBiome)
                            {
                                ++count;
                            }
        					
        					if (count >= 3)
        					{
        						returnInts[x + z * width] = hill;
        					}
        					else
        					{
        						returnInts[x + z * width] = currentBiome;
        					}
            			}
            			else
            			{
            				returnInts[x + z * width] = currentBiome;
            			}
            		}
            	}
            	else
            	{
            		returnInts[x + z * width] = currentBiome;
            	}
			}
        }
		return returnInts;
	}
}