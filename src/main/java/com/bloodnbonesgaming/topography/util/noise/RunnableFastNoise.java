package com.bloodnbonesgaming.topography.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableFastNoise implements Runnable{
	
	final FastNoise noise;
	
	final int startX;
	final int startY;
	final int startZ;
	final int height;
	final int layerCount;
	final double[] layerArray;

	public RunnableFastNoise(final long seed, final double[] array, final FastNoise noise, final int height, final int startX, final int startY, final int startZ, final int layerCount)
	{
		this.noise = noise;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.height = height;
        this.layerCount = layerCount;
        this.layerArray = new double[81 * layerCount];
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 9; x++)
		{
			for (int z = 0; z < 9; z++)
			{
				for (int y = 0; y < this.layerCount; y++)
				{
					final double value = this.noise.GetNoise(x * 2 + this.startX, (this.height + y) * 2 + this.startY, z * 2 + this.startZ);
//					RunnableFastNoise.addToArray(value, (x * 9 + z) * 129 + this.height + y);
					this.layerArray[(x * 9 + z) * this.layerCount + y] = value;
				}
			}
		}
		RunnableFastNoise.addToArray(this.layerArray, this.height, this.layerCount);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double value, final int arrayIndex)
	{
		RunnableFastNoise.array[arrayIndex] = value;
	}
	
	public static synchronized void addToArray(final double[] layerArray, final int layerHeight, final int layerCount)
	{
		for (int x = 0; x < 9; x++)
		{
			for (int z = 0; z < 9; z++)
			{
				for (int y = 0; y < layerCount; y++)
				{
					RunnableFastNoise.array[(x * 9 + z) * 129 + layerHeight + y] = layerArray[(x * 9 + z) * layerCount + y];
				}
			}
		}
	}
	
	public static void getNoise(final double[] array, final long seed, final int startX, final int startY, final int startZ)
	{
		RunnableFastNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		FastNoise noise = new FastNoise();
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        noise.SetSeed((int) seed);
		
		for (int y = 0; y < 129; y+=10)
		{
			if (y + 10 < 129)
			{
				callables.add(Executors.callable(new RunnableFastNoise(seed, null, noise, y, startX, startY, startZ, 10)));
			}
			else
			{
				callables.add(Executors.callable(new RunnableFastNoise(seed, null, noise, y, startX, startY, startZ, 129-y)));
			}
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
