package com.bloodnbonesgaming.topography.util;

public class InterpolationTest {
	
	public static void interpolate(final double[] smallArray, final double[] largeArray, final int horizontalSize, final int verticalSize, final int horizontalSpacing, final int verticalSpacing)
    {
        int realXSize = horizontalSize - 1;
        int realZSize = horizontalSize - 1;
        int realYSize = verticalSize - 1;
        int largeHeight = realYSize * verticalSpacing;
        int width = realXSize * horizontalSpacing;
        final double horizontalMult = 1.0D / horizontalSpacing;
        final double verticalMult = 1.0D / verticalSpacing;
        
        for (int i = 0; i < realXSize; ++i)
        {
            int j = i * horizontalSize;
            int k = (i + 1) * horizontalSize;

            for (int l = 0; l < realZSize; ++l)
            {
                int i1 = (j + l) * verticalSize;
                int j1 = (j + l + 1) * verticalSize;
                int k1 = (k + l) * verticalSize;
                int l1 = (k + l + 1) * verticalSize;

                for (int i2 = 0; i2 < realYSize; ++i2)
                {
                    double d1 = smallArray[i1 + i2];
                    double d2 = smallArray[j1 + i2];
                    double d3 = smallArray[k1 + i2];
                    double d4 = smallArray[l1 + i2];
                    double d5 = (smallArray[i1 + i2 + 1] - d1) * verticalMult;
                    double d6 = (smallArray[j1 + i2 + 1] - d2) * verticalMult;
                    double d7 = (smallArray[k1 + i2 + 1] - d3) * verticalMult;
                    double d8 = (smallArray[l1 + i2 + 1] - d4) * verticalMult;

                    for (int j2 = 0; j2 < verticalSpacing; ++j2)
                    {
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * horizontalMult;
                        double d13 = (d4 - d2) * horizontalMult;

                        for (int k2 = 0; k2 < horizontalSpacing; ++k2)
                        {
                            double d16 = (d11 - d10) * horizontalMult;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < horizontalSpacing; ++l2)
                            {
                                lvt_45_1_ += d16;
                                
                                int x = i * horizontalSpacing + k2;
                                int y = i2 * verticalSpacing + j2;
                                int z = l * horizontalSpacing + l2;
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
