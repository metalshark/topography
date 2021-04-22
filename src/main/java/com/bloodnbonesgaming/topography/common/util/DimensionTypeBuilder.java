package com.bloodnbonesgaming.topography.common.util;

import java.util.OptionalLong;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.ColumnFuzzedBiomeMagnifier;

public class DimensionTypeBuilder {
	
	private ResourceLocation effects = new ResourceLocation("overworld");
	private OptionalLong fixedTime = OptionalLong.empty();
	private boolean hasSkylight = true;
	private boolean hasCeiling = false;
	private float[] ambientWorldLight = null;
	
	public DimensionTypeBuilder fixedTime(long time) {
		this.fixedTime = OptionalLong.of(time);
		return this;
	}
	
	public DimensionTypeBuilder effects(String location) {
		effects = new ResourceLocation(location);
		return this;
	}
	
	public DimensionTypeBuilder skylight(boolean bool) {
		this.hasSkylight = bool;
		return this;
	}
	
	//Stops thunder, changes how spawn location locating works, changes map filling range?
	public DimensionTypeBuilder ceiling(boolean bool) {
		this.hasCeiling = bool;
		return this;
	}
	
	public DimensionTypeBuilder lightBrightness(float[] brightness) {
		this.ambientWorldLight = brightness;
		return this;
	}
	
	public DimensionType build() {
		DimensionType type = new DimensionType(fixedTime, hasSkylight, hasCeiling, false, true, 1.0D, false, false, true, false, true, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), this.effects, 0.0F);
		if (ambientWorldLight != null) {
			type.ambientWorldLight = ambientWorldLight;
		}
		return type;
	}
}
