package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableVanillaNoise implements Runnable{
	
    protected VanillaNoiseWrapper noise;
	
	final int startX;
	final int startY;
	final int startZ;
	final int height;
	final int xSize;
	final int ySize;
	final int zSize;
	final double xScale;
	final double yScale;
	final double zScale;

	public RunnableVanillaNoise(final VanillaNoiseWrapper octaves, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, int height) 
	{
		this.noise = octaves;
        this.startX = xOffset;
        this.startY = yOffset;
        this.startZ = zOffset;
        this.height = height;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
	}
	
	@Override
	public void run() {
		RunnableVanillaNoise.addToArray(this.noise.generateNoise(null, startX, startY, startZ, this.xSize, this.zSize, this.xScale, this.yScale, this.zScale, this.height), this.height, this.xSize, this.ySize, this.zSize);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double[] layerArray, final int layerHeight, final int xSize, final int ySize, final int zSize)
	{
		for (int x = 0; x < xSize; x++)
		{
			for (int z = 0; z < zSize; z++)
			{
				Topography.instance.getLog().info(((x * xSize + z) * ySize + layerHeight) + " " + x + "/" + layerHeight + "/" + z + " " + layerArray[x * xSize + z]);
				RunnableVanillaNoise.array[(x * xSize + z) * ySize + layerHeight] = layerArray[x * xSize + z];
			}
		}
	}
	
	public static double[] getNoise(final VanillaNoiseWrapper octaves, double[] array, int xOffset, int yOffset, int zOffset, int xSize,
			int ySize, int zSize, double xScale, double yScale, double zScale) {
		
		if (array == null)
        {
			array = new double[xSize * ySize * zSize];
        }
//        else
//        {
//            for (int i = 0; i < array.length; ++i)
//            {
//            	array[i] = 0.0D;
//            }
//        }
		
//		final long time = System.nanoTime();
		
		RunnableVanillaNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		
		for (int y = 0; y < ySize; y++)
		{
			callables.add(Executors.callable(new RunnableVanillaNoise(octaves, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale, y)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("Noise: " + ((System.nanoTime() - time) / 1000000000D));
		
		return RunnableVanillaNoise.array;
	}
}