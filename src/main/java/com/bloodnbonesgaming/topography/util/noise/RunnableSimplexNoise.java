package com.bloodnbonesgaming.topography.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableSimplexNoise implements Runnable{
	
    protected OpenSimplexNoiseGeneratorOctaves noise;
	
	final int startX;
	final int startY;
	final int startZ;
	final int height;

	public RunnableSimplexNoise(final long seed, final double[] array, final int height, final OpenSimplexNoiseGeneratorOctaves noise, final int startX, final int startY, final int startZ)
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
//				float skew = (float) (this.skewNoise.eval((x + xI * xCoordinateScale) / 32.0, (y + yI * yCoordinateScale) / 32.0, (z + zI * zCoordinateScale) / 32.0, 3, 0.5) * 16);
				final double value = this.noise.eval((x * 2 + this.startX) / 32.0, (this.height * 2 + this.startY) / 32.0, (z * 2 + this.startZ) / 32.0, 3, 0.5);
				
				RunnableSimplexNoise.addToArray(value, (x * 9 + z) * 129 + this.height);
			}
		}
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double value, final int arrayIndex)
	{
		RunnableSimplexNoise.array[arrayIndex] = value;
	}
	
	public static void getNoise(final double[] array, final long seed, final int startX, final int startY, final int startZ)
	{
		RunnableSimplexNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();

		OpenSimplexNoiseGeneratorOctaves noise = new OpenSimplexNoiseGeneratorOctaves(seed);
		
		for (int y = 0; y < 129; y++)
		{
			callables.add(Executors.callable(new RunnableSimplexNoise(seed, null, y, noise, startX, startY, startZ)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
