package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;

public class CircleRegionFeatureConfig extends RegionFeatureConfig implements ICircleConfig {

	public static final Codec<CircleRegionFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
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
		})).apply(builder, CircleRegionFeatureConfig::new);
	});
	
	public final int radius;

	public CircleRegionFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset, int radius) {
		super(regionSize, minSpacing, positionAttemptCount, seedOffset);
		this.radius = radius;
	}

	@Override
	public List<BlockPos> generatePositions(Map<BlockPos, RegionFeatureConfig> allPositions, int regionX, int regionZ) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		
//		for (int i = 0; i < 5; i++) {
//			final BlockPos pos = new BlockPos((regionX) * regionSize + 16 * i, 0, (regionZ) * regionSize + 16 * i);
//        	positions.add(pos);
//        	allPositions.put(pos, this);
//		}
		
        for (int i = 0; i < this.positionAttemptCount; i++)
        {
            final double maxHorizontalFeatureRadius = this.radius;

            final int regionCenterX = (int) ((regionX) * regionSize + regionSize / 2);
            final int regionCenterZ = (int) ((regionZ) * regionSize + regionSize / 2);

            final int randomSpace = (int) (regionSize - maxHorizontalFeatureRadius * 2);

            final int featureCenterX = regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
            
            if (this.isPostionAcceptable(allPositions, pos, this)) {
            	positions.add(pos);
            	allPositions.put(pos, this);
            }
        }
		return positions;
	}

	@Override
	public int getRadius() {
		return this.radius;
	}

}
