package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.Random;

import com.bloodnbonesgaming.topography.util.noise.FastNoise;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class VanillaNoiseWrapper extends NoiseGeneratorOctaves {
	
	final OpenSimplexOctavesTest noise;
//	final FastNoiseOctaves noise;

	public VanillaNoiseWrapper(long seed, int octavesIn) {
		super(new Random(), octavesIn);

		noise = new OpenSimplexOctavesTest(seed, octavesIn);
//		final FastNoise fast = new FastNoise();
//		fast.SetSeed((int) seed);
//		fast.SetNoiseType(FastNoise.NoiseType.Perlin);
//		noise = new FastNoiseOctaves(fast, octavesIn);
	}

	@Override
	public double[] generateNoiseOctaves(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize,
			int ySize, int zSize, double xScale, double yScale, double zScale) {
		return RunnableSimplexNoiseTest.getNoise(noise, noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale);
//		return RunnableFastNoiseSimplex.getNoise(noise, noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale);
	}
	
	public double[] generateVanillaNoise(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize,
			int ySize, int zSize, double xScale, double yScale, double zScale) {
		return super.generateNoiseOctaves(noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale);
	}
	
	public double[] generateNoise(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, int xPos, int zPos) {
		if (noiseArray == null)
        {
            noiseArray = new double[xSize * ySize * zSize];
        }

        double d3 = 1.0D;

        for (int j = 0; j < this.octaves; ++j)
        {
            double d0 = (double)xOffset * d3 * xScale;
            double d1 = (double)yOffset * d3 * yScale;
            double d2 = (double)zOffset * d3 * zScale;
            long k = MathHelper.lfloor(d0);
            long l = MathHelper.lfloor(d2);
            d0 = d0 - (double)k;
            d2 = d2 - (double)l;
            k = k % 16777216L;
            l = l % 16777216L;
            d0 = d0 + (double)k;
            d2 = d2 + (double)l;
            ((VanillaNoiseGeneratorImproved)this.generatorCollection[j]).populateNoiseArray(noiseArray, d0, d1, d2, xSize, ySize, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
//            ((VanillaNoiseGeneratorImproved)this.generatorCollection[j]).populateNoiseArray(noiseArray, d0 + xPos * xScale, d1, d2 + zPos * zScale, xSize, ySize, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
//            ((VanillaNoiseGeneratorImproved)this.generatorCollection[j]).populateNoiseArray2(noiseArray, d0, d1, d2, xSize, 1, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
            d3 /= 2.0D;
        }

        return noiseArray;
	}
}
