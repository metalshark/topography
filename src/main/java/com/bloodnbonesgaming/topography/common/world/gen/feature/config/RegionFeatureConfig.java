package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RegionFeatureConfig implements IFeatureConfig {

	public static final Codec<RegionFeatureConfig> codec = RecordCodecBuilder.create((builder) -> {
		return builder.group(Codec.INT.fieldOf("region_size").forGetter((config) -> {
	    	return config.regionSize;
		}), Codec.INT.fieldOf("min_spacing").forGetter((config) -> {
			return config.minSpacing;
		}), Codec.INT.fieldOf("position_attempt_count").forGetter((config) -> {
			return config.positionAttemptCount;
		}), Codec.INT.fieldOf("seed_offset").forGetter((config) -> {
			return config.featureCountSeedOffset;
		}), Codec.INT.fieldOf("radius").forGetter((config) -> {
			return config.radius;
		})).apply(builder, RegionFeatureConfig::new);
	});

	public final int regionSize;
	public final int minSpacing;
	public final int positionAttemptCount;
	public final int featureCountSeedOffset;
	public final int radius;
	
	
	
	public RegionFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset, int radius) {
		this.regionSize = regionSize * 16;
		this.minSpacing = minSpacing;
		this.positionAttemptCount = positionAttemptCount;
		this.featureCountSeedOffset = seedOffset;
		this.radius = radius;
	}
}
