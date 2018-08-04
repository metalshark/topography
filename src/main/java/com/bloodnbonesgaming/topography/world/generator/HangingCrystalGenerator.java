package com.bloodnbonesgaming.topography.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.BlockPredicate;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class HangingCrystalGenerator implements IGenerator
{
    private final int count;
    private final int expansionAttempts;
    private int minHeight = 4;
    private int maxHeight = 250;
    private final IBlockState block;
    private final List<BlockPredicate> requiredBlocks = new ArrayList<BlockPredicate>();
    
    public HangingCrystalGenerator(final ItemBlockData data, final int count, final int expansionAttempts) throws Exception
    {
        this.block = data.buildBlockState();
        this.count = count;
        this.expansionAttempts = expansionAttempts;
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
        final MutableBlockPos mutable = new MutableBlockPos();
        final MutableBlockPos mutable2 = new MutableBlockPos();
        
        for (int i = 0; i < this.count; i++)
        {
            final int x = chunkX * 16 + rand.nextInt(16) + 8;
            final int y = rand.nextInt(this.maxHeight - this.minHeight + 1) + this.minHeight;
            final int z = chunkZ * 16 + rand.nextInt(16) + 8;
            
            mutable.setPos(x, y, z);
            
            if (world.isAirBlock(mutable) && this.isBlockAcceptable(world, mutable.up()))
            {
                world.setBlockState(mutable, this.block, 2);
                
                for (int index = 0; index < this.expansionAttempts; index++)
                {
                    int x2 = x + rand.nextInt(8) - rand.nextInt(8);
                    int y2 = y - rand.nextInt(12);
                    int z2 = z + rand.nextInt(8) - rand.nextInt(8);
                    
                    mutable.setPos(x2, y2, z2);
                    
                    if (world.isAirBlock(mutable))
                    {
                        int crystal = 0;
                        
                        if (world.getBlockState(mutable2.setPos(x2, y2, z2 - 1)) == this.block)//North
                            crystal++;
                        if (world.getBlockState(mutable2.setPos(x2, y2, z2 + 1)) == this.block)//South
                        {
                            crystal++;
                            if (crystal > 1)
                                continue;
                        }
                        if (world.getBlockState(mutable2.setPos(x2 + 1, y2, z2)) == this.block)//East
                        {
                            crystal++;
                            if (crystal > 1)
                                continue;
                        }
                        if (world.getBlockState(mutable2.setPos(x2 - 1, y2, z2)) == this.block)//West
                        {
                            crystal++;
                            if (crystal > 1)
                                continue;
                        }
                        if (world.getBlockState(mutable2.setPos(x2, y2 + 1, z2)) == this.block)//Up
                        {
                            crystal++;
                            if (crystal > 1)
                                continue;
                        }
                        if (world.getBlockState(mutable2.setPos(x2, y2 - 1, z2)) == this.block)//Down
                        {
                            crystal++;
                            if (crystal > 1)
                                continue;
                        }

                        if (crystal == 1)
                        {
                            world.setBlockState(mutable, this.block, 2);
                        }
                    }
                }
            }
        }
    }
    
    private boolean isBlockAcceptable(final World world, final BlockPos pos)
    {
        if (this.requiredBlocks.isEmpty() && !world.isAirBlock(pos))
        {
            return true;
        }
        for (final BlockPredicate predicate : this.requiredBlocks)
        {
            if (predicate.test(world.getBlockState(pos)))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

}
