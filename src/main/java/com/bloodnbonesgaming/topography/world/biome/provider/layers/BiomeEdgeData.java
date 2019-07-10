package com.bloodnbonesgaming.topography.world.biome.provider.layers;

public class BiomeEdgeData {
	
	private int[] biomes = null;
	private int[] whitelist = null;
	private int[] blacklist = null;
	private int edge = 0;
	
	/*
	 * If whitelist && blacklist == null, return true
	 * If whitelist == null && not in blacklist, return true
	 * If whitelist == null && in blacklist, return false
	 * If blacklist == null && in whitelist, return true
	 * If blacklist == null && not in whitelist, return false
	 */
	public boolean useEdge(final int biome)
	{
		if (!this.usedForBiome(biome))
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
	
	public void setBlacklist(final int[] list)
	{
		this.blacklist = list;
	}
	
	public void setWhitelist(final int[] list)
	{
		this.whitelist = list;
	}
	
	public void setBiomes(final int[] biomeIDs)
	{
		this.biomes = biomeIDs;
	}
	
	public void setEdge(final int biomeID)
	{
		this.edge = biomeID;
	}
	
	public int getEdge()
	{
		return this.edge;
	}
}