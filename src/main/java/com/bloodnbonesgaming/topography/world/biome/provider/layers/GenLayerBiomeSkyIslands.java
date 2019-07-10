package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBiomeSkyIslands extends GenLayer
{
    List<Integer> biomes = new ArrayList<>();
    final double regionSize = 464;
    final Random biomeRand = new Random();

    public GenLayerBiomeSkyIslands(long p_i2125_1_)
    {
        super(p_i2125_1_);
        
        
        this.biomes.add(Biome.getIdForBiome(Biomes.FOREST));
        this.biomes.add(Biome.getIdForBiome(Biomes.DESERT));
        this.biomes.add(Biome.getIdForBiome(Biomes.PLAINS));
        this.biomes.add(Biome.getIdForBiome(Biomes.SAVANNA));
        this.biomes.add(Biome.getIdForBiome(Biomes.MESA));
        this.biomes.add(Biome.getIdForBiome(Biomes.EXTREME_HILLS));
        this.biomes.add(Biome.getIdForBiome(Biomes.JUNGLE));
    }

    @Override
    public int[] getInts(int chunkX, int chunkZ, int width, int depth)
    {
        int[] returnInts = IntCache.getIntCache(width * depth);
        
        this.biomeRand.setSeed((long)((int)Math.floor(chunkX / regionSize)) * 341873128712L + (long)((int)Math.floor(chunkZ / regionSize)) * 132897987541L);
        final int biome = this.biomes.get(this.biomeRand.nextInt(biomes.size()));
        
        for (int i = 0; i < returnInts.length; i++)
        {
            returnInts[i] = biome;
        }
        return returnInts;
    }

}
