package com.bloodnbonesgaming.topography.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.BlockPredicate;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "ScatteredPillarGenerator", classExplaination = 
"This file is for the ScatteredPillarGenerator. This generator generates scattered clusters of block pillars per chunk. Similar to how fire is generated in the nether.")
public class ScatteredPillarGenerator implements IGenerator
{
    private final int clusterCount;
    private final int clusterSize;
    private int minHeight = 4;
    private int maxHeight = 250;
    private final IBlockState block;
    private final int minPillarHeight;
    private final int maxPillarHeight;
    private final List<BlockPredicate> requiredBlocks = new ArrayList<BlockPredicate>();
    private final List<BlockPredicate> requiredAdjacentBlocks = new ArrayList<BlockPredicate>();
    
    @ScriptMethodDocumentation(args = "ItemBlockData, int, int", usage = "block to generate, clusters per chunk, blocks per cluster", notes = "This constructs a ScatteredBlockGenerator.")
	public ScatteredPillarGenerator(final ItemBlockData data, final int minHeight, final int maxHeight, final int clusterCount, final int clusterSize) throws Exception
    {
        this.block = data.buildBlockState();
        this.clusterCount = clusterCount;
        this.clusterSize = clusterSize;
        this.minPillarHeight = minHeight;
        this.maxPillarHeight = maxHeight;
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "required block", notes = "Adds a block the generator is allowed to generate blocks on top of.")
	public void addRequiredBlock(final ItemBlockData data) throws Exception
    {
        this.requiredBlocks.add(data.buildBlockPredicate());
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "required block", notes = "Adds a block the generator must have adjacent to the block below it to generate. Mostly for generating things next to water or lava.")
	public void addRequiredAdjacentBlock(final ItemBlockData data) throws Exception
    {
        this.requiredAdjacentBlocks.add(data.buildBlockPredicate());
    }
    
    @ScriptMethodDocumentation(args = "int, int", usage = "min height, max height", notes = "Sets the min and max heights this generator can generate at. Default is 4 and 250.")
	public void setHeight(final int min, final int max)
    {
        this.minHeight = min;
        this.maxHeight = max;
    }

    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random)
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
                		final BlockPos down = pos.down();
                		
                        if (predicate.test(world.getBlockState(down)))
                        {
                        	if (this.requiredAdjacentBlocks.isEmpty())
                        	{
                        		int height = rand.nextInt(Math.max(1, this.maxPillarHeight - this.minPillarHeight + 1)) + this.minPillarHeight;
                            	
                            	for (int i1 = 0; i1 < height && i1 + height < 255; i1++)
                            	{
                            		final BlockPos up = pos.offset(EnumFacing.UP, i1);
                            		
                            		if (world.isAirBlock(up)) {
                            			world.setBlockState(up, this.block, 2);
                            		}
                            	}
                                break;
                        	}
                        	else
                        	{
                            	for (final BlockPredicate adjacent : this.requiredAdjacentBlocks)
                            	{
                            		if (adjacent.test(world.getBlockState(down.east())) || adjacent.test(world.getBlockState(down.west())) || adjacent.test(world.getBlockState(down.north())) || adjacent.test(world.getBlockState(down.south())))
                            		{
                                    	int height = rand.nextInt(Math.max(1, this.maxPillarHeight - this.minPillarHeight + 1)) + this.minPillarHeight;
                                    	
                                    	for (int i1 = 0; i1 < height && i1 + height < 255; i1++)
                                    	{
                                    		final BlockPos up = pos.offset(EnumFacing.UP, i1);
                                    		
                                    		if (world.isAirBlock(up)) {
                                    			world.setBlockState(up, this.block, 2);
                                    		}
                                    	}
                                        break;
                            		}
                            	}
                        	}
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
