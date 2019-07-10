package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerHeatZ", classExplaination = 
		"This GenLayer is used to create a repeating set of ids on the Z axis based upon an array."
		+ " This is mainly used to create a semi-realistic wrapping heat map.")
public class GenLayerHeatZ extends GenLayer{

	private int[] heatArray;
	
	@ScriptMethodDocumentation(args = "long", usage = "seed", notes = "Constructors the layer, setting the seed to be used.")
	public GenLayerHeatZ(long seed) {
		super(seed);
	}
	
	@ScriptMethodDocumentation(usage = "seed, heat array", notes = "Constructors the layer, setting the seed and heat array to be used.")
	@ScriptArgs(args = {ArgType.LONG, ArgType.NON_NULL_BIOME_ID_ARRAY})
	public GenLayerHeatZ(final long seed, final int[] heatArray)
	{
		this(seed);
		this.heatArray = heatArray;
	}
	
	@ScriptMethodDocumentation(usage = "heat array", notes = "Sets the heat array the layer will use.")
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY})
	public void setHeatArray(final int[] heatArray)
	{
		this.heatArray = heatArray;
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int height)
	{
		int[] returnInts = IntCache.getIntCache(width * height);

		boolean negative;
		
        for (int z = 0; z < height; ++z)
        {
        	negative = z + chunkZ < 0;
        	
            for (int x = 0; x < width; ++x)
            {
                this.initChunkSeed((long)(chunkX + x), (long)(chunkZ + z));
                returnInts[x + z * width] = this.heatArray[negative ? heatArray.length * 2 - (heatArray.length + (Math.abs(chunkZ + z + 1) % this.heatArray.length)) - 1 : Math.abs(chunkZ + z + this.heatArray.length) % this.heatArray.length];
            }
        }
        return returnInts;
	}
}