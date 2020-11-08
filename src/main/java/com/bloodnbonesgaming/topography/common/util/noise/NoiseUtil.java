package com.bloodnbonesgaming.topography.common.util.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.util.MathUtil;

public abstract class NoiseUtil {
	public static class Simplex {
		public static class Five_ThirtyThree {

			private static final double[] smallArray = new double[5 * 33 * 5];
			
			private class GenLayer implements Runnable {
				
				private final OpenSimplexNoiseGeneratorOctaves gen;
				private final double[] array;
				private final int layer;
				private final int startX;
				private final int startZ;
				private final double horizontalScale;
				private final double verticalScale;
				private final int octaves;
				private final double persistence;
				
				private GenLayer(final OpenSimplexNoiseGeneratorOctaves gen, final double[] array, final int layer, final int startX, final int startZ, final double horizontalScale, final double verticalScale, final int octaves, final double persistence) {
					this.gen = gen;
					this.array = array;
					this.layer = layer;
					this.startX = startX;
					this.startZ = startZ;
					this.horizontalScale = horizontalScale;
					this.verticalScale = verticalScale;
					this.octaves = octaves;
					this.persistence = persistence;
				}

				@Override
				public void run() {
					for (int x = 0; x < 5; x++) {
						for (int z = 0; z < 5; z++) {
							array[(x * 5 + z) * 33 + layer] = gen.eval((startX + x * 4) / horizontalScale, (layer * 8) / verticalScale, (startZ + z * 4) / horizontalScale, octaves, persistence);
						}
					}
				}
			}
			
			public static void generateChunk(final double[] bigArray, final long seed, final int startX, final int startZ, final double horizontalScale, final double verticalScale, final int octaves, final double persistence) {

				final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
				OpenSimplexNoiseGeneratorOctaves gen = new OpenSimplexNoiseGeneratorOctaves(seed);
				
				for (int y = 0; y < 33; y++) {
					callables.add(Executors.callable(new NoiseUtil.Simplex.Five_ThirtyThree().new GenLayer(gen, smallArray, y, startX, startZ, horizontalScale, verticalScale, octaves, persistence)));
				}
				
				try {
					ConfigurationManager.getExecutor().invokeAll(callables);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				MathUtil.interpolate(smallArray, bigArray, 5, 33, 5, 4, 8, 4);
			}
		}
	}
}
