package com.bloodnbonesgaming.randomgenskyislands.world.layer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandData;
import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandType;
import com.bloodnbonesgaming.randomgenskyislands.world.SkyIslandDataHandler;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiomeSkyIslands extends GenLayer
{
    final SkyIslandDataHandler handler;
//    List<Integer> biomes = new ArrayList<>();
//    final Random biomeRand = new Random();
    final long worldSeed;
    
//    final List<SkyIslandData> skyIslandData = new ArrayList<SkyIslandData>();
//    final Map<Double, List<BlockPos>> islandPositions = new LinkedHashMap<Double, List<BlockPos>>();
    private final Random islandIndexRandom = new Random();


    public GenLayerBiomeSkyIslands(long p_i2125_1_, final SkyIslandDataHandler handler)
    {
        super(p_i2125_1_);
        this.worldSeed = p_i2125_1_;
        this.handler = handler;
        
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
        
//        this.biomes.add(Biome.getIdForBiome(Biomes.FOREST));
//        this.biomes.add(Biome.getIdForBiome(Biomes.DESERT));
//        this.biomes.add(Biome.getIdForBiome(Biomes.PLAINS));
//        this.biomes.add(Biome.getIdForBiome(Biomes.SAVANNA));
//        this.biomes.add(Biome.getIdForBiome(Biomes.MESA));
//        this.biomes.add(Biome.getIdForBiome(Biomes.EXTREME_HILLS));
//        this.biomes.add(Biome.getIdForBiome(Biomes.BIRCH_FOREST));
//        this.biomes.add(Biome.getIdForBiome(Biomes.COLD_TAIGA));
//        this.biomes.add(Biome.getIdForBiome(Biomes.ICE_PLAINS));
//        this.biomes.add(Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND));
//        this.biomes.add(Biome.getIdForBiome(Biomes.REDWOOD_TAIGA));
//        this.biomes.add(Biome.getIdForBiome(Biomes.TAIGA));
    }

    @Override
    public int[] getInts(int chunkX, int chunkZ, int width, int depth)
    {
        int[] returnInts = IntCache.getIntCache(width * depth);
        
//        this.biomeRand.setSeed((long)((int)Math.floor(chunkX / regionSize)) * 341873128712L + (long)((int)Math.floor(chunkZ / regionSize)) * 132897987541L + this.worldSeed);
//        final int biome = this.biomes.get(this.biomeRand.nextInt(biomes.size()));
//        
//        for (int i = 0; i < returnInts.length; i++)
//        {
//            returnInts[i] = biome;
//        }
        
//        for (final SkyIslandData data : this.skyIslandData)
//        {
//            countLoop:
//            for (int i = 0; i < data.getCount(); i++)
//            {
//                final double maxFeatureRadius = data.getRadius();
//                final double midHeight = maxFeatureRadius + this.biomeRand.nextInt((int) (220 - (maxFeatureRadius * 2)));
//                
//                final int regionCenterX = (int) (((int)Math.floor(chunkX / regionSize)) * regionSize + regionSize / 2);
//                final int regionCenterZ = (int) (((int)Math.floor(chunkZ / regionSize)) * regionSize + regionSize / 2);
//                
//                final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);
//                
//                final int featureCenterX = this.biomeRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
//                final int featureCenterZ = this.biomeRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;
//                
//                final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
//                
//                for (final Entry<Double, List<BlockPos>> set : this.islandPositions.entrySet())
//                {
//                    final double minDistance = set.getKey() + maxFeatureRadius + 25;
//                    
//                    for (final BlockPos islandPos : set.getValue())
//                    {
//                        if (pos.getDistance(islandPos.getX(), islandPos.getY(), islandPos.getZ()) < minDistance)
//                        {
//                            continue countLoop;
//                        }
//                    }
//                }
//                
//                if (!this.islandPositions.containsKey(maxFeatureRadius))
//                {
//                    this.islandPositions.put(maxFeatureRadius, new ArrayList<BlockPos>());
//                }
//                final List<BlockPos> positions = this.islandPositions.get(maxFeatureRadius);
//                positions.add(pos);
//                
//            }
//        }
        final Map<SkyIslandData, Map<BlockPos, SkyIslandType>> islandPositions = this.handler.getIslandPositions(this.worldSeed, chunkX, chunkZ);
        
        for (int x = 0; x < width; x++)
        {
            x:
            for (int z = 0; z < depth; z++)
            {
                final BlockPos pos = new BlockPos(chunkX + x, 0, chunkZ + z);
                
                for (final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : islandPositions.entrySet())
                {
                    final SkyIslandData data = set.getKey();
                    int islandCount = -1;
                    final double minDistance = data.getRadius();
                    
                    for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
                    {
                        islandCount++;
                        if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) <= minDistance)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            
                            returnInts[x + z * width] = type.getBiome();
                            continue x;
                        }
                    }
                }
                returnInts[x + z * width] = Biome.getIdForBiome(Biomes.VOID);
            }
        }
        return returnInts;
    }

}
