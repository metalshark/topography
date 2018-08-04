package com.bloodnbonesgaming.topography.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.BlockPredicate;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class ScatteredBlockGenerator implements IGenerator
{
    private final int clusterCount;
    private final int clusterSize;
    private int minHeight = 4;
    private int maxHeight = 250;
    private final IBlockState block;
    private final List<BlockPredicate> requiredBlocks = new ArrayList<BlockPredicate>();
    
    public ScatteredBlockGenerator(final ItemBlockData data, final int clusterCount, final int clusterSize) throws Exception
    {
        this.block = data.buildBlockState();
        this.clusterCount = clusterCount;
        this.clusterSize = clusterSize;
    }
    
    public void addRequiredBlock(final ItemBlockData data) throws Exception
    {
        this.requiredBlocks.add(data.buildBlockPredicate());
    }
    
    public void setHeight(final int min, final int max)
    {
        this.minHeight = min;
        this.maxHeight = max;
    }

    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ)
    {

    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {
        MutableBlockPos pos = new MutableBlockPos();
        
        for (int i = 0; i < this.clusterCount; i++)
        {
            final int x = chunkX * 16 + rand.nextInt(15) + 8;
            final int y = rand.nextInt(this.maxHeight - this.minHeight + 1 - 4) + this.minHeight;
            final int z = chunkZ * 16 + rand.nextInt(15) + 8;
            
            for (int index = 0; index < this.clusterSize; index++)
            {
                pos.setPos(x + rand.nextInt(8) - rand.nextInt(8), y + rand.nextInt(4) - rand.nextInt(4), z + rand.nextInt(8) - rand.nextInt(8));
                
                if (world.isAirBlock(pos))
                {
                    for (final BlockPredicate predicate : this.requiredBlocks)
                    {
                        if (predicate.test(world.getBlockState(pos.down())))
                        {
                            world.setBlockState(pos, this.block, 2);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

}
