package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class BiomeData {
	
	private List<BiomeEntry> biomes = new ArrayList<BiomeEntry>();
	private Integer special;
	private Integer specialVariant;
	private int specialVariantChance = 3;
	
	public List<BiomeEntry> getBiomes() {
		return biomes;
	}
	
	public void addBiomeEntry(final BiomeEntry entry)
	{
		this.biomes.add(entry);
	}
	public void setBiomes(List<BiomeEntry> biomes) {
		this.biomes = biomes;
	}
	public Integer getSpecial() {
		return special;
	}
	public void setSpecial(Integer special) {
		this.special = special;
	}
	public Integer getSpecialVariant() {
		return specialVariant;
	}
	public void setSpecialVariant(Integer specialVariant) {
		this.specialVariant = specialVariant;
	}
	public int getSpecialVariantChance() {
		return specialVariantChance;
	}
	public void setSpecialVariantChance(int specialVariantChance) {
		this.specialVariantChance = specialVariantChance;
	}
}