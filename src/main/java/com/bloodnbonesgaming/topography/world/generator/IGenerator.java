package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public interface IGenerator
{
    public abstract void generate(final World world, final ChunkPrimer primer, final int chunkX, final int chunkZ);
    public abstract void populate(final World world, final int chunkX, final int chunkZ, final Random rand);
    public abstract GenLayer getLayer(final World world, final GenLayer parent);
}
