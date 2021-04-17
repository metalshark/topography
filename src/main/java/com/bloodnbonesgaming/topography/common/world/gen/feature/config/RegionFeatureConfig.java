package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.Topography;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.IFeatureConfig;

public abstract class RegionFeatureConfig implements IFeatureConfig {

//	public static final Codec<RegionFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
//		return builder.group(Codec.INT.fieldOf("region_size").forGetter((config) -> {
//	    	return config.regionSize;
//		}), Codec.INT.fieldOf("min_spacing").forGetter((config) -> {
//			return config.minSpacing;
//		}), Codec.INT.fieldOf("position_attempt_count").forGetter((config) -> {
//			return config.positionAttemptCount;
//		}), Codec.INT.fieldOf("seed_offset").forGetter((config) -> {
//			return config.featureCountSeedOffset;
//		})).apply(builder, RegionFeatureConfig::new);
//	});

	public final int regionSize;
	public final int minSpacing;
	public final int positionAttemptCount;
	public final int featureCountSeedOffset;
	
	public final Random regionPositionRand = new Random();
	
	
	
	public RegionFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int seedOffset) {
		this.regionSize = regionSize * 16;
		this.minSpacing = minSpacing;
		this.positionAttemptCount = positionAttemptCount;
		this.featureCountSeedOffset = seedOffset;
	}
	
	public abstract List<BlockPos> generatePositions(Map<BlockPos, RegionFeatureConfig> allPositions, int regionX, int regionZ);
    
	protected boolean isPostionAcceptable(Map<BlockPos, RegionFeatureConfig> positions, BlockPos pos, RegionFeatureConfig config) {
		for (Entry<BlockPos, RegionFeatureConfig> entry : positions.entrySet()) {
			if (config instanceof ICircleConfig) {
				if (entry.getValue() instanceof ICircleConfig) {
					if (!testDistanceCircleCircle(pos, (ICircleConfig)config, entry.getKey(), (ICircleConfig)entry.getValue())) {
						return false;
					}
				} else if (entry.getValue() instanceof ISquareConfig) {
					if (!testDistanceSquareCircle(entry.getKey(), (ISquareConfig)entry.getValue(), pos, (ICircleConfig)config)) {
						return false;
					}
				}
			} else if (config instanceof ISquareConfig) {
				if (entry.getValue() instanceof ICircleConfig) {
					if (!testDistanceSquareCircle(pos, (ISquareConfig)config, entry.getKey(), (ICircleConfig)entry.getValue())) {
						return false;
					}
				} else if (entry.getValue() instanceof ISquareConfig) {
					if (!testDistanceSquareSquare(pos, (ISquareConfig)config, entry.getKey(), (ISquareConfig)entry.getValue())) {
						return false;
					}
				}
			}
		}
    	return true;
    }

    protected static double getHorizontalDistanceSq(final BlockPos pos, final BlockPos pos2) {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return d0 * d0 + d2 * d2;
    }

    protected static double getHorizontalDistance(final BlockPos pos, final BlockPos pos2) {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return Math.sqrt(d0 * d0 + d2 * d2);
    }
    
    protected static boolean testDistanceSquareCircle(final BlockPos square, final ISquareConfig squareCfg, final BlockPos circle, final ICircleConfig circleCfg) {
    	int squareMinX = square.getX();
    	int squareMaxX = squareMinX + squareCfg.getSizeX();
    	int squareMinZ = square.getZ();
    	int squareMaxZ = squareMinZ + squareCfg.getSizeZ();
    	
    	int circleMinX = circle.getX() - circleCfg.getRadius();
    	int circleMaxX = circle.getX() + circleCfg.getRadius();
    	int circleMinZ = circle.getZ() - circleCfg.getRadius();
    	int circleMaxZ = circle.getZ() + circleCfg.getRadius();
    	
    	int minSpacing = ((RegionFeatureConfig)squareCfg).minSpacing + ((RegionFeatureConfig)circleCfg).minSpacing;
    	
    	if (circleMaxX + minSpacing > squareMinX && circleMinX - minSpacing < squareMaxX && circleMaxZ + minSpacing > squareMinZ && circleMinZ - minSpacing < squareMaxZ) {
			return false;
    	}
    	return true;
    }
    
    protected static boolean testDistanceCircleCircle(final BlockPos circle, final ICircleConfig circleCfg, final BlockPos circle2, final ICircleConfig circleCfg2) {
    	double minDistance = circleCfg.getRadius() + circleCfg2.getRadius() + ((RegionFeatureConfig)circleCfg).minSpacing + ((RegionFeatureConfig)circleCfg2).minSpacing;
		
		if (getHorizontalDistanceSq(circle, circle2) < minDistance * minDistance) {
			return false;
		}
    	return true;
    }
    
    protected static boolean testDistanceSquareSquare(final BlockPos square, final ISquareConfig squareCfg, final BlockPos square2, final ISquareConfig squareCfg2) {
    	if (square.getX() < square2.getX()) {
    		//If the <x square is large enough to intersect with the >x square
    		if (square.getX() + squareCfg.getSizeX() + ((RegionFeatureConfig)squareCfg).minSpacing + ((RegionFeatureConfig)squareCfg2).minSpacing >= square2.getX()) {
    			return false;
    		}
    	} else {
    		//If the <x square is large enough to intersect with the >x square
    		if (square2.getX() + squareCfg2.getSizeX() + ((RegionFeatureConfig)squareCfg).minSpacing + ((RegionFeatureConfig)squareCfg2).minSpacing >= square.getX()) {
    			return false;
    		}
    	}
    	if (square.getZ() < square2.getZ()) {
    		//If the <z square is large enough to intersect with the >z square
    		if (square.getZ() + squareCfg.getSizeZ() + ((RegionFeatureConfig)squareCfg).minSpacing + ((RegionFeatureConfig)squareCfg2).minSpacing >= square2.getZ()) {
    			return false;
    		}
    	} else {
    		//If the <z square is large enough to intersect with the >z square
    		if (square2.getZ() + squareCfg2.getSizeZ() + ((RegionFeatureConfig)squareCfg).minSpacing + ((RegionFeatureConfig)squareCfg2).minSpacing >= square.getZ()) {
    			return false;
    		}
    	}
    	return true;
    }
}
