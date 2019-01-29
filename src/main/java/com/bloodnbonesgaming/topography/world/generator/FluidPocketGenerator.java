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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "FluidPocketGenerator", classExplaination = 
"This file is for the FluidPocketGenerator. This generator generates pockets of fluid, similar to lava pockets in the nether.")
public class FluidPocketGenerator implements IGenerator
{
    private final int count;
    private final boolean requiresAir;
    private int minHeight = 4;
    private int maxHeight = 250;
    private final IBlockState block;
    private final List<BlockPredicate> requiredBlocks = new ArrayList<BlockPredicate>();
    
    @ScriptMethodDocumentation(args = "ItemBlockData, int, boolean", usage = "block to generate, pockets per chunk, whether it requires air", notes = "This constructs a FluidPocketGenerator. "
    		+ "The boolean is for if one side of the pocket must have air, not counting the top. This is so the fluid flows prettily.")
	public FluidPocketGenerator(final ItemBlockData data, final int count, final boolean requiresAir) throws Exception
    {
        this.block = data.buildBlockState();
        this.count = count;
        this.requiresAir = requiresAir;
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "required block", notes = "Adds a block the generator is allowed to generate a pocket under. By default can generate under any block.")
	public void addRequiredBlock(final ItemBlockData data) throws Exception
    {
        this.requiredBlocks.add(data.buildBlockPredicate());
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
        final MutableBlockPos pos = new MutableBlockPos();
        
        for (int i = 0; i < this.count; i++)
        {
            pos.setPos(chunkX * 16 + rand.nextInt(16) + 8, rand.nextInt(this.maxHeight - this.minHeight + 1) + this.minHeight, chunkZ * 16 + rand.nextInt(16) + 8);
            
            if (this.isBlockAcceptable(world, pos.up()))
            {
                int solid = 0;
                int air = 0;
                
                if (this.isBlockAcceptable(world, pos.north()))
                    solid++;
                if (this.isBlockAcceptable(world, pos.south()))
                    solid++;
                if (this.isBlockAcceptable(world, pos.east()))
                    solid++;
                if (this.isBlockAcceptable(world, pos.west()))
                    solid++;
                if (this.isBlockAcceptable(world, pos.down()))
                    solid++;
                
                if (!this.requiresAir && solid == 5)
                {
                    world.setBlockState(pos, this.block, 2);
                    world.immediateBlockTick(pos, this.block, rand);
                }
                else if (solid == 4)
                {
                    if (world.isAirBlock(pos.north()))
                        air++;
                    if (world.isAirBlock(pos.south()))
                        air++;
                    if (world.isAirBlock(pos.east()))
                        air++;
                    if (world.isAirBlock(pos.west()))
                        air++;
                    if (world.isAirBlock(pos.down()))
                        air++;
                    
                    if (air == 1)
                    {
                        world.setBlockState(pos, this.block, 2);
                        world.immediateBlockTick(pos, this.block, rand);
                    }
                }
            }
        }
        
//        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
//        
//        for (int k = 0; k < 16; ++k)
//        {
//            blockpos = blockpos.add(rand.nextInt(16) + 8, rand.nextInt(245) + 4, rand.nextInt(16) + 8);
//            
//            if (world.getBlockState(blockpos.up()).getBlock() != Blocks.NETHERRACK)
//            {
//                return;
//            }
//            else if (!world.isAirBlock(blockpos) && world.getBlockState(blockpos).getBlock() != Blocks.NETHERRACK)
//            {
//                return;
//            }
//            else
//            {
//                int i = 0;
//
//                if (world.getBlockState(blockpos.west()).getBlock() == Blocks.NETHERRACK)
//                {
//                    ++i;
//                }
//
//                if (world.getBlockState(blockpos.east()).getBlock() == Blocks.NETHERRACK)
//                {
//                    ++i;
//                }
//
//                if (world.getBlockState(blockpos.north()).getBlock() == Blocks.NETHERRACK)
//                {
//                    ++i;
//                }
//
//                if (world.getBlockState(blockpos.south()).getBlock() == Blocks.NETHERRACK)
//                {
//                    ++i;
//                }
//
//                if (world.getBlockState(blockpos.down()).getBlock() == Blocks.NETHERRACK)
//                {
//                    ++i;
//                }
//
//                int j = 0;
//
//                if (world.isAirBlock(blockpos.west()))
//                {
//                    ++j;
//                }
//
//                if (world.isAirBlock(blockpos.east()))
//                {
//                    ++j;
//                }
//
//                if (world.isAirBlock(blockpos.north()))
//                {
//                    ++j;
//                }
//
//                if (world.isAirBlock(blockpos.south()))
//                {
//                    ++j;
//                }
//
//                if (world.isAirBlock(blockpos.down()))
//                {
//                    ++j;
//                }
//
//                if (i == 4 && j == 1 || i == 5)
//                {
//                    world.setBlockState(blockpos, this.block, 2);
//                    world.immediateBlockTick(blockpos, this.block, rand);
//                }
//            }
//        }
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
