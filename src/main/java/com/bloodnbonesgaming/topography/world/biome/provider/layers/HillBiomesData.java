package com.bloodnbonesgaming.topography.world.biome.provider.layers;

public class HillBiomesData
{
	private Integer hillBiome = null;
	private Integer variantBiome = null;
	private Integer variantHillBiome = null;
	private Double hillBiomeChance = null;
	private Double variantBiomeChance = null;
	private Double variantHillBiomeChance = null;
	
	/*
	 * For Scripting
	 */
	public void setData(final Integer hillBiome, final Integer variantBiome, final Integer variantHillBiome, final Double hillBiomeChance, final Double variantBiomeChance, final Double variantHillBiomeChance)
	{
		this.hillBiome = hillBiome;
		this.variantBiome = variantBiome;
		this.variantHillBiome = variantHillBiome;
		this.hillBiomeChance = hillBiomeChance;
		this.variantBiomeChance = variantBiomeChance;
		this.variantHillBiomeChance = variantHillBiomeChance;
	}
	
	/*
	 * For Scripting
	 */
	public void setHillBiome(Integer hillBiome) {
		this.hillBiome = hillBiome;
	}

	/*
	 * For Scripting
	 */
	public void setVariantBiome(Integer variantBiome) {
		this.variantBiome = variantBiome;
	}

	/*
	 * For Scripting
	 */
	public void setVariantHillBiome(Integer variantHillBiome) {
		this.variantHillBiome = variantHillBiome;
	}

	/*
	 * For Scripting
	 */
	public void setHillBiomeChance(Double hillBiomeChance) {
		this.hillBiomeChance = hillBiomeChance;
	}

	/*
	 * For Scripting
	 */
	public void setVariantBiomeChance(Double variantBiomeChance) {
			this.variantBiomeChance = variantBiomeChance;
	}

	/*
	 * For Scripting
	 */
	public void setVariantHillBiomeChance(Double variantHillBiomeChance) {
		this.variantHillBiomeChance = variantHillBiomeChance;
	}

	public Integer getHillBiome()
	{
		return this.hillBiome;
	}
	
	public Integer getVariantBiome()
	{
		return this.variantBiome;
	}
	
	public Integer getVariantHillBiome()
	{
		return this.variantHillBiome;
	}
	
	public Double getHillBiomeChance()
	{
		return this.hillBiomeChance;
	}
	
	public Double getVariantBiomeChance()
	{
		return this.variantBiomeChance;
	}
	
	public Double getVariantHillBiomeChance()
	{
		return this.variantHillBiomeChance;
	}
}