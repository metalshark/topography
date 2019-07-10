package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.HashMap;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerRiverMixDC", classExplaination = 
"A configurable version of the vanilla GenLayerRiverMix. Uses a biome map and a river map layer to set configurable river biomes for biomes.")
public class GenLayerRiverMixDC extends GenLayer
{
	private final GenLayer biomePatternGeneratorChain;
	private final GenLayer riverPatternGeneratorChain;
    private Map<Integer, Integer> riverBiomes = new HashMap<Integer, Integer>();
	
    @ScriptMethodDocumentation(args = "long, GenLayer, GenLayer", usage = "base layer seed, biome map parent layer, river map layer", notes = 
    		"Constructs the layer, setting the base layer seed and parent layers.")
    public GenLayerRiverMixDC(long seed, final GenLayer biomePatternGeneratorChain, final GenLayer riverPatternGeneratorChain)
    {
    	super(seed);
		
		this.biomePatternGeneratorChain = biomePatternGeneratorChain;
		this.riverPatternGeneratorChain = riverPatternGeneratorChain;
    }
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome ID, river biome ID", notes = "Sets the river biome for the provided biomeID.")
	public void setRiverBiome(final int biome, final int river)
	{
		this.riverBiomes.put(biome, river);
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
						returnInts[i] = Biome.getIdForBiome(Biomes.RIVER);
					}
				}
				else
				{
					if (biomeInts[i] != Biome.getIdForBiome(Biomes.OCEAN) && biomeInts[i] != Biome.getIdForBiome(Biomes.DEEP_OCEAN))
					{
						if (biomeInts[i] == Biome.getIdForBiome(Biomes.ICE_PLAINS))
	                    {
	                    	returnInts[i] = Biome.getIdForBiome(Biomes.FROZEN_RIVER);
	                    }
	                    else if (biomeInts[i] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND) && biomeInts[i] != Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE))
	                    {
	                    	returnInts[i] = riverInts[i] & 255;
	                    }
	                    else
	                    {
	                    	returnInts[i] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
	                    }
					}
					else
					{
						returnInts[i] = biomeInts[i];
					}
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