package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StalactiteFormationConfig extends RegionFeatureConfig {

	public static final Codec<StalactiteFormationConfig> CODEC = RecordCodecBuilder.create((builder) -> {
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
		}), Codec.INT.fieldOf("size_reduction_chance").forGetter((config) -> {
			return config.sizeReductionChance;
		})).apply(builder, StalactiteFormationConfig::new);
	});

	public final int sizeReductionChance;

	public StalactiteFormationConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset, int radius, int sizeReductionChance) {
		super(regionSize, minSpacing, positionAttemptCount, seedOffset, radius);
		this.sizeReductionChance = sizeReductionChance;
	}
}
