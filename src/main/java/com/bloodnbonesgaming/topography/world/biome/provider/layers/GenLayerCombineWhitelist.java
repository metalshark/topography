package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerCombineWhitelist", classExplaination = 
"A layer used to combine two parent layers using a whitelist. If the biome from the first parent is in the whitelist, it's used, otherwise the biome from the second parent is used."
+ " This is useful for doing things like combining a heat map and a terrain map.")
public class GenLayerCombineWhitelist extends GenLayer{
	
	private final GenLayer parent2;
	private int[] whitelist;
	
	@ScriptArgs(args = {ArgType.LONG, ArgType.GENLAYER, ArgType.GENLAYER, ArgType.NON_NULL_BIOME_ID_ARRAY})
	@ScriptMethodDocumentation(usage = "base layer seed, first parent layer, second parent layer, whitelist", notes = "Constructs the layer with its seed, parent layers and whitelist.")
	public GenLayerCombineWhitelist(long seed, final GenLayer parent, final GenLayer parent2, final int[] whitelist) {
		super(seed);
		this.parent = parent;
		this.parent2 = parent2;
		this.whitelist = whitelist;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
		int[] parentInts2 = this.parent2.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);

        for (int z = 0; z < depth; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
                
                final int parentIndex = x + 1 + (z + 1) * (width + 2);
            	final int currentBiome = parentInts[parentIndex];
            	final int currentBiome2 = parentInts2[parentIndex];
                
                returnInts[x + z * width] = this.inWhitelist(currentBiome) ? currentBiome : currentBiome2;
            }
        }
		return returnInts;
	}
	
	private boolean inWhitelist(final int id)
	{
		for (final int i : this.whitelist)
		{
			if (id == i)
				return true;
		}
		return false;
	}
}