package com.bloodnbonesgaming.topography.common.util;

import java.util.OptionalLong;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.ColumnFuzzedBiomeMagnifier;

public class DimensionTypeBuilder {
	
	private ResourceLocation effects = new ResourceLocation("overworld");
	private OptionalLong fixedTime = OptionalLong.empty();
	
	public DimensionTypeBuilder fixedTime(long time) {
		this.fixedTime = OptionalLong.of(time);
		return this;
	}
	
	public DimensionTypeBuilder effects(String location) {
		effects = new ResourceLocation(location);
		return this;
	}
	
	public DimensionType build() {
		return new DimensionType(fixedTime, true, false, false, true, 1.0D, false, false, true, false, true, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), this.effects, 0.0F);
	}
}
