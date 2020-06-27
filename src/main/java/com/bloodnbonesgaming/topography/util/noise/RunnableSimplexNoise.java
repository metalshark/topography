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
	final int octaves;
	final double persistence;
	final double coordinateScale;
	final double[] layerArray;

	public RunnableSimplexNoise( final OpenSimplexNoiseGeneratorOctaves noise, final int startX, final int startY, final int startZ, final double coordinateScale, final int octaves, final double persistence)
	{
		this.noise = noise;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.coordinateScale = coordinateScale;
        this.octaves = octaves;
        this.persistence = persistence;
        this.layerArray = new double[5 * 5 * 3];
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				for (int y = 0; y < 3; y++) {
					//int index = (xI * arraySizeX + zI) * arraySizeY + yI;
					this.layerArray[(x * 5 + z) * 3 + y] = this.noise.eval((startX + x * 4) / coordinateScale, ((startY + y) * 8) / coordinateScale, (startZ + z * 4) / coordinateScale, octaves, persistence);
				}
			}
		}
		RunnableSimplexNoise.addToArray(this.layerArray, this.startY);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double[] layerArray, final int startY)
	{
		for (int x = 0; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				for (int y = 0; y < 3; y++) {
					RunnableSimplexNoise.array[(x * 5 + z) * 33 + startY + y] = layerArray[(x * 5 + z) * 3 + y];
				}
			}
		}
	}
	
	public static void getNoise(final double[] array, final OpenSimplexNoiseGeneratorOctaves generator, final int startX, final int startZ, final double coordinateScale, final int octaves, final double persistence)
	{
		RunnableSimplexNoise.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		
		for (int y = 0; y < 33; y += 3) {
			callables.add(Executors.callable(new RunnableSimplexNoise(generator, startX, y, startZ, coordinateScale, octaves, persistence)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}