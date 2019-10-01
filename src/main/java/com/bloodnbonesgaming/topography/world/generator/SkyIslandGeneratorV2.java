package com.bloodnbonesgaming.topography.world.generator;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;
import com.bloodnbonesgaming.topography.world.SkyIslandDataHandler;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class SkyIslandGeneratorV2 extends SkyIslandGenerator {
		
	@Override
	public void generateIslands(final long seed, final int chunkX, final int chunkZ, final ChunkPrimer primer)
    {
        this.generateNoise(this.smallNoiseArray, 5, 33, 5, chunkX * 16, 0, chunkZ * 16, 4, 8, 4);
        NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
        
        FastNoise noise = new FastNoise();
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.01f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        noise.SetSeed((int) seed);
        
        final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.getIslandPositions(seed, chunkX * 16, chunkZ * 16).entrySet().iterator();
        
        while (iterator.hasNext())
        {
            final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> entry = iterator.next();
            final SkyIslandData data = entry.getKey();
            final int chunkBlockX = chunkX * 16;
            final int chunkBlockZ = chunkZ * 16;
            
            for (final Entry<BlockPos, SkyIslandType> islandPos : entry.getValue().entrySet())
            {
                final int featureCenterX = islandPos.getKey().getX();
                final int featureCenterZ = islandPos.getKey().getZ();
                final int midHeight = islandPos.getKey().getY();
                final int maxFeatureRadius = data.getRadius();
                
                for (double x = 0; x < 16; x++)
                {
                    final double realX = x + chunkBlockX;
                    final double xDistance = Math.pow(Math.abs(featureCenterX - realX), 2);
                    
                    for (double z = 0; z < 16; z++)
                    {
                        final double realZ = z + chunkBlockZ;
                        
                        final double zDistance = Math.pow(Math.abs(featureCenterZ - realZ), 2);
                        
                        final double maxNoiseDistance = (maxFeatureRadius - Math.sqrt(xDistance + zDistance)) * 1.5;
                        
                        final double noiseDistance = maxFeatureRadius * 0.32;
                        
                        final double noise2 = this.terrainNoise.eval((realX) / noiseDistance, (realZ) / noiseDistance, 3, 0.5);
                        
                        if (Math.sqrt(xDistance + zDistance) <= maxFeatureRadius)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            final Map<MinMaxBounds, IBlockState> boundsToState = type.getBoundsToStateMap();
                            double waterNoise = (noise.GetNoise((float) x + chunkX * 16, (float) z + chunkZ * 16) + 1) / type.getWaterDivisionValue();
                            double antiWaterRingDistance = maxFeatureRadius / 10;
                            double waterRingHeightDivision = maxFeatureRadius * 0.07;
                            double maxWaterHeight = maxFeatureRadius * 0.05;
                            //distance from the anti water ring radius
                            double distanceFromWaterRing = Math.abs(antiWaterRingDistance - ((maxFeatureRadius - noiseDistance * noise2) - Math.sqrt(xDistance + zDistance)));
                            //
                            double scaledDistanceFromWaterRing = Math.max((antiWaterRingDistance - distanceFromWaterRing) / waterRingHeightDivision, 0);
                            
                            for (double y = 0; y < midHeight; y++)
                            {
                                final double skewNoise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)] * 2 - 1;
                                 double skewedNoise = this.terrainNoise.eval((realX + 16 * skewNoise) / (noiseDistance * 2), (realZ + 16 * skewNoise) / (noiseDistance * 2), 3, 0.5);
                                
                                final double bottomHeight = midHeight - skewedNoise * (maxNoiseDistance - noiseDistance * noise2);
                                double topHeight;
                                
                                //Increases exponentially increases the max height the closer to the center of the island it is.
                                double maxTopNoise = ((maxFeatureRadius - Math.sqrt(xDistance + zDistance)) + (maxFeatureRadius - Math.sqrt(xDistance + zDistance)) * ((maxFeatureRadius - Math.sqrt(xDistance + zDistance)) / maxFeatureRadius)) * 1.5;
                                
                                topHeight = skewedNoise * Math.max((maxTopNoise - noiseDistance * noise2) * (1 + scaledDistanceFromWaterRing), 0) / 3;
                                
                                double waterHeight = waterNoise * Math.max(maxNoiseDistance - noiseDistance, 0);
                                
                                topHeight -= waterHeight;
                                topHeight = Math.max(Math.max(topHeight, ((antiWaterRingDistance - distanceFromWaterRing) / (antiWaterRingDistance / maxWaterHeight))), 0);
                                
                                if (distanceFromWaterRing < 1)
                                {
                                	topHeight = Math.max(maxWaterHeight, topHeight);
                                }
                                
                                final int mid = (int) Math.floor(((topHeight + midHeight) - bottomHeight) / 2 + bottomHeight);
                                
                                final double distance = Math.floor(((topHeight + midHeight) - bottomHeight) / 2);
                                IBlockState state = type.getMainBlock();
                                //Bottom
                                {                                    
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                        }
                                    }
                                    
                                    if (bottomHeight < y)
                                    {
                                        primer.setBlockState((int) x, (int) y, (int) z, state);
                                    }
                                }
                                
                                //Top
                                {//                                                                        
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y + midHeight - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                            break;
                                        }
                                    }
                                    
                                    if (topHeight != 0 && topHeight >= y)
                                    {
                                    	primer.setBlockState((int) x, (int) (y + midHeight), (int) z, state);
                                    }
                                    else if (type.generateFluid() && ((maxFeatureRadius - noiseDistance * noise2) - Math.sqrt(xDistance + zDistance)) > antiWaterRingDistance && y < maxWaterHeight)
                                    {
                                    	primer.setBlockState((int) x, (int) (y + midHeight), (int) z, type.getFluidBlock());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
	
	@Override
	public void replaceBiomeBlocks(final long seed, int chunkX, int chunkZ, ChunkPrimer primer)
    {
        for (int x = 0; x < 16; x++)
        {
            x:
            for (int z = 0; z < 16; z++)
            {
                final BlockPos pos = new BlockPos(chunkX * 16 + x, 0, chunkZ * 16 + z);
                
                final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.getIslandPositions(seed, chunkX * 16, chunkZ * 16).entrySet().iterator();
                
                while (iterator.hasNext())
                {
                    final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set = iterator.next();
                    final SkyIslandData data = set.getKey();
                    final double minDistance = data.getRadius();
                    
                    for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
                    {
                        if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) <= minDistance)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            
                            if (type.isGenBiomeBlocks())
                            {
                                Biome biome = Biome.getBiome(type.getBiome());
                                
                                if (biome != Biomes.VOID)
                                {
                                    this.genBiomeTerrainBlocks(biome, this.rand, primer, chunkX * 16 + x, chunkZ * 16 + z, islandPos.getKey().getY(), 2.625, type);
                                }
                            }
                            continue x;
                        }
                    }
                }
            }
        }
    }
	
	public void genBiomeTerrainBlocks(Biome biome, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, int islandMid,
			double noiseVal, final SkyIslandType type) {
		int i = islandMid;
        IBlockState iblockstate = biome.topBlock;
        IBlockState iblockstate1 = biome.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = z & 15;
        int i1 = x & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1)
        {
            IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

            if (iblockstate2.getMaterial() == Material.AIR)
            {
                j = -1;
            }
            else if (iblockstate2 == type.getMainBlock())
            {
                if (j == -1)
                {
                    if (k <= 0)
                    {
                        iblockstate = AIR;
                        iblockstate1 = type.getMainBlock();
                    }
                    else if (j1 >= i - 4 && j1 <= i + 1)
                    {
                        iblockstate = biome.topBlock;
                        iblockstate1 = biome.fillerBlock;
                    }

                    if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                    {
                        if (biome.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F)
                        {
                            iblockstate = ICE;
                        }
                        else
                        {
                            iblockstate = WATER;
                        }
                    }

                    j = k;

                    if (j1 >= i - 1)
                    {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                    }
                    else if (j1 < i - 7 - k)
                    {
                        iblockstate = AIR;
                        iblockstate1 = type.getMainBlock();
                        chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                    }
                    else
                    {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                    }
                }
                else if (j > 0)
                {
                    --j;
                    chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                    if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1)
                    {
                        j = rand.nextInt(4) + Math.max(0, j1 - 63);
                        iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                    }
                }
            }
        }
	}
}
