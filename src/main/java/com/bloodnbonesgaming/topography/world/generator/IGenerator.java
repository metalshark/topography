package com.bloodnbonesgaming.topography.world.generator;

import java.util.Map;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public interface IGenerator
{
    public default void generate(final World world, final ChunkPrimer primer, final int chunkX, final int chunkZ, final Random random) {};
    public default void populate(final World world, final int chunkX, final int chunkZ, final Random random) {};
    public default GenLayer getLayer(final World world, final GenLayer parent) {return null;};
    public default int getRegionSize()
    {
    	return 0;
    }
}
