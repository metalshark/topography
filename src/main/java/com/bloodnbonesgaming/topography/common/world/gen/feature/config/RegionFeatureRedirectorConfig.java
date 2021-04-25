package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RegionFeatureRedirectorConfig implements IFeatureConfig {

	public static final Codec<RegionFeatureRedirectorConfig> CODEC = RecordCodecBuilder.create((builder) -> {
		return builder.group(Codec.INT.fieldOf("stage").forGetter((config) -> {
			return config.stage.ordinal();
		})).apply(builder, RegionFeatureRedirectorConfig::new);
	});
	
	public final GenerationStage.Decoration stage;

	public RegionFeatureRedirectorConfig(GenerationStage.Decoration stage) {
		this.stage = stage;
	}

	public RegionFeatureRedirectorConfig(int stage) {
		this.stage = GenerationStage.Decoration.values()[stage];
	}
}
