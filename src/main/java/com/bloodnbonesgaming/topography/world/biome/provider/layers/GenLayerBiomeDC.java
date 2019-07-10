package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerBiomeDC", classExplaination = 
"A configurable version of the vanilla GenLayerBiome. Replaces the heat map with a pseudo-random weighted biome map.")
public class GenLayerBiomeDC extends GenLayer
{
	private List<BiomeEntry> ocean = new ArrayList<BiomeEntry>();
	private List<BiomeEntry> desert = new ArrayList<BiomeEntry>();
	private List<BiomeEntry> warm = new ArrayList<BiomeEntry>();
	private List<BiomeEntry> cool = new ArrayList<BiomeEntry>();
	private List<BiomeEntry> icy = new ArrayList<BiomeEntry>();
	private Integer specialOcean;
	private Integer specialVariantOcean;
	private int specialVariantOceanChance = 3;
	private Integer specialDesert;
	private Integer specialVariantDesert;
	private int specialVariantDesertChance = 3;
	private Integer specialWarm;
	private Integer specialVariantWarm;
	private int specialVariantWarmChance = 3;
	private Integer specialCool;
	private Integer specialVariantCool;
	private int specialVariantCoolChance = 3;
	private Integer specialIcy;
	private Integer specialVariantIcy;
	private int specialVariantIcyChance = 3;
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with its seed and parent layer.")
	public GenLayerBiomeDC(long seed, final GenLayer parent)
	{
		super(seed);
		this.parent = parent;
	}
	
	@ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome type, biome ID", notes = "Adds the provided biome id to the provided type with a weight of 1. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\"")
	public void addBiome(final String type, final int id)
	{
		this.addBiome(type, id, 1);
	}
	
	@ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID, ArgType.INT})
	@ScriptMethodDocumentation(usage = "biome type, biome ID, weight", notes = "Adds the provided biome id to the provided type with the provided weight. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\".")
	public void addBiome(final String type, final int id, final int weight)
	{
		if (type.equalsIgnoreCase("Ocean"))
		{
			final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
			
			if (entry != null)
			{
				this.ocean.add(entry);
			}
		}
		else if (type.equalsIgnoreCase("Desert"))
		{
			final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
			
			if (entry != null)
			{
				this.desert.add(entry);
			}
		}
		else if (type.equalsIgnoreCase("Warm"))
		{
			final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
			
			if (entry != null)
			{
				this.warm.add(entry);
			}
		}
		else if (type.equalsIgnoreCase("Cool"))
		{
			final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
			
			if (entry != null)
			{
				this.cool.add(entry);
			}
		}
		else if (type.equalsIgnoreCase("Icy"))
		{
			final BiomeEntry entry = BiomeHelper.generateBiomeEntry(id, weight);
			
			if (entry != null)
			{
				this.icy.add(entry);
			}
		}
		else
		{
			Topography.instance.getLog().error(type + " is not a biome type!");
		}
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "chance", notes = "Sets the global chance for the special variant biome to be used. This will set the chance for all types. Set this before you set specific ones or it will overwrite. The chance is 1 in the provided number.")
	public void setSpecialVariantChance(final int chance)
	{
		this.specialVariantOceanChance = chance;
		this.specialVariantDesertChance = chance;
		this.specialVariantWarmChance = chance;
		this.specialVariantCoolChance = chance;
		this.specialVariantIcyChance = chance;
	}
	
	@ScriptMethodDocumentation(args = "String, int", usage = "biome type, chance", notes = "Sets the chance for the special variant biome to be used for the provided type. The chance is 1 in the provided number. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\".")
	public void setSpecialVariantChance(final String type, final int chance)
	{
		if (type.equalsIgnoreCase("Ocean"))
		{
			this.specialVariantOceanChance = chance;
		}
		else if (type.equalsIgnoreCase("Desert"))
		{
			this.specialVariantDesertChance = chance;
		}
		else if (type.equalsIgnoreCase("Warm"))
		{
			this.specialVariantWarmChance = chance;
		}
		else if (type.equalsIgnoreCase("Cool"))
		{
			this.specialVariantCoolChance = chance;
		}
		else if (type.equalsIgnoreCase("Icy"))
		{
			this.specialVariantIcyChance = chance;
		}
		else
		{
			Topography.instance.getLog().error(type + " is not a biome type!");
		}
	}
	
	@ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome type, biome ID", notes = "Sets the special biome for the provided type. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\".")
	public void setSpecialBiome(final String type, final int biome)
	{
		if (type.equalsIgnoreCase("Ocean"))
		{
			this.specialOcean = biome;
		}
		else if (type.equalsIgnoreCase("Desert"))
		{
			this.specialDesert = biome;
		}
		else if (type.equalsIgnoreCase("Warm"))
		{
			this.specialWarm = biome;
		}
		else if (type.equalsIgnoreCase("Cool"))
		{
			this.specialCool = biome;
		}
		else if (type.equalsIgnoreCase("Icy"))
		{
			this.specialIcy = biome;
		}
		else
		{
			Topography.instance.getLog().error(type + " is not a biome type!");
		}
	}
	
	@ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome type, biome ID", notes = "Sets the special variant biome for the provided type. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\".")
	public void setSpecialVariantBiome(final String type, final int biome)
	{
		if (type.equalsIgnoreCase("Ocean"))
		{
			this.specialVariantOcean = biome;
		}
		else if (type.equalsIgnoreCase("Desert"))
		{
			this.specialVariantDesert = biome;
		}
		else if (type.equalsIgnoreCase("Warm"))
		{
			this.specialVariantWarm = biome;
		}
		else if (type.equalsIgnoreCase("Cool"))
		{
			this.specialVariantCool = biome;
		}
		else if (type.equalsIgnoreCase("Icy"))
		{
			this.specialVariantIcy = biome;
		}
		else
		{
			Topography.instance.getLog().error(type + " is not a biome type!");
		}
	}
	
	@ScriptArgs(args = {ArgType.STRING})
	@ScriptMethodDocumentation(usage = "biome type", notes = "Fills the BiomeEntry list for the provided type with the entries it would contain in vanilla. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\".")
	public void fillWithVanillaBiomes(final String type)
	{
		if (type.equalsIgnoreCase("Ocean"))
		{
			
		}
		else if (type.equalsIgnoreCase("Desert"))
		{
			List<BiomeEntry> biomes = net.minecraftforge.common.BiomeManager.getBiomes(BiomeType.DESERT);
			this.desert.addAll(biomes);
			this.desert.add(new net.minecraftforge.common.BiomeManager.BiomeEntry(Biomes.DESERT, 30));
			this.desert.add(new net.minecraftforge.common.BiomeManager.BiomeEntry(Biomes.SAVANNA, 20));
			this.desert.add(new net.minecraftforge.common.BiomeManager.BiomeEntry(Biomes.PLAINS, 10));
		}
		else if (type.equalsIgnoreCase("Warm"))
		{
			List<BiomeEntry> biomes = net.minecraftforge.common.BiomeManager.getBiomes(BiomeType.WARM);
			this.warm.addAll(biomes);
		}
		else if (type.equalsIgnoreCase("Cool"))
		{
			List<BiomeEntry> biomes = net.minecraftforge.common.BiomeManager.getBiomes(BiomeType.COOL);
			this.cool.addAll(biomes);
		}
		else if (type.equalsIgnoreCase("Icy"))
		{
			List<BiomeEntry> biomes = net.minecraftforge.common.BiomeManager.getBiomes(BiomeType.ICY);
			this.icy.addAll(biomes);
		}
		else
		{
			Topography.instance.getLog().error(type + " is not a biome type!");
		}
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Fills all BiomeEntry lists with the entries they would contain in vanilla.")
	public void fillWithVanillaBiomes()
	{
		this.fillWithVanillaBiomes("Desert");
		this.fillWithVanillaBiomes("Warm");
		this.fillWithVanillaBiomes("Cool");
		this.fillWithVanillaBiomes("Icy");
		this.fillWithVanillaBiomes("Ocean");
	}
	
	@ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome type, biome ID", notes = "Removes any entries with the provided biome entry from the provided type. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\"")
    public void removeEntriesForBiome(final String type, final int biome)
    {
	    if (type.equalsIgnoreCase("Ocean"))
        {
	        final Biome biomeObj = Biome.getBiomeForId(biome);

	        final Iterator<BiomeEntry> iterator = this.ocean.iterator();
	        
	        while (iterator.hasNext())
	        {	            
	            if (iterator.next().biome == biomeObj)
                {
	                iterator.remove();
                }
	        }
        }
        else if (type.equalsIgnoreCase("Desert"))
        {
            final Biome biomeObj = Biome.getBiomeForId(biome);

            final Iterator<BiomeEntry> iterator = this.desert.iterator();
            
            while (iterator.hasNext())
            {               
                if (iterator.next().biome == biomeObj)
                {
                    iterator.remove();
                }
            }
        }
        else if (type.equalsIgnoreCase("Warm"))
        {
            final Biome biomeObj = Biome.getBiomeForId(biome);

            final Iterator<BiomeEntry> iterator = this.warm.iterator();
            
            while (iterator.hasNext())
            {               
                if (iterator.next().biome == biomeObj)
                {
                    iterator.remove();
                }
            }
        }
        else if (type.equalsIgnoreCase("Cool"))
        {
            final Biome biomeObj = Biome.getBiomeForId(biome);

            final Iterator<BiomeEntry> iterator = this.cool.iterator();
            
            while (iterator.hasNext())
            {               
                if (iterator.next().biome == biomeObj)
                {
                    iterator.remove();
                }
            }
        }
        else if (type.equalsIgnoreCase("Icy"))
        {
            final Biome biomeObj = Biome.getBiomeForId(biome);

            final Iterator<BiomeEntry> iterator = this.icy.iterator();
            
            while (iterator.hasNext())
            {               
                if (iterator.next().biome == biomeObj)
                {
                    iterator.remove();
                }
            }
        }
        else
        {
            Topography.instance.getLog().error(type + " is not a biome type!");
        }
    }
    
    @ScriptArgs(args = {ArgType.STRING, ArgType.NON_NULL_BIOME_ID})
    @ScriptMethodDocumentation(usage = "biome type, biome ID", notes = "Removes any entries with the provided biome entry from the provided type. Type options are \"Ocean\", \"Desert\", \"Warm\", \"Cool\", \"Icy\"")
    public void removeEntriesForBiome(final int biome)
    {
        this.removeEntriesForBiome("Desert", biome);
        this.removeEntriesForBiome("Warm", biome);
        this.removeEntriesForBiome("Cool", biome);
        this.removeEntriesForBiome("Icy", biome);
        this.removeEntriesForBiome("Ocean", biome);
    }

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		final int arrayLength = width * depth;
		final int[] parentInts;
		final int[] returnInts = IntCache.getIntCache(arrayLength);
		
		if (parent != null)
		{
			parentInts = parent.getInts(chunkX, chunkZ, width, depth);
		}
		else
		{
			parentInts = IntCache.getIntCache(arrayLength);
			
			for (int i = 0; i < parentInts.length; i++)
			{
				parentInts[i] = 0;
			}
		}
		
		for (int z = 0; z < depth; z++)
		{
			for (int x = 0; x < width; x++)
			{
				this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
				final int index = x + z * width;
				final int currentBiome = parentInts[index];
				final boolean useSpecialBiome = (currentBiome & 3840) >> 8 > 0;
				int warmth = currentBiome & -3841;
				
				switch (warmth)
				{
					case 0:
					{
						if (useSpecialBiome)
						{
							if (this.specialVariantOcean != null)
							{
								if (this.nextInt(this.specialVariantOceanChance) == 0)
								{
									returnInts[index] = this.specialVariantOcean;
									break;
								}
							}
							if (this.specialOcean != null)
							{
								returnInts[index] = this.specialOcean;
								break;
							}
						}
						if (this.ocean.size() > 0)
						{
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(ocean).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					case 1:
					{
						if (useSpecialBiome)
						{
							if (this.specialVariantDesert != null)
							{
								if (this.nextInt(this.specialVariantDesertChance) == 0)
								{
									returnInts[index] = this.specialVariantDesert;
									break;
								}
							}
							if (this.specialDesert != null)
							{
								returnInts[index] = this.specialDesert;
								break;
							}
						}
						if (this.desert.size() > 0)
						{
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(desert).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					case 2:
					{
						if (useSpecialBiome)
						{
							if (this.specialVariantWarm != null)
							{
								if (this.nextInt(this.specialVariantWarmChance) == 0)
								{
									returnInts[index] = this.specialVariantWarm;
									break;
								}
							}
							if (this.specialWarm != null)
							{
								returnInts[index] = this.specialWarm;
								break;
							}
						}
						if (this.warm.size() > 0)
						{
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(warm).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					case 3:
					{
						if (useSpecialBiome)
						{
							if (this.specialVariantCool != null)
							{
								if (this.nextInt(this.specialVariantCoolChance) == 0)
								{
									returnInts[index] = this.specialVariantCool;
									break;
								}
							}
							if (this.specialCool != null)
							{
								returnInts[index] = this.specialCool;
								break;
							}
						}
						if (this.cool.size() > 0)
						{
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(cool).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					case 4:
					{
						if (useSpecialBiome)
						{
							if (this.specialVariantIcy != null)
							{
								if (this.nextInt(this.specialVariantIcyChance) == 0)
								{
									returnInts[index] = this.specialVariantIcy;
									break;
								}
							}
							if (this.specialIcy != null)
							{
								returnInts[index] = this.specialIcy;
								break;
							}
						}
						if (this.icy.size() > 0)
						{
							returnInts[index] = Biome.getIdForBiome(this.getWeightedBiomeEntry(icy).biome);
							break;
						}
						returnInts[index] = 0;
						break;
					}
					default:
					{
						returnInts[index] = parentInts[index];
						break;
					}
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
    
    /*protected BiomeEntry getWeightedBiomeEntry(final List<BiomeEntry> biomeList)
    {
        int totalWeight = WeightedRandom.getTotalWeight(biomeList);
        int weight = nextInt(totalWeight);
        return (BiomeEntry)WeightedRandom.getRandomItem(biomeList, nextInt(totalWeight));
    }*/
}