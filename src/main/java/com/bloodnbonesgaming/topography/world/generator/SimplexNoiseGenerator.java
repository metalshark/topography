package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class SimplexNoiseGenerator implements IGenerator {
	
	private OpenSimplexNoiseGeneratorOctaves noise;
	private double xScale = 128;
	private double yScale = 32;
	private double zScale = 128;
	private int octaves = 3;
	private double persistence = 0.5;
	private double cutoff = 0.5;
	private IBlockState state;
	private int minHeight = 0;//Inclusive
	private int maxHeight = 256;//Exclusive
	
	
	public SimplexNoiseGenerator(final ItemBlockData data) throws Exception
	{
		this.state = data.buildBlockState();
	}
	
	public void setScale(final double xScale, final double yScale, final double zScale)
	{
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
	}
	
	public void setOctaves(final int octaves)
	{
		this.octaves = octaves;
	}
	
	public void setPersistence(final double persistence)
	{
		this.persistence = persistence;
	}
	
	public void setCutoff(final double cutoff)
	{
		this.cutoff = cutoff;
	}
	
	public void setHeight(final int minHeight, final int maxHeight)
	{
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		
		if (this.noise == null)
		{
			this.noise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
		}
		
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for(int y = this.minHeight; y < this.maxHeight; y++)
				{
					double value = this.noise.eval((chunkX * 16 + x) / this.xScale, y / this.yScale, (chunkZ * 16 + z) / this.zScale, this.octaves, this.persistence);
					
					if (y < 20)
					{
						value += (20 - y) * 0.05;
					}
					else if (maxHeight - y < 20)
					{
						value += (20 - (maxHeight - y)) * 0.05;
					}
					if (value > this.cutoff)
					{
						primer.setBlockState(x, y, z, this.state);
					}
				}
			}
		}
	}
}
