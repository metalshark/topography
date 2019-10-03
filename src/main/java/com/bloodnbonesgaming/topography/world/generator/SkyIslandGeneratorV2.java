package com.bloodnbonesgaming.topography.world.generator;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.SkyIslandDataHandler;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class SkyIslandGeneratorV2 extends SkyIslandGenerator {
	
	OpenSimplexNoiseGeneratorOctaves horizontalConeSkewNoise;
	
	@Override
    public void generate(final World world, ChunkPrimer primer, int chunkX, int chunkZ, final Random random)
    {
        this.horizontalConeSkewNoise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
        super.generate(world, primer, chunkX, chunkZ, random);
    }
		
	@Override
	public void generateIslands(final long seed, final int chunkX, final int chunkZ, final ChunkPrimer primer)
    {
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
                final double maxHorizontalRadius = data.getHorizontalRadius();
                final double maxTopHeight = data.getTopHeight();
                final double maxBottomHeight = data.getBottomHeight();
                
                for (double x = 0; x < 16; x++)
                {
                    final double realX = x + chunkBlockX;
                    final double xDistance = Math.pow(Math.abs(featureCenterX - realX), 2);
                    
                    for (double z = 0; z < 16; z++)
                    {
                        final double realZ = z + chunkBlockZ;
                        
                        final double zDistance = Math.pow(Math.abs(featureCenterZ - realZ), 2);
                        
                        if (Math.sqrt(xDistance + zDistance) <= maxHorizontalRadius)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            final Map<MinMaxBounds, IBlockState> boundsToState = type.getBoundsToStateMap();
                            //distance from the anti water ring radius
                            //they can be 100 in any direction already.
                            //If the skew is 60%, they can be another +30 
                            //If the radius is reduced by 30%, they can still be 100 in any direction, they are just unlikely to hit it
                            
                            for (double y = 0; y <= 255; y++)
                            {
                            	final double maxConeCoordinateSkewValue = maxHorizontalRadius * 0.6;
                            	final double horizontalConeCoordinateSkew = (this.horizontalConeSkewNoise.eval(realX / maxConeCoordinateSkewValue, y / maxConeCoordinateSkewValue, realZ / maxConeCoordinateSkewValue, 3, 0.5) - 0.5) * maxConeCoordinateSkewValue;
                            	final double skewedXDistance = Math.pow(Math.abs(featureCenterX - (realX + horizontalConeCoordinateSkew)), 2);
                            	final double skewedZDistance = Math.pow(Math.abs(featureCenterZ - (realZ + horizontalConeCoordinateSkew)), 2);
                            	final double maxTopConeHeightAtPos = maxTopHeight / (maxHorizontalRadius * 0.7) * Math.sqrt(skewedXDistance + skewedZDistance);
                            	final double maxBottomConeHeightAtPos = maxBottomHeight / (maxHorizontalRadius * 0.7) * Math.sqrt(skewedXDistance + skewedZDistance);
                            	
                                
                                final double bottomHeight = maxBottomConeHeightAtPos + midHeight - maxBottomHeight;
                                double topHeight = midHeight + maxTopHeight - maxTopConeHeightAtPos;
                                
                                
                                final int mid = (int) Math.floor(((topHeight + midHeight) - bottomHeight) / 2 + bottomHeight);
                                
                                final double distance = Math.floor(((topHeight + midHeight) - bottomHeight) / 2);
                                IBlockState state = type.getMainBlock();
                                //Bottom
                                if (y < midHeight)
                                {                                    
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                        }
                                    }
                                    
                                    if (bottomHeight <= y)
                                    {
                                    	primer.setBlockState((int) x, (int) y, (int) z, state);
                                    }
                                }
                                else 
                                {//Top
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y + midHeight - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                            break;
                                        }
                                    }
                                    
                                    if (topHeight >= y)
                                    {
                                    	primer.setBlockState((int) x, (int) (y), (int) z, state);
                                    }
//                                    else if (type.generateFluid() && ((maxFeatureRadius - noiseDistance * noise2) - Math.sqrt(xDistance + zDistance)) > antiWaterRingDistance && y < maxWaterHeight)
//                                    {
//                                    	primer.setBlockState((int) x, (int) (y + midHeight), (int) z, type.getFluidBlock());
//                                    }
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
                    final double minDistance = data.getHorizontalRadius();
                    
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	  //TODO Update documentation
    @ScriptMethodDocumentation(args = "int, int, boolean", usage = "radius, count, randomTypes", notes = "Generates a SkyIslandData and returns it. "
    		+ "Radius is the radius of the sky islands to be generated, count is the number of times to attempt to generate sky islands, randomTypes is how to use the SkyIslandTypes. "
    		+ "If randomTypes is set to true it will randomly choose a SkyIslandType from the list when an island is generated. "
    		+ "If it is set to false, then every time an island is generated it will use the next SkyIslandType in the list. This allows you to guarantee certain islands are generated in a region.")
	public SkyIslandData addSkyIslands(final int horizontalRadius, final int topHeight, final int bottomHeight, final int count, final boolean randomTypes)
    {
        final SkyIslandData data = new SkyIslandData();
        data.setHorizontalRadius(horizontalRadius);
        data.setTopHeight(topHeight);
        data.setBottomHeight(bottomHeight);
        data.setCount(count);
        data.setRandomTypes(randomTypes);

        this.skyIslandData.add(data);

        return data;
    }
//TODO Update documentation
    @ScriptMethodDocumentation(args = "int, int, boolean, int", usage = "radius, count, randomTypes, minCount", notes = "Generates a SkyIslandData and returns it. "
    		+ "Radius is the radius of the sky islands to be generated, count is the number of times to attempt to generate sky islands, randomTypes is how to use the SkyIslandTypes, minCount is the minimum number of the sky islands which must be generated. "
    		+ "If randomTypes is set to true it will randomly choose a SkyIslandType from the list when an island is generated. "
    		+ "If it is set to false, then every time an island is generated it will use the next SkyIslandType in the list. This allows you to guarantee certain islands are generated in a region.")
	public SkyIslandData addSkyIslands(final int horizontalRadius, final int topHeight, final int bottomHeight, final int count, final boolean randomTypes, final int minCount)
    {
        final SkyIslandData data = new SkyIslandData();
        data.setHorizontalRadius(horizontalRadius);
        data.setTopHeight(topHeight);
        data.setBottomHeight(bottomHeight);
        data.setCount(count);
        data.setRandomTypes(randomTypes);
        data.setMinCount(minCount);

        this.skyIslandData.add(data);

        return data;
    }
}
