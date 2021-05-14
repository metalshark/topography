package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;

public abstract class SquareRegionFeatureConfig extends RegionFeatureConfig implements ISquareConfig {
	
	public SquareRegionFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int sizeX, int sizeZ) {
		super(regionSize, minSpacing, positionAttemptCount);
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
	}

	public int sizeX;
	public int sizeZ;


	
	@Override
	public List<BlockPos> generatePositions(Map<BlockPos, RegionFeatureConfig> allPositions, int regionX, int regionZ) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		
        for (int i = 0; i < this.positionAttemptCount; i++) {
//            final int regionCenterX = (int) ((regionX) * config.regionSize + config.regionSize / 2);
//            final int regionCenterZ = (int) ((regionZ) * config.regionSize + config.regionSize / 2);
            final int regionMinX = regionX * this.regionSize;
            final int regionMinZ = regionZ * this.regionSize;
            
            final int randomXSpace = this.regionSize - sizeX;
            final int randomZSpace = this.regionSize - sizeZ;
            
            final int featureX = regionMinX + this.regionPositionRand.nextInt(randomXSpace);
            final int featureZ = regionMinZ + this.regionPositionRand.nextInt(randomZSpace);
            
            
            
//            final int randomSpace = (int) (config.regionSize - templateSize.getX() > templateSize.getZ() ? templateSize.getX() : templateSize.getZ());
//
//            final int featureCenterX = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
//            final int featureCenterZ = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureX, 0, featureZ);
            
            if (this.isPostionAcceptable(allPositions, pos, this)) {
            	positions.add(pos);
            	allPositions.put(pos, this);
            }
        }
		return positions;
	}

	@Override
	public int getSizeX() {
		return this.sizeX;
	}

	@Override
	public int getSizeZ() {
		return sizeZ;
	}

}
