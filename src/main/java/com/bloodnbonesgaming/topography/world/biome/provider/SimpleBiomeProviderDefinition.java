package com.bloodnbonesgaming.topography.world.biome.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.BiomeHelper;

import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class SimpleBiomeProviderDefinition {
	
	private final List<BiomeEntry> biomes = new ArrayList<BiomeEntry>();
	private final List<BiomeEntry> oceanBiomes = new ArrayList<BiomeEntry>();
	private final Map<Integer, List<Integer>> hills = new HashMap<Integer, List<Integer>>();
	private final Map<Integer, Integer> rivers = new HashMap<Integer, Integer>();
	
	public SimpleBiomeProvider buildBiomeProvider(final WorldInfo info)
	{
		return new SimpleBiomeProvider(info.getSeed(), info.getTerrainType(), info.getGeneratorOptions(), this.biomes, this.oceanBiomes, this.hills, this.rivers);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT})
	public void addBiomes(final int[] biomes)
	{
		this.addBiomes(biomes, 10);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT})
	public void addBiomes(final int[] biomes, final int weight)
	{
		for (final int biome : biomes)
		{
			this.biomes.add(BiomeHelper.generateBiomeEntry(biome, weight));
		}
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT})
	public void addOceanBiomes(final int[] biomes)
	{
		this.addOceanBiomes(biomes, 10);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.INT})
	public void addOceanBiomes(final int[] biomes, final int weight)
	{
		for (final int biome : biomes)
		{
			this.oceanBiomes.add(BiomeHelper.generateBiomeEntry(biome, weight));
		}
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.NON_NULL_BIOME_ID_ARRAY})
	public void addHills(final int[] biomes, final int[] hills)
	{
		List<Integer> list = Arrays.stream(hills).boxed().collect(Collectors.toList());
		
		for (final int biome : biomes)
		{
			if (this.hills.containsKey(biome))
			{
				this.hills.get(biome).addAll(list);
			}
			else
			{
				this.hills.put(biome, list);
			}
		}
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.NON_NULL_BIOME_ID})
	public void setRiver(final int[] biomes, final int river)
	{		
		for (final int biome : biomes)
		{
			this.rivers.put(biome, river);
		}
	}
}
