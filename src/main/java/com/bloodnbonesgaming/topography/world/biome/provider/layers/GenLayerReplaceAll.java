package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerReplaceAll", classExplaination = 
"A layer for replacing all biomes with a single biome.")
public class GenLayerReplaceAll extends GenLayer{
	
	private int replacement;
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with its base seed and parent layer.")
	public GenLayerReplaceAll(long seed, final GenLayer parent) {
		super(seed);
		this.parent = parent;
	}
	
	@ScriptArgs(args = {ArgType.LONG, ArgType.GENLAYER, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "base layer seed, parent layer", notes = "Constructs the layer with its base seed and parent layer and sets the replacement biome.")
	public GenLayerReplaceAll(final long seed, final GenLayer parent, final int replacement)
	{
		this(seed, parent);
		this.replacement = replacement;
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "Sets the biome this layer will replace all biomes with")
	public void setReplacement(final int biome)
	{
		this.replacement = biome;
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
                
                returnInts[x + z * width] = this.replacement;
            }
        }
		return returnInts;
	}
}