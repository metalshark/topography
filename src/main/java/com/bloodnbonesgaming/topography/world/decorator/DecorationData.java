package com.bloodnbonesgaming.topography.world.decorator;

import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.config.definitions.SkyIslandDefinition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;

public class DecorationData
{
    private final DecoratorScattered decorator;
    private final int regionSize;
    private final int count;
    
    public DecorationData(final DecoratorScattered decorator, final int count, final int regionSize)
    {
        this.decorator = decorator;
        this.regionSize = regionSize;
        this.count = count;
    }

    public DecoratorScattered getDecorator()
    {
        return decorator;
    }

    public int getRegionSize()
    {
        return regionSize;
    }

    public int getCount()
    {
        return count;
    }
    
    public void generateForSkyIsland(final long seed, final int chunkX, final int chunkZ, final ChunkPrimer primer, final BlockPos center, final SkyIslandData data, final SkyIslandType type, final SkyIslandDefinition handler)
    {
        this.decorator.generateForSkyIsland(this.count, seed, chunkX, chunkZ, primer, center, data, type, handler, this.regionSize);
    }
}
