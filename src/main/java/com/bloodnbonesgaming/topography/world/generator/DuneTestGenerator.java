package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;
import com.bloodnbonesgaming.topography.util.noise.FastNoise.FractalType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class DuneTestGenerator implements IGenerator {
	
	final FastNoise noise = new FastNoise();
	OpenSimplexNoiseGeneratorOctaves simplex;
	private IBlockState state = Blocks.SAND.getDefaultState();
	
	public DuneTestGenerator(final ItemBlockData data) throws Exception
	{
		this.state = data.buildBlockState();
	}

	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		noise.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        noise.SetFrequency(0.0035f);
        noise.SetFractalOctaves(2);
        noise.SetFractalType(FractalType.RigidMulti);
        noise.SetSeed((int) world.getSeed());
        noise.SetFractalGain(0.25F);
        
        simplex = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
        
        for (int x = 0; x < 16; x++)
        {
        	for (int z = 0; z < 16; z++)
        	{
        		
        		for (int y = 0; y < 128; y++)
        		{
        			final float skew = (float) simplex.eval(x + chunkX * 16, y, z + chunkZ * 16, 64, 4, 0.5);

            		final float value = (noise.GetNoise(x + chunkX * 16 + skew * 32, z + chunkZ * 16 + skew * 32) + 1) / 2;
        			
            		if (value * 64 >= y)
            		{
            			primer.setBlockState(x, y, z, this.state);
            		}
        		}
        	}
        }
	}

	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {
		// TODO Auto-generated method stub

	}

	@Override
	public GenLayer getLayer(World world, GenLayer parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
