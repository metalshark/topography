package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import com.bloodnbonesgaming.topography.common.util.IOHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.feature.template.Template;

public class StructureFeatureConfig extends RegionFeatureConfig {

	public static final Codec<StructureFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
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
		}), Codec.STRING.fieldOf("structure_path").forGetter((config) -> {
			return config.path;
		})).apply(builder, StructureFeatureConfig::new);
	});

	public StructureFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset, int radius, String path) {
		super(regionSize, minSpacing, positionAttemptCount, seedOffset, radius);
		this.path = path;
	}

	public final String path;
	private Template structure = null;
	
	public Template getTemplate() {
		if (structure == null) {
			structure = IOHelper.loadStructureTemplate(path);
		}
		return structure;
	}
}
