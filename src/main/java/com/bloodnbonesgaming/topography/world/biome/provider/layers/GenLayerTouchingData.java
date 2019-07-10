package com.bloodnbonesgaming.topography.world.biome.provider.layers;

public class GenLayerTouchingData {
	
	private final int[] biomes;
	private final int[] whitelist;
	private final int[] blacklist;
	private final int requiredCount;
	private final int replacement;
	private final int chance;
	
	public GenLayerTouchingData(final int[] biomes, final int[] whitelist, final int[] blacklist, final int requiredCount, final int replacement, final int chance)
	{
		this.biomes = biomes;
		this.whitelist = whitelist;
		this.blacklist = blacklist;
		this.requiredCount = requiredCount;
		this.chance = chance;
		this.replacement = replacement;
	}
	
	public boolean replace(final int biome)
	{
		if (this.whitelist == null && this.blacklist == null)
		{
			return true;
		}
		else if (whitelist == null)
		{
			if (!this.inBlacklist(biome))
			{
				return true;
			}
		}
		else if (blacklist == null)
		{
			if (this.inWhitelist(biome))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean inWhitelist(final int biome)
	{
		if (this.whitelist != null)
		{
			for (final int biomeID : this.whitelist)
			{
				if (biomeID == biome)
					return true;
			}
		}
		return false;
	}
	
	private boolean inBlacklist(final int biome)
	{
		if (this.blacklist != null)
		{
			for (final int biomeID : this.blacklist)
			{
				if (biomeID == biome)
					return true;
			}
		}
		return false;
	}
	
	public boolean usedForBiome(final int biome)
	{
		if (this.biomes != null)
		{
			for (final int biomeID : this.biomes)
			{
				if (biomeID == biome)
					return true;
			}
		}
		return false;
	}

	public int getRequiredCount() {
		return this.requiredCount;
	}

	public int getReplacement() {
		return this.replacement;
	}
	
	public int getChance()
	{
		return this.chance;
	}
}