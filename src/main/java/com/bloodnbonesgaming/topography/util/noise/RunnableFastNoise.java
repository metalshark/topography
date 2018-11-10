package com.bloodnbonesgaming.topography.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableFastNoise implements Runnable{
	
	final FastNoise noise = new FastNoise();
	
	final int startX;
	final int startY;
	final int startZ;
//	final int spacingX;
//	final int spacingY;
//	final int spacingZ;
	final int height;

	public RunnableFastNoise(final long seed, final double[] array, final int height, final int startX, final int startY, final int startZ)
	{
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        this.noise.SetSeed((int) seed);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
//        this.spacingX = spacingX;
//        this.spacingY = spacingY;
//        this.spacingZ = spacingZ;
        this.height = height;
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 9; x++)
		{
			for (int z = 0; z < 9; z++)
			{
				final double value = this.noise.GetNoise(x * 2 + this.startX, this.height * 2 + this.startY, z * 2 + this.startZ);
				
				RunnableFastNoise.addToArray(value, (x * 9 + z) * 129 + this.height);
			}
		}
		
//		for (int x = 0; x < 16; x++)
//        {
//            final int realX = x + chunkX * 16;
//            
//            for (int z = 0; z < 16; z++)
//            {
//                final int realZ = z + chunkZ * 16;
//                
//                for (int y = this.startY; y < this.startY + 16; y++)
//                {
//                    final double value = this.noise.GetNoise(realX, y, realZ);
//                    
//                    double scale = 0;
//                    
//                    if (this.closeTop)
//                    {
//                        if (y >= 224)
//                        {
//                            scale = (32 - (256 - y)) / 32.0;
//                        }
//                    }
//                    else if (this.openTop)
//                    {
//                        if (y >= 224)
//                        {
//                            scale = -((32 - (256 - y)) / 64.0);
//                        }
//                    }
//                    if (value + scale > -0.15)
//                    {
//                    	CellNoiseGenerator.setBlock(x, y, z, primer, state);
//                    }
//                    else
//                    {
//                        continue;
//                    }
//                }
//            }
//        }
	}
	
	private static double[] array = null;
	
	public static synchronized void addArray(final double[] array, final int arrayIndex)
	{
//		for (int x = 0; x < )
	}
	
	public static synchronized void addToArray(final double value, final int arrayIndex)
	{
		RunnableFastNoise.array[arrayIndex] = value;
	}
	
	public static void getNoise(final double[] array, final long seed, final int startX, final int startY, final int startZ)
	{
		RunnableFastNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		
		for (int y = 0; y < 129; y++)
		{
			callables.add(Executors.callable(new RunnableFastNoise(seed, null, y, startX, startY, startZ)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
