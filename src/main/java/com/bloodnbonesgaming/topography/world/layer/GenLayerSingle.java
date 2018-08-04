package com.bloodnbonesgaming.topography.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerSingle extends GenLayer
{
    final int biome;

    public GenLayerSingle(long p_i2125_1_, final int biome)
    {
        super(p_i2125_1_);
        this.biome = biome;
    }

    @Override
    public int[] getInts(int chunkX, int chunkZ, int width, int depth)
    {
        int[] returnInts = IntCache.getIntCache(width * depth);
                
        for (int x = 0; x < width; x++)
        {
            for (int z = 0; z < depth; z++)
            {
                returnInts[x + z * width] = this.biome;
            }
        }
        return returnInts;
    }

}