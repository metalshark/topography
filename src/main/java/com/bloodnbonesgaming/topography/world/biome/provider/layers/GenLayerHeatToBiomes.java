package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerHeatToBiomes", classExplaination = 
"A more configurable version of GenLayerBiomeDC. Replaces the heat map with a pseudo-random weighted biome map."
+ " For the vanilla heat map, normally 1 is desert, 2 is warm, 3 is cool, 4 is icy.")
public class GenLayerHeatToBiomes extends GenLayer
{
	private List<BiomeData> biomeDataList = new ArrayList<BiomeData>();
	private int specialVariantChance = 3;
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent", notes = "Constructs the layer with the seed and parent.")
	public GenLayerHeatToBiomes(long seed, final GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	private BiomeData getBiomeData(final int id)
	{
		while (this.biomeDataList.size() <= id)
		{
			final BiomeData data = new BiomeData();
			data.setSpecialVariantChance(this.specialVariantChance);
			
			this.biomeDataList.add(data);
		}
		
		return this.biomeDataList.get(id);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "heat ID, biome ID", notes = "Adds the provided biome id to the provided heat id with a weight of 1. What heat ids are available depends on the GenLayer used before this")
	public void addBiome(final int heat, final int id)
	{
		this.addBiome(heat, id, 1);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID, ArgType.INT})
	@ScriptMethodDocumentation(usage = "heat ID, biome ID, weight", notes = "Adds the provided biome id to the provided heat id with the provided weight. What heat ids are available depends on the GenLayer used before this.")
	public void addBiome(final int heat, final int id, final int weight)
	{
		final BiomeData data = this.getBiomeData(heat);
		final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
		
		if (entry != null)
		{
			data.addBiomeEntry(entry);
		}
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "chance", notes = "Sets the global chance for the special variant biome to be used. This will set the chance for all heat ids. Set this before you set specific ones or it will overwrite. The chance is 1 in the provided number.")
	public void setSpecialVariantChance(final int chance)
	{
		this.specialVariantChance = chance;
		
		for (final BiomeData data : this.biomeDataList)
		{
			data.setSpecialVariantChance(chance);
		}
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.INT})
	@ScriptMethodDocumentation(usage = "heat ID, chance", notes = "Sets the chance for the special variant biome to be used for the provided heat id. The chance is 1 in the provided number. What heat ids are available depends on the GenLayer used before this.")
	public void setSpecialVariantChance(final int heat, final int chance)
	{
		this.getBiomeData(heat).setSpecialVariantChance(chance);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "heat ID, biome ID", notes = "Sets the special biome for the provided heat id. What heat ids are available depends on the GenLayer used before this.")
	public void setSpecialBiome(final int heat, final int biome)
	{
		this.getBiomeData(heat).setSpecial(biome);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "heat ID, biome ID", notes = "Sets the special variant biome for the provided heat id. What heat ids are available depends on the GenLayer used before this.")
	public void setSpecialVariantBiome(final int heat, final int biome)
	{
		this.getBiomeData(heat).setSpecialVariant(biome);
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		final int arrayLength = width * depth;
		final int[] parentInts = parent.getInts(chunkX, chunkZ, width, depth);
		final int[] returnInts = IntCache.getIntCache(arrayLength);
		
		for (int z = 0; z < depth; z++)
		{
			for (int x = 0; x < width; x++)
			{
				this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
				final int index = x + z * width;
				final int currentBiome = parentInts[index];
				final boolean useSpecialBiome = (currentBiome & 3840) >> 8 > 0;
				
				if (currentBiome < this.biomeDataList.size())
				{
				    final BiomeData data = this.biomeDataList.get(currentBiome);
	                
	                if (data != null)
	                {
	                    if (useSpecialBiome)
	                    {
	                        if (data.getSpecialVariant() != null)
	                        {
	                            if (this.nextInt(data.getSpecialVariantChance()) == 0)
	                            {
	                                returnInts[index] = data.getSpecialVariant();
	                                continue;
	                            }
	                        }
	                        if (data.getSpecial() != null)
	                        {
	                            returnInts[index] = data.getSpecial();
	                            continue;
	                        }
	                    }
	                    if (data.getBiomes().size() > 0)
	                    {
	                        returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(data.getBiomes()).biome);
	                        continue;
	                    }
	                    returnInts[index] = 0;
	                }
				}
				else
				{
				    returnInts[index] = parentInts[index];
				}
			}
		}
		return returnInts;
	}
	
	protected net.minecraftforge.common.BiomeManager.BiomeEntry getWeightedBiomeEntry(final List<BiomeEntry> biomeList)
    {
		boolean modded = false;
        //java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = net.minecraft.util.WeightedRandom.getTotalWeight(biomeList);
        int weight = modded?nextInt(totalWeight):nextInt(totalWeight / 10) * 10;
        return (net.minecraftforge.common.BiomeManager.BiomeEntry)net.minecraft.util.WeightedRandom.getRandomItem(biomeList, weight);
    }
}