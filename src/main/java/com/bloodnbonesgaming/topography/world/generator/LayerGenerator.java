package com.bloodnbonesgaming.topography.world.generator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

public class LayerGenerator implements IGenerator
{

    @Override
    public void generate(ChunkPrimer primer, int chunkX, int chunkZ)
    {
        for (int y = 0; y < 256; y++)
        {
            for (final Entry<MinMaxBounds, IBlockState> entry : layers.entrySet())
            {
                if (entry.getKey().test(y))
                {
                    for (int x = 0; x < 16; x++)
                    {
                        for (int z = 0; z < 16; z++)
                        {
                            primer.setBlockState(x, y, z, entry.getValue());
                        }
                    }
                }
            }
        }
    }

    
    private final Map<MinMaxBounds, IBlockState> layers = new LinkedHashMap<MinMaxBounds, IBlockState>();
    
    public void addLayer(final MinMaxBounds bounds, final ItemBlockData block) throws Exception
    {
        this.layers.put(bounds, block.buildBlockState());
    }
}
