package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

public abstract class RegionFeature<U extends RegionFeatureConfig> extends Feature<U>{

	public RegionFeature(Codec<U> codec) {
		super(codec);
	}
	
	protected long getRegionSeed(RegionFeatureConfig config, int regionX, int regionZ, long worldSeed) {
		return (long) (regionX) * 341873128712L + (long) (regionZ) * 132897987541L + worldSeed + config.featureCountSeedOffset;
	}
	
	protected List<BlockPos> generatePositions(RegionFeatureConfig config, int regionX, int regionZ, int radius) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		
        for (int i = 0; i < config.positionAttemptCount; i++)
        {
            final double maxHorizontalFeatureRadius = radius;

            final int regionCenterX = (int) ((regionX) * config.regionSize + config.regionSize / 2);
            final int regionCenterZ = (int) ((regionZ) * config.regionSize + config.regionSize / 2);

            final int randomSpace = (int) (config.regionSize - maxHorizontalFeatureRadius * 2);

            final int featureCenterX = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
            
            if (this.isPostionAcceptable(positions, pos, maxHorizontalFeatureRadius, config.minSpacing)) {
            	positions.add(pos);
            }
        }
		return positions;
	}
    
	protected boolean isPostionAcceptable(List<BlockPos> positions, BlockPos pos, double maxHorizontalFeatureRadius, int extraSpacing) {
		double minDistance = maxHorizontalFeatureRadius * 2 + extraSpacing;
		
    	for (BlockPos position : positions) {
    		if (getHorizontalDistance(pos, position) < minDistance) {
    			return false;
    		}
    	}
    	return true;
    }

    protected static double getHorizontalDistance(final BlockPos pos, final BlockPos pos2)//Switch this to using multiplied values instead of sqrt. faster
    {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return Math.sqrt(d0 * d0 + d2 * d2);
    }
	
    protected boolean canBeInChunk(BlockPos center, double radius, final int chunkMinX, final int chunkMinZ) {
    	if (center.getX() - radius > chunkMinX + 16) {//If outside chunk in +X direction, and radius isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getX() + radius < chunkMinX) {//If outside chunk in -X direction, and radius isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getZ() - radius > chunkMinZ + 16) {//If outside chunk in +Z direction, and radius isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getZ() + radius < chunkMinZ) {//If outside chunk in -Z direction, and radius isn't large enough to get into chunk
    		return false;
    	}
    	return true;
    }
}
