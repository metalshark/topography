package com.bloodnbonesgaming.topography.threadedvanillatest;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class NoiseGeneratorOctavesDoubleTest extends NoiseGeneratorOctaves{

	public NoiseGeneratorOctavesDoubleTest(Random seed, int octavesIn) {
		super(seed, octavesIn);
	}

	public double[] generateNoiseOctaves(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale)
    {
        if (noiseArray == null)
        {
            noiseArray = new double[xSize * ySize * zSize];
        }
        else
        {
            for (int i = 0; i < noiseArray.length; ++i)
            {
                noiseArray[i] = 0.0D;
            }
        }

        double d3 = 0.001953125D;

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
            this.generatorCollection[j].populateNoiseArray(noiseArray, d0, d1, d2, xSize, ySize, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
            d3 /= 2D;
        }

        return noiseArray;
    }
}
