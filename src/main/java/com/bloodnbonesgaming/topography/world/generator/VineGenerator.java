package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VineGenerator", classExplaination = 
"This file is for the VineGenerator. This generator generates vines. Made of vines. Incredible, isn't it?")
public class VineGenerator implements IGenerator {
	
	private int attemptsPerChunk = 10;
	private int minLength = 3;
	private int maxLength = 20;
	private int minHeight = 0; //Exclusive
	private int maxHeight = 255; //Inclusive
	private IBlockState state = Blocks.VINE.getDefaultState();
	
	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {
		
        for (int i = 0; i < this.attemptsPerChunk; i++)
        {
        	int x = random.nextInt(27) + 3;
        	int z = random.nextInt(27) + 3;
        	
        	MutableBlockPos mutable = new MutableBlockPos(chunkX * 16 + x, 256, chunkZ * 16 + z);
        	EnumFacing current = null;
        	int length = 0;
        	
        	for (int y = this.maxHeight; y > this.minHeight; y--)
        	{
        		mutable.setY(y);
        		
        		if (world.isAirBlock(mutable))
                {
    				final Block block = this.state.getBlock();
    				
        			if (current == null)
        			{
        				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL.facings())
						{
							if (block.canPlaceBlockOnSide(world, mutable, enumfacing)
									&& this.checkSpaceForMinLength(world, mutable)) {
								current = enumfacing;
								length = random.nextInt(Math.max(1, this.maxLength - this.minLength + 1)) + this.minLength - 1;
								IBlockState iblockstate = this.state
										.withProperty(BlockVine.SOUTH, Boolean.valueOf(current == EnumFacing.NORTH))
										.withProperty(BlockVine.WEST, Boolean.valueOf(current == EnumFacing.EAST))
										.withProperty(BlockVine.NORTH, Boolean.valueOf(current == EnumFacing.SOUTH))
										.withProperty(BlockVine.EAST, Boolean.valueOf(current == EnumFacing.WEST));

								world.setBlockState(mutable, iblockstate, 2);
								break;
							}
                        }
        			}
        			else if (length > 0)
        			{
						IBlockState iblockstate = this.state
								.withProperty(BlockVine.SOUTH, Boolean.valueOf(current == EnumFacing.NORTH))
								.withProperty(BlockVine.WEST, Boolean.valueOf(current == EnumFacing.EAST))
								.withProperty(BlockVine.NORTH, Boolean.valueOf(current == EnumFacing.SOUTH))
								.withProperty(BlockVine.EAST, Boolean.valueOf(current == EnumFacing.WEST));
							
						world.setBlockState(mutable, iblockstate, 2);
						length--;
        			}
        			else
        			{
            			current = null;
        			}
                }
        		else
        		{
        			current = null;
        		}
        	}
        }
	}
	
	private boolean checkSpaceForMinLength(final World world, final BlockPos top)
	{
		BlockPos pos = new MutableBlockPos(top);
		
		for (int i = 0; i < this.minLength; i++)
		{
			if (!world.isAirBlock(pos))
			{
				return false;
			}
			pos = pos.down();
		}
		return true;
	}
	
	@ScriptMethodDocumentation(args = "ItemBlockData", usage = "block to generate", notes = "Sets the block to generate vines out of. Must extend vanilla vines.")
	public void setBlock(final ItemBlockData data) throws Exception
	{
		final IBlockState state = data.buildBlockState();
		
		if (state.getBlock() instanceof BlockVine)
		{
			this.state = state;
		}
		else
		{
			throw new Exception("The vine generator can only use blocks which extend vanilla vines.");
		}
	}
	
	@ScriptMethodDocumentation(args = "int, int", usage = "min length, max length", notes = "Sets the minimum and maximum length of the vines. Vines will not generate if there is insufficient space for minimum length. Default is 3 and 20.")
	public void setLength(final int min, final int max)
	{
		this.minLength = min > 0 ? min : 1;
		this.maxLength = max;
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "attempt count", notes = "Sets the number of positions on the horizontal plane to attempt to generate vines per chunk. Default is 3 and 20.")
	public void setAttemptsPerChunk(final int count)
	{
		this.attemptsPerChunk = count;
	}
	
	@ScriptMethodDocumentation(args = "int, int", usage = "min height(exclusive), max height(inclusive)", notes = "Sets the min and max heights the top of vines will generate. Vines can hang below as long as their top is within the range. Default is 0 and 255.")
	public void setHeightRange(final int min, final int max)
	{
		this.minHeight = min;
		this.maxHeight = max <= 255 ? max : 255;
	}
	
	
}
