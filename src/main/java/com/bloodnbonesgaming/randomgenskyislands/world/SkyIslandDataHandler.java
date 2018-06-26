package com.bloodnbonesgaming.randomgenskyislands.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandData;
import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandType;

import net.minecraft.util.math.BlockPos;

public class SkyIslandDataHandler
{
    private final List<SkyIslandData> skyIslandData = new ArrayList<SkyIslandData>();
    private Map<SkyIslandData, Map<BlockPos, SkyIslandType>> islandPositions = new LinkedHashMap<SkyIslandData, Map<BlockPos, SkyIslandType>>();
    private final Random islandPositionRandom = new Random();
    private final Random islandIndexRandom = new Random();
    private double regionSize = 464;
    
    private int currentChunkX = -10000000;
    private int currentChunkZ = -10000000;
    
    public static final Map<String, Class> classKeywords = new HashMap<String, Class>();
    
    static {
        SkyIslandDataHandler.classKeywords.put("SkyIslandType", SkyIslandType.class);
    }
    
    public SkyIslandDataHandler()
    {
//        
////      final SkyIslandType end = new SkyIslandType();
////      end.setMainBlock(Blocks.END_STONE.getDefaultState());
////      end.setGenBiomeBlocks(false);
////      end.setGenDecorations(false);
//      
////      final SkyIslandType obsidian = new SkyIslandType();
////      obsidian.setMainBlock(Blocks.OBSIDIAN.getDefaultState());
////      obsidian.setGenBiomeBlocks(false);
////      obsidian.setGenDecorations(false);
//      
////      final SkyIslandType lava = new SkyIslandType();
////      lava.setMainBlock(Blocks.LAVA.getDefaultState());
////      lava.setGenBiomeBlocks(false);
////      lava.setGenDecorations(false);
//      
//      final SkyIslandType forest = new SkyIslandType();
//      forest.setBiome(Biome.getIdForBiome(Biomes.FOREST));
//      
//      final SkyIslandType cold_taiga = new SkyIslandType();
//      cold_taiga.setBiome(Biome.getIdForBiome(Biomes.COLD_TAIGA));
//      
//      final SkyIslandType desert = new SkyIslandType();
//      desert.setBiome(Biome.getIdForBiome(Biomes.DESERT));
//      
//      final SkyIslandType mesa = new SkyIslandType();
//      mesa.setBiome(Biome.getIdForBiome(Biomes.MESA));
//      
//      final SkyIslandType jungle = new SkyIslandType();
//      jungle.setBiome(Biome.getIdForBiome(Biomes.JUNGLE));
//      jungle.setGenDecorations(false);
//      
//      final SkyIslandType extreme_hills = new SkyIslandType();
//      extreme_hills.setBiome(Biome.getIdForBiome(Biomes.EXTREME_HILLS));
//      
//      
//      final SkyIslandData large = new SkyIslandData();
//      large.setCount(1);
//      large.setRadius(100);
//      large.setRandomIslands(false);
//      large.addType(forest);
//      large.addType(cold_taiga);
//      large.addType(desert);
//      large.addType(mesa);
//      large.addType(jungle);
//      large.addType(extreme_hills);
////      large.addType(end);
////      large.addType(obsidian);
////      large.addType(lava);
//      this.skyIslandData.add(large);
//      
//      final SkyIslandData medium = new SkyIslandData();
//      medium.setCount(32);
//      medium.setRadius(50);
//      medium.setRandomIslands(false);
//      medium.addType(forest);
//      medium.addType(cold_taiga);
//      medium.addType(desert);
//      medium.addType(mesa);
//      medium.addType(jungle);
//      medium.addType(extreme_hills);
////      medium.addType(end);
////      medium.addType(obsidian);
////      medium.addType(lava);
//      this.skyIslandData.add(medium);
//      
//      final SkyIslandData small = new SkyIslandData();
//      small.setCount(64);
//      small.setRadius(25);
//      small.setRandomIslands(false);
//      small.addType(forest);
//      small.addType(cold_taiga);
//      small.addType(desert);
//      small.addType(mesa);
//      small.addType(jungle);
//      small.addType(extreme_hills);
////      small.addType(end);
////      small.addType(obsidian);
////      small.addType(lava);
//      this.skyIslandData.add(small);
//      
//      final SkyIslandData tiny = new SkyIslandData();
//      tiny.setCount(128);
//      tiny.setRadius(10);
//      tiny.setRandomIslands(false);
//      tiny.addType(forest);
//      tiny.addType(cold_taiga);
//      tiny.addType(desert);
//      tiny.addType(mesa);
//      tiny.addType(jungle);
//      tiny.addType(extreme_hills);
////      tiny.addType(end);
////      tiny.addType(obsidian);
////      tiny.addType(lava);
//      this.skyIslandData.add(tiny);
    }
    
    private void generateIslandPositions(final long worldSeed)
    {
        this.islandPositionRandom.setSeed((long)((int)Math.floor(this.currentChunkX * 16D / regionSize)) * 341873128712L + (long)((int)Math.floor(this.currentChunkZ * 16D / regionSize)) * 132897987541L + worldSeed);
        this.islandPositions = new LinkedHashMap<SkyIslandData, Map<BlockPos, SkyIslandType>>();
        for (final SkyIslandData data : this.skyIslandData)
        {
            countLoop:
            for (int i = 0; i < data.getCount(); i++)
            {
                final double maxFeatureRadius = data.getRadius();
                final double midHeight = maxFeatureRadius + this.islandPositionRandom.nextInt((int) (220 - (maxFeatureRadius * 2)));
                
                final int regionCenterX = (int) (((int)Math.floor(this.currentChunkX * 16D / regionSize)) * regionSize + regionSize / 2);
                final int regionCenterZ = (int) (((int)Math.floor(this.currentChunkZ * 16D / regionSize)) * regionSize + regionSize / 2);
                
                final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);
                
                final int featureCenterX = this.islandPositionRandom.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
                final int featureCenterZ = this.islandPositionRandom.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;
                
                final BlockPos pos = new BlockPos(featureCenterX, midHeight, featureCenterZ);
                
                for (final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : this.islandPositions.entrySet())
                {
                    final double minDistance = set.getKey().getRadius() + maxFeatureRadius + 25;
                    
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
                    positions.put(pos, data.getType(i));
                }
            }
        }
    }
    
    public Map<SkyIslandData, Map<BlockPos, SkyIslandType>> getIslandPositions(final long worldSeed, final int x, final int z)
    {
        if ((int) (Math.floor(x / 16.0D)) != this.currentChunkX || (int) (Math.floor(z / 16.0D)) != this.currentChunkZ)
        {
            this.currentChunkX = (int) Math.floor(x / 16.0D);
            this.currentChunkZ = (int) Math.floor(z / 16.0D);
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void setRegionSize(final double size)
    {
        this.regionSize = size;
    }
    
//    @ScriptArgs(args = {ArgType.INT, ArgType.INT})
    public SkyIslandData addSkyIslands(final int radius, final int count, final boolean randomTypes)
    {
        final SkyIslandData data = new SkyIslandData();
        data.setRadius(radius);
        data.setCount(count);
        data.setRandomIslands(randomTypes);
        
        this.skyIslandData.add(data);
        
        return data;
    }
}
