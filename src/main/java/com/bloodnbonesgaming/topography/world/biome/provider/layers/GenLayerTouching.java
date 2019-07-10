package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerTouching", classExplaination = "This layer is meant to be a combined/improved version of the edge and mushroom island layers. "
+ "The idea is you set a biome or list of biomes, a list of biomes to look for around it, a required count of those biomes, a replacement biome and optionally a random chance. "
+ "If the biome is found, touching >= count biomes from the list to look for, then it is replaced with the replacement biome. "
+ "If using a chance, this has a 1/chance chance of happening."
+ " Check the Jungle_Islands preset for many commented examples.")
public class GenLayerTouching extends GenLayer{
	
	private List<GenLayerTouchingData> dataList = new ArrayList<GenLayerTouchingData>();

	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with its base seed and parent layer.")
	public GenLayerTouching(final long seed, final GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);

        for (int z = 0; z < depth; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
                int biome = parentInts[x + 1 + (z + 1) * (width + 2)];
                
                if (dataList.size() > 0)
                {
                	//Get each touching biome
                	final int down  = parentInts[x + 1 + (z + 1 - 1) * (width + 2)];
					final int right = parentInts[x + 1 + 1 + (z + 1) * (width + 2)];
					final int left  = parentInts[x + 1 - 1 + (z + 1) * (width + 2)];
					final int up    = parentInts[x + 1 + (z + 1 + 1) * (width + 2)];
					
					final int downRight = parentInts[x + 1 + 1 + (z + 1 - 1) * (width + 2)];
					final int downLeft = parentInts[x + 1 - 1 + (z + 1 - 1) * (width + 2)];
					final int upRight = parentInts[x + 1 + 1 + (z + 1 + 1) * (width + 2)];
					final int upLeft = parentInts[x + 1 - 1 + (z + 1 + 1) * (width + 2)];
					
					
					
					
                	for (final GenLayerTouchingData data : this.dataList)
                    {
                    	if (data.usedForBiome(biome))
                    	{
                    		int count = 0;
                    		
                    		if (data.replace(down))
                    		{
                    			count++;
                    		}
                    		if (data.replace(right))
                    		{
                    			count++;
                    		}
                    		if (data.replace(left))
                    		{
                    			count++;
                    		}
                    		if (data.replace(up))
                    		{
                    			count++;
                    		}
                    		if (data.replace(downRight))
                    		{
                    			count++;
                    		}
                    		if (data.replace(downLeft))
                    		{
                    			count++;
                    		}
                    		if (data.replace(upRight))
                    		{
                    			count++;
                    		}
                    		if (data.replace(upLeft))
                    		{
                    			count++;
                    		}
                    		
                    		if (count >= data.getRequiredCount())
                    		{
                    			if (data.getChance() > 0)
                    			{
                    				if (this.nextInt(data.getChance()) == 0)
                    				{
                    					biome = data.getReplacement();
                    				}
                    			}
                    			else
                    			{
                        			biome = data.getReplacement();
                    			}
                    			break;
                    		}
                    	}
                    }
                }
                returnInts[x + z * width] = biome;
            }
        }
		return returnInts;
	}
	
//	@ScriptArgs(args = {ArgType.BIOME_INT_ARRAY, ArgType.BIOME_INT_ARRAY, ArgType.BIOME_INT_ARRAY, ArgType.INT, ArgType.INT, ArgType.INT}, required = {true, false, false, true, true, false})
//	public void addData(final Object... args)
//	{
//		if (args.length == 3)
//		{
//			this.addData((int[]) args[0], null, null, ((Integer) args[1]).intValue(), ((Integer) args[2]).intValue(), 0);
//		}
//		else if (args.length == 4)
//		{
//			this.addData((int[]) args[0], null, null, ((Integer) args[1]).intValue(), ((Integer) args[2]).intValue(), ((Integer) args[3]).intValue());
//		}
//		else if (args.length == 5)
//		{
//			this.addData((int[]) args[0], (int[]) args[1], (int[]) args[2], ((Integer) args[3]).intValue(), ((Integer) args[4]).intValue(), 0);
//		}
//		else if (args.length == 6)
//		{
//			this.addData((int[]) args[0], (int[]) args[1], (int[]) args[2], ((Integer) args[3]).intValue(), ((Integer) args[4]).intValue(), ((Integer) args[3]).intValue());
//		}
//	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome IDs, required count, replacement biome ID", notes = "Sets biomes to check neighbors for, the required count of acceptable neighbors and the replacement biome ID.")
	public void addData(final int[] biomes, final int requiredCount, final int replacement)
	{
		this.addData(biomes, null, null, requiredCount, replacement, 0);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT, ArgType.NON_NULL_BIOME_ID, ArgType.INT})
	@ScriptMethodDocumentation(usage = "biome IDs, required count, replacement biome ID, chance", notes = "Sets the biomes to check neighbors for, the required count of acceptable neighbors, the replacement biome ID and a random chance. Chance for it to happen is 1/chance.")
	public void addData(final int[] biomes, final int requiredCount, final int replacement, final int chance)
	{
		this.addData(biomes, null, null, requiredCount, replacement, chance);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.INT, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome IDs, whitelist, blacklist, required count, replacement biome ID", notes = 
	"Sets the biomes to check neighbors for, the whitelist/blacklist, the required count of acceptable neighbors and the replacement biome."
			+ " If there are at least requiredCount number of neighbors nearby that are in the whitelist/not in the blacklist, the biome is replaced with the replacement biome.")
	public void addData(final int[] biomes, final int[] whitelist, final int[] blacklist, final int requiredCount, final int replacement)
	{
		this.addData(biomes, whitelist, blacklist, requiredCount, replacement, 0);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.INT, ArgType.NON_NULL_BIOME_ID, ArgType.INT})
	@ScriptMethodDocumentation(usage = "biome IDs, whitelist, blacklist, required count, replacement biome ID, chance", notes = 
	"Sets the biomes to check neighbors for, the whitelist/blacklist, the required count of acceptable neighbors, the replacement biome and a random chance."
			+ " If there are at least requiredCount number of neighbors nearby that are in the whitelist/not in the blacklist, there is a 1/chance chance for the biome to be replaced with the replacement biome.")
	public void addData(final int[] biomes, final int[] whitelist, final int[] blacklist, final int requiredCount, final int replacement, final int chance)
	{
		this.dataList.add(new GenLayerTouchingData(biomes, whitelist, blacklist, requiredCount, replacement, chance));
	}
}