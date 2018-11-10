package com.bloodnbonesgaming.topography.util.noise;

import com.bloodnbonesgaming.topography.world.generator.CellNoiseGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

public class ThreadedFastNoise implements Runnable{
	
	final ChunkPrimer primer;
	final IBlockState state;
	final FastNoise noise = new FastNoise();
	final int startY;
	final int chunkX;
	final int chunkZ;
	final boolean openTop;
	final boolean closeTop;

	public ThreadedFastNoise(final ChunkPrimer primer, final IBlockState state, final long seed, final int startY, final int chunkX, final int chunkZ, final boolean openTop, final boolean closeTop)
	{
		this.primer = primer;
		this.state = state;
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        this.noise.SetSeed((int) seed);
        this.startY = startY;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.openTop = openTop;
        this.closeTop = closeTop;
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 16; x++)
        {
            final int realX = x + chunkX * 16;
            
            for (int z = 0; z < 16; z++)
            {
                final int realZ = z + chunkZ * 16;
                
                for (int y = this.startY; y < this.startY + 16; y++)
                {
                    final double value = this.noise.GetNoise(realX, y, realZ);
                    
                    double scale = 0;
                    
                    if (this.closeTop)
                    {
                        if (y >= 224)
                        {
                            scale = (32 - (256 - y)) / 32.0;
                        }
                    }
                    else if (this.openTop)
                    {
                        if (y >= 224)
                        {
                            scale = -((32 - (256 - y)) / 64.0);
                        }
                    }
                    if (value + scale > -0.15)
                    {
                    	CellNoiseGenerator.setBlock(x, y, z, primer, state);
                    }
                    else
                    {
                        continue;
                    }
                }
            }
        }
	}
}
