package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.common.world.gen.feature.RegionFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RegionFeatureConfig implements IFeatureConfig {

	public static final Codec<RegionFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
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
	
	public final Random regionPositionRand = new Random();
	
	
	
	public RegionFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset, int radius) {
		this.regionSize = regionSize * 16;
		this.minSpacing = minSpacing;
		this.positionAttemptCount = positionAttemptCount;
		this.featureCountSeedOffset = seedOffset;
		this.radius = radius;
	}
	
	public List<BlockPos> generatePositionsForRegion(RegionFeature requestingFeature, ISeedReader region, int chunkX, int chunkZ, int regionX, int regionZ, int radius) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		Biome biome = region.getWorld().getNoiseBiome((chunkX << 2) + 2, 2, (chunkZ << 2) + 2);//May need to change to getNoiseBiomeRaw if it doesn't function properly
		
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();
		List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>> regionFeatures = new ArrayList<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>();
		
		//Get all region features
		for (List<Supplier<ConfiguredFeature<?, ?>>> innerList : features) {//Each list is a different GenerationStage.Decoration. May want to only block if on the same stage
			for (Supplier<ConfiguredFeature<?, ?>> supplier : innerList) {
				ConfiguredFeature<?, ?> feature = supplier.get();
				
				if (feature.feature instanceof RegionFeature) {
					regionFeatures.add((ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>) feature);
				}
			}
		}
		
		return positions;
	}
}
