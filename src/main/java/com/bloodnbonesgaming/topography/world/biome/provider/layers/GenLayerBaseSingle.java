package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerBaseSingle", classExplaination = 
"A layer to be used as a base to fill the world with a single biome. Good for when you want a single base biome, such as in the Jungle_Islands preset example.")
public class GenLayerBaseSingle extends GenLayer{
	
	private int biome;

	@ScriptMethodDocumentation(args = "long", usage = "layer base seed", notes = "Constructs the layer with the single biome set to 0/\"Ocean\".")
	public GenLayerBaseSingle(long seed) {
		super(seed);
	}
	
	@ScriptArgs(args = {ArgType.LONG, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "layer base seed, biome ID", notes = "Constructs the layer and sets the biome it will fill the world with.")
	public GenLayerBaseSingle(final long seed, final int biome)
	{
		this(seed);
		this.biome = biome;
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome ID", notes = "Sets the biome this layer will fill the world with.")
	public void setBiome(final int biome)
	{
		this.biome = biome;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
        int[] returnInts = IntCache.getIntCache(width * depth);

        for (int i = 0; i < returnInts.length; i++)
        {
        	returnInts[i] = this.biome;
        }
		return returnInts;
	}
}