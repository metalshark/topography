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
	final int horizontalSize;
	final int verticalSize;
	final int horizontalSpacing;
	final int verticalSpacing;

	public RunnableSimplexSkewedCellNoise(final long seed, final FastNoise noise, final OpenSimplexNoiseGeneratorOctaves simplex, final int height, final int startX, final int startY, final int startZ, final int horizontalSize, final int verticalSize, final int horizontalSpacing, final int verticalSpacing)
	{
		this.noise = noise;
		this.simplex = simplex;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.height = height;
        this.layerArray = new double[horizontalSize * horizontalSize];
        this.horizontalSize = horizontalSize;
        this.verticalSize = verticalSize;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < this.horizontalSize; x++)
		{
			for (int z = 0; z < this.horizontalSize; z++)
			{
				final float skew = (float) (this.simplex.eval((x * this.horizontalSpacing + this.startX) / 32.0, (this.height * this.verticalSpacing + this.startY) / 32.0, (z * this.horizontalSpacing + this.startZ) / 32.0, 3, 0.5) * 16);
				this.layerArray[x * this.horizontalSize + z] = this.noise.GetNoise(x * this.horizontalSpacing + this.startX + skew, this.height * this.verticalSpacing + this.startY + skew, z * this.horizontalSpacing + this.startZ + skew);
			}
		}
		RunnableSimplexSkewedCellNoise.addToArray(this.layerArray, this.height, this.horizontalSize, this.verticalSize);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double[] layerArray, final int layerHeight, final int horizontalSize, final int verticalSize)
	{
		for (int x = 0; x < horizontalSize; x++)
		{
			for (int z = 0; z < horizontalSize; z++)
			{
				RunnableSimplexSkewedCellNoise.array[(x * horizontalSize + z) * verticalSize + layerHeight] = layerArray[x * horizontalSize + z];
			}
		}
	}
	
	public static void getNoise(final double[] array, final long seed, final int startX, final int startY, final int startZ, final int horizontalSize, final int verticalSize, final int horizontalSpacing, final int verticalSpacing, final float frequency)
	{
		RunnableSimplexSkewedCellNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		FastNoise noise = new FastNoise();
		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(frequency);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        noise.SetSeed((int) seed);
        
		OpenSimplexNoiseGeneratorOctaves simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
		
		for (int y = 0; y < verticalSize; y++)
		{
			callables.add(Executors.callable(new RunnableSimplexSkewedCellNoise(seed, noise, simplex, y, startX, startY, startZ, horizontalSize, verticalSize, horizontalSpacing, verticalSpacing)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
