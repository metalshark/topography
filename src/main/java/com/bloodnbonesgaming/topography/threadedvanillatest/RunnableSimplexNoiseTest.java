package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableSimplexNoiseTest implements Runnable{
	
    protected OpenSimplexOctavesTest noise;
	
	final int startX;
	final int startY;
	final int startZ;
	final int height;
	final double[] layerArray;
	final int xSize;
	final int ySize;
	final int zSize;
	final double xScale;
	final double yScale;
	final double zScale;

	public RunnableSimplexNoiseTest(final int height, final OpenSimplexOctavesTest noise, final int startX, final int startY, final int startZ, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale)
	{
		this.noise = noise;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.height = height;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.layerArray = new double[xSize * zSize];
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < this.xSize; x++)
		{
			for (int z = 0; z < this.zSize; z++)
			{
				this.layerArray[x * this.xSize + z] = this.noise.eval((x + this.startX) / this.xScale, (this.height + this.startY) / this.yScale, (z + this.startZ) / this.zScale, 0.5, false);
			}
		}
		RunnableSimplexNoiseTest.addToArray(this.layerArray, this.height, this.xSize, this.ySize, this.zSize);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double[] layerArray, final int layerHeight, final int xSize, final int ySize, final int zSize)
	{
		for (int x = 0; x < xSize; x++)
		{
			for (int z = 0; z < zSize; z++)
			{
				RunnableSimplexNoiseTest.array[(x * xSize + z) * ySize + layerHeight] = layerArray[x * xSize + z];
			}
		}
	}
	
	public static double[] getNoise(OpenSimplexOctavesTest noise, double[] array, final int startX, final int startY, final int startZ, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale)
	{
		if (array == null)
		{
			array = new double[xSize * ySize * zSize];
		}
		RunnableSimplexNoiseTest.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		
		for (int y = 0; y < ySize; y++)
		{
			callables.add(Executors.callable(new RunnableSimplexNoiseTest(y, noise, startX, startY, startZ, xSize, ySize, zSize, xScale, yScale, zScale)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return RunnableSimplexNoiseTest.array;
	}
}