package com.bloodnbonesgaming.topography.world.generator;

import net.minecraft.world.chunk.ChunkPrimer;

public interface IGenerator
{
    public abstract void generate(final ChunkPrimer primer, final int chunkX, final int chunkZ);
}
