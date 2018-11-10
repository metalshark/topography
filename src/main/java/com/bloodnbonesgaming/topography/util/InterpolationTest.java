package com.bloodnbonesgaming.topography.util;

public class InterpolationTest {
	
	public static void interpolate(final double[] smallArray, final double[] largeArray, final int xSize, final int ySize, final int zSize, final int xSpacing, final int ySpacing, final int zSpacing)
    {
        int realXSize = xSize - 1;
        int realZSize = zSize - 1;
        int realYSize = ySize - 1;
        int largeHeight = realYSize * ySpacing;
        int width = realXSize * xSpacing;
        
        for (int i = 0; i < realXSize; ++i)
        {
            int j = i * 9;
            int k = (i + 1) * 9;

            for (int l = 0; l < realZSize; ++l)
            {
                int i1 = (j + l) * 129;
                int j1 = (j + l + 1) * 129;
                int k1 = (k + l) * 129;
                int l1 = (k + l + 1) * 129;

                for (int i2 = 0; i2 < realYSize; ++i2)
                {
                    double d0 = 0.125D;
                    double d1 = smallArray[i1 + i2];
                    double d2 = smallArray[j1 + i2];
                    double d3 = smallArray[k1 + i2];
                    double d4 = smallArray[l1 + i2];
                    double d5 = (smallArray[i1 + i2 + 1] - d1) * 0.5D;
                    double d6 = (smallArray[j1 + i2 + 1] - d2) * 0.5D;
                    double d7 = (smallArray[k1 + i2 + 1] - d3) * 0.5D;
                    double d8 = (smallArray[l1 + i2 + 1] - d4) * 0.5D;

                    for (int j2 = 0; j2 < ySpacing; ++j2)
                    {
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.5D;
                        double d13 = (d4 - d2) * 0.5D;

                        for (int k2 = 0; k2 < xSpacing; ++k2)
                        {
                            double d16 = (d11 - d10) * 0.5D;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < zSpacing; ++l2)
                            {
                                lvt_45_1_ += d16;
                                
                                int x = i * 2 + k2;
                                int y = i2 * 2 + j2;
                                int z = l * 2 + l2;
                                int index = (x * width + z) * largeHeight + y;
                                
                                largeArray[index] = lvt_45_1_;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }
}
