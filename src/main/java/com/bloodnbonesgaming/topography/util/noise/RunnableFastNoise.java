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

	public RunnableFastNoise(final long seed, final double[] array, final FastNoise noise, final int height, final int startX, final int startY, final int startZ)
	{
		this.noise = noise;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
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
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double value, final int arrayIndex)
	{
		RunnableFastNoise.array[arrayIndex] = value;
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
		
		for (int y = 0; y < 129; y++)
		{
			callables.add(Executors.callable(new RunnableFastNoise(seed, null, noise, y, startX, startY, startZ)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
