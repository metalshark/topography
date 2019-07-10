package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerRemoveTooMuchOceanDC", classExplaination = 
"A configurable version of the vanilla GenLayerRemoveTooMuchOcean. Removes \"excess\" ocean biomes.")
public class GenLayerRemoveTooMuchOceanDC extends GenLayer
{
	private EnumOceanSize size;
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with its base seed and parent layer.")
	public GenLayerRemoveTooMuchOceanDC(final long seed, final GenLayer parent)
	{
		super(seed);
		
		this.parent = parent;
	}
	
	@ScriptMethodDocumentation(args = "String", usage = "ocean size", notes = "Sets the ocean size for the layer. Options are \"Small\", \"Large\", \"Default\"."
			+ " Small replaces all ocean with plains, large does nothing, default does vanilla removal behavior.")
	public void setOceanSize(final String oceanSize)
	{
		final EnumOceanSize size = EnumOceanSize.valueOf(oceanSize.toUpperCase());
		
		if (size != null)
		{
			this.size = size;
		}
		else
		{
			Topography.instance.getLog().error(oceanSize + " is not a valid ocean size.");
		}
	}
	
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
    {
        int i1 = chunkX - 1;
        int j1 = chunkZ - 1;
        int k1 = width + 2;
        int l1 = depth + 2;
        int[] aint = this.parent.getInts(i1, j1, k1, l1);
        int[] aint1 = IntCache.getIntCache(width * depth);

        for (int z = 0; z < depth; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                int currentBiome = aint[x + 1 + (z + 1) * k1];
                
                switch (this.size)
                {
                	case SMALL: {
                		aint1[x + z * width] = 1;
                		break;
                	}
                	case LARGE: {
                		aint1[x + z * width] = currentBiome;
                		break;
                	}
                	default: {
                		int biome1 = aint[x + 1 + (z + 1 - 1) * (width + 2)];
                        int biome2 = aint[x + 1 + 1 + (z + 1) * (width + 2)];
                        int biome3 = aint[x + 1 - 1 + (z + 1) * (width + 2)];
                        int biome4 = aint[x + 1 + (z + 1 + 1) * (width + 2)];
                        this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
                		
                		if (currentBiome == 0 && biome1 == 0 && biome2 == 0 && biome3 == 0 && biome4 == 0 && this.nextInt(2) == 0)
                        {
                            aint1[x + z * width] = 1;
                        }
                		else
                		{
                            aint1[x + z * width] = currentBiome;
                		}
                		break;
                	}
                }
            }
        }

        return aint1;
    }
}