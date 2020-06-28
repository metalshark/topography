package com.bloodnbonesgaming.topography.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

public class RunnableSimplexNoise1x1 implements Runnable{
	
    protected OpenSimplexNoiseGeneratorOctaves noise;
	
	final int startX;
	final int startY;
	final int startZ;
	final int octaves;
	final double persistence;
	final double coordinateScale;
	final double[] layerArray;

	public RunnableSimplexNoise1x1( final OpenSimplexNoiseGeneratorOctaves noise, final int startX, final int startY, final int startZ, final double coordinateScale, final int octaves, final double persistence)
	{
		this.noise = noise;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.coordinateScale = coordinateScale;
        this.octaves = octaves;
        this.persistence = persistence;
        this.layerArray = new double[16 * 16 * 8];
	}
	
	@Override
	public void run() {
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 8; y++) {
					//int index = (xI * arraySizeX + zI) * arraySizeY + yI;
					this.layerArray[(x * 16 + z) * 8 + y] = this.noise.eval((startX + x) / coordinateScale, (startY + y) / coordinateScale, (startZ + z) / coordinateScale, octaves, persistence);
				}
			}
		}
		RunnableSimplexNoise1x1.addToArray(this.layerArray, this.startY);
	}
	
	private static double[] array = null;
	
	public static synchronized void addToArray(final double[] layerArray, final int startY)
	{
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 8; y++) {
					RunnableSimplexNoise1x1.array[(x * 16 + z) * 256 + startY + y] = layerArray[(x * 16 + z) * 8 + y];
				}
			}
		}
	}
	
	public static void getNoise(final double[] array, final OpenSimplexNoiseGeneratorOctaves generator, final int firstY, final int lastY, final int startX, final int startZ, final double coordinateScale, final int octaves, final double persistence)
	{
		RunnableSimplexNoise1x1.array = array;
		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		
		//Generates noise 
		for (int y = firstY / 8 * 8; y < lastY; y += 8) {
			callables.add(Executors.callable(new RunnableSimplexNoise1x1(generator, startX, y, startZ, coordinateScale, octaves, persistence)));
		}
		
		try {
			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}