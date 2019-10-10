package com.bloodnbonesgaming.topography.world;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;

import net.minecraft.util.math.BlockPos;

public class SkyIslandDataHandler
{
    private final List<SkyIslandData> skyIslandData = new ArrayList<SkyIslandData>();
    private Map<SkyIslandData, Map<BlockPos, SkyIslandType>> islandPositions = new LinkedHashMap<SkyIslandData, Map<BlockPos, SkyIslandType>>();
    private final Random islandPositionRandom = new Random();
    private double regionSize = 464;

    private int currentRegionX = -100000000;
    private int currentRegionZ = -100000000;

    public SkyIslandDataHandler()
    {
    }

    private void generateIslandPositions(final long worldSeed)
    {
        this.islandPositionRandom.setSeed((long) (this.currentRegionX) * 341873128712L
                + (long) (this.currentRegionZ) * 132897987541L + worldSeed);
        this.islandPositions = new LinkedHashMap<SkyIslandData, Map<BlockPos, SkyIslandType>>();
        for (final SkyIslandData data : this.skyIslandData)
        {
            int genCount = 0;
            countLoop: for (int i = 0; i < data.getCount() || (genCount < data.getMinCount() && i < data.getCount() * 2); i++)
            {
                final double maxFeatureRadius = data.getHorizontalRadius();
                final double midHeight = maxFeatureRadius + this.islandPositionRandom.nextInt((int) (220 - (maxFeatureRadius * 2)));

                final int regionCenterX = (int) ((this.currentRegionX) * regionSize + regionSize / 2);
                final int regionCenterZ = (int) ((this.currentRegionZ) * regionSize + regionSize / 2);

                final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);

                final int featureCenterX = this.islandPositionRandom.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
                final int featureCenterZ = this.islandPositionRandom.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

                final BlockPos pos = new BlockPos(featureCenterX, midHeight, featureCenterZ);

                for (final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : this.islandPositions.entrySet())
                {
                    final double minDistance = set.getKey().getHorizontalRadius() + maxFeatureRadius + 25;

                    for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
                    {
                        if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) < minDistance)
                        {
                            continue countLoop;
                        }
                    }
                }

                if (!this.islandPositions.containsKey(data))
                {
                    this.islandPositions.put(data, new LinkedHashMap<BlockPos, SkyIslandType>());
                }
                if (data.isRandomIslands())
                {
                    final Map<BlockPos, SkyIslandType> positions = this.islandPositions.get(data);
                    positions.put(pos, data.getType(this.islandPositionRandom.nextInt(128)));
                }
                else
                {
                    final Map<BlockPos, SkyIslandType> positions = this.islandPositions.get(data);
                    positions.put(pos, data.getType(genCount));
                }
                genCount++;
            }
        }
    }

    public Map<SkyIslandData, Map<BlockPos, SkyIslandType>> getIslandPositions(final long worldSeed, final int x, final int z)
    {
        if (((int) Math.floor(Math.floor(x / 16.0D) * 16D / this.regionSize)) != this.currentRegionX || ((int) Math.floor(Math.floor(z / 16.0D) * 16D / this.regionSize)) != this.currentRegionZ)
        {
            this.currentRegionX = ((int) Math.floor(Math.floor(x / 16.0D) * 16D / this.regionSize));
            this.currentRegionZ = ((int) Math.floor(Math.floor(z / 16.0D) * 16D / this.regionSize));
            this.generateIslandPositions(worldSeed);
        }
        return this.islandPositions;
    }

    public static double getDistance(final BlockPos pos, final BlockPos pos2)
    {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return Math.sqrt(d0 * d0 + d2 * d2);
    }

    public double getRegionSize()
    {
        return this.regionSize;
    }

    public void setRegionSize(final int size)
    {
        this.regionSize = size * 16;
    }
}
