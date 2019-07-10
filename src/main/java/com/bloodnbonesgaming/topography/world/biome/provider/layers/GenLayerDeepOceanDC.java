package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerDeepOceanDC", classExplaination = 
"A copy of the vanilla GenLayerDeepOcean with an optional disable. I can't think of a single reason to use this rather than just not adding the layer.")
public class GenLayerDeepOceanDC extends GenLayer
{
	private boolean disableDeepOceanLayer = false;
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with the seed and parent.")
	public GenLayerDeepOceanDC(final long seed, final GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	@ScriptMethodDocumentation(args = "boolean", usage = "", notes = "Disables the layer", defaultValues = "false")
	public void disableDeepOceanLayer()
	{
		this.disableDeepOceanLayer = true;
	}

	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_)
	{
		int i1 = p_75904_1_ - 1;
		int j1 = p_75904_2_ - 1;
		int k1 = p_75904_3_ + 2;
		int l1 = p_75904_4_ + 2;
		int[] aint = this.parent.getInts(i1, j1, k1, l1);
		int[] aint1 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		if (this.disableDeepOceanLayer)
		{
			for (int i2 = 0; i2 < p_75904_4_; ++i2)
			{
				for (int j2 = 0; j2 < p_75904_3_; ++j2)
				{
					aint1[j2 + i2 * p_75904_3_] = aint[j2 + 1 + (i2 + 1) * k1];
				}
			}
		}
		else
		{
			for (int i2 = 0; i2 < p_75904_4_; ++i2)
			{
				for (int j2 = 0; j2 < p_75904_3_; ++j2)
				{
					int k2 = aint[j2 + 1 + (i2 + 1 - 1) * (p_75904_3_ + 2)];
					int l2 = aint[j2 + 1 + 1 + (i2 + 1) * (p_75904_3_ + 2)];
					int i3 = aint[j2 + 1 - 1 + (i2 + 1) * (p_75904_3_ + 2)];
					int j3 = aint[j2 + 1 + (i2 + 1 + 1) * (p_75904_3_ + 2)];
					int k3 = aint[j2 + 1 + (i2 + 1) * k1];
					int l3 = 0;

					if (k2 == 0)
					{
						++l3;
					}

					if (l2 == 0)
					{
						++l3;
					}

					if (i3 == 0)
					{
						++l3;
					}

					if (j3 == 0)
					{
						++l3;
					}

					if (k3 == 0 && l3 > 3)
					{
						aint1[j2 + i2 * p_75904_3_] = Biome.getIdForBiome(Biomes.DEEP_OCEAN);
					}
					else
					{
						aint1[j2 + i2 * p_75904_3_] = k3;
					}
				}
			}
		}
		return aint1;
	}
}