package com.bloodnbonesgaming.topography.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableSimplexSkewedCellNoise implements Runnable{
	
	final FastNoise noise;
	final protected OpenSimplexNoiseGeneratorOctaves simplex;
	
	final int startX;
	final int startY;
	final int startZ;
	final int height;
	final double[] layerArray;

	public RunnableSimplexSkewedCellNoise(final long seed, final FastNoise noise, final OpenSimplexNoiseGeneratorOctaves simplex, final int height, final int startX, final int startY, final int startZ)
	{
		this.noise = noise;
		this.simplex = simplex;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.height = height;
        this.layerArray = new double[81];
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 9; x++)
		{
			for (int z = 0; z < 9; z++)
			{
				final float skew = (float) (this.simplex.eval((x * 2 + this.startX) / 32.0, (this.height * 2 + this.startY) / 32.0, (z * 2 + this.startZ) / 32.0, 3, 0.5) * 16);
				this.layerArray[x * 9 + z] = this.noise.GetNoise(x * 2 + this.startX + skew, this.height * 2 + this.startY + skew, z * 2 + this.startZ + skew);
			}
		}
		RunnableSimplexSkewedCellNoise.addToArray(this.layerArray, this.height);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double value, final int arrayIndex)
	{
		RunnableSimplexSkewedCellNoise.array[arrayIndex] = value;
	}
	
	public static synchronized void addToArray(final double[] layerArray, final int layerHeight)
	{
		for (int x = 0; x < 9; x++)
		{
			for (int z = 0; z < 9; z++)
			{
				RunnableSimplexSkewedCellNoise.array[(x * 9 + z) * 129 + layerHeight] = layerArray[x * 9 + z];
			}
		}
	}
	
	public static void getNoise(final double[] array, final long seed, final int startX, final int startY, final int startZ)
	{
		RunnableSimplexSkewedCellNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		FastNoise noise = new FastNoise();
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        noise.SetSeed((int) seed);
        
		OpenSimplexNoiseGeneratorOctaves simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
		
		for (int y = 0; y < 129; y++)
		{
			callables.add(Executors.callable(new RunnableSimplexSkewedCellNoise(seed, noise, simplex, y, startX, startY, startZ)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
