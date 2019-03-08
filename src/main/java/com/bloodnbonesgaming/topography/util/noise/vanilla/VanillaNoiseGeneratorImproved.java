package com.bloodnbonesgaming.topography.util.noise.vanilla;

import java.util.Random;

import net.minecraft.world.gen.NoiseGeneratorImproved;

public class VanillaNoiseGeneratorImproved extends NoiseGeneratorImproved {
	
	
	public VanillaNoiseGeneratorImproved(Random seed) {
		super(seed);
	}

	@Override
	public void populateNoiseArray(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale)
    {
		int i = 0;
        double d0 = 1.0D / noiseScale;
        int k = -1;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        int i2 = 0;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 0.0D;
        double d4 = 0.0D;

        for (int l2 = 0; l2 < xSize; ++l2)
        {
            double d5 = xOffset + (double)l2 * xScale + this.xCoord;
            int i3 = (int)d5;

            if (d5 < (double)i3)
            {
                --i3;
            }

            int j3 = i3 & 255;
            d5 = d5 - (double)i3;
            double d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);

            for (int k3 = 0; k3 < zSize; ++k3)
            {
                double d7 = zOffset + (double)k3 * zScale + this.zCoord;
                int l3 = (int)d7;

                if (d7 < (double)l3)
                {
                    --l3;
                }

                int i4 = l3 & 255;
                d7 = d7 - (double)l3;
                double d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);

                for (int j4 = 0; j4 < ySize; ++j4)
                {
                    double d9 = yOffset + (double)j4 * yScale + this.yCoord;
                    int k4 = (int)d9;

                    if (d9 < (double)k4)
                    {
                        --k4;
                    }

                    int l4 = k4 & 255;
                    d9 = d9 - (double)k4;
                    double d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);

                    if (j4 == 0 || l4 != k)
                    {
                        k = l4;
                        l = this.permutations[j3] + l4;
                        i1 = this.permutations[l] + i4;
                        j1 = this.permutations[l + 1] + i4;
                        k1 = this.permutations[j3 + 1] + l4;
                        l1 = this.permutations[k1] + i4;
                        i2 = this.permutations[k1 + 1] + i4;
                        d1 = this.lerp(d6, this.grad(this.permutations[i1], d5, d9, d7), this.grad(this.permutations[l1], d5 - 1.0D, d9, d7));
                        d2 = this.lerp(d6, this.grad(this.permutations[j1], d5, d9 - 1.0D, d7), this.grad(this.permutations[i2], d5 - 1.0D, d9 - 1.0D, d7));
                        d3 = this.lerp(d6, this.grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0D), this.grad(this.permutations[l1 + 1], d5 - 1.0D, d9, d7 - 1.0D));
                        d4 = this.lerp(d6, this.grad(this.permutations[j1 + 1], d5, d9 - 1.0D, d7 - 1.0D), this.grad(this.permutations[i2 + 1], d5 - 1.0D, d9 - 1.0D, d7 - 1.0D));
                    }

                    double d11 = this.lerp(d10, d1, d2);
                    double d12 = this.lerp(d10, d3, d4);
                    double d13 = this.lerp(d8, d11, d12);
                    int j7 = i++;
                    noiseArray[j7] += d13 * d0;
                }
            }
        }
    }
}
