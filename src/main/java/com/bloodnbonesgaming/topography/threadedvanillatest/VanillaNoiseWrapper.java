package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.NoiseGeneratorImproved;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class VanillaNoiseWrapper extends NoiseGeneratorOctaves {

	public VanillaNoiseWrapper(Random seed, int octavesIn) {
		super(new Random(), octavesIn);
		
		this.octaves = octavesIn;
        this.generatorCollection = new NoiseGeneratorImproved[octavesIn];

        for (int i = 0; i < octavesIn; ++i)
        {
            this.generatorCollection[i] = new VanillaNoiseGeneratorImproved(seed);
        }
	}

	@Override
	public double[] generateNoiseOctaves(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize,
			int ySize, int zSize, double xScale, double yScale, double zScale) {
		return RunnableVanillaNoise.getNoise(this, noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale);
	}
	
	public double[] generateVanillaNoise(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize,
			int ySize, int zSize, double xScale, double yScale, double zScale) {
		return super.generateNoiseOctaves(noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale, yScale, zScale);
	}
	
	public double[] generateNoise(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize, int zSize, double xScale, double yScale, double zScale, int yIndex) {
		if (noiseArray == null)
        {
            noiseArray = new double[xSize * zSize];
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
            ((VanillaNoiseGeneratorImproved)this.generatorCollection[j]).populateNoiseArray(noiseArray, d0, d1 + (double)yIndex * (yScale * d3), d2, xSize, 1, zSize, xScale * d3, yScale * d3, zScale * d3, d3, yIndex);
//            ((VanillaNoiseGeneratorImproved)this.generatorCollection[j]).populateNoiseArray2(noiseArray, d0, d1, d2, xSize, 1, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
            d3 /= 2.0D;
        }

        return noiseArray;
	}
}
