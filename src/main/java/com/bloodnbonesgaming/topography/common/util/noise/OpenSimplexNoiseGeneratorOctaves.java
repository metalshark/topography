package com.bloodnbonesgaming.topography.common.util.noise;

public class OpenSimplexNoiseGeneratorOctaves
{
    protected final OpenSimplexNoiseGenerator generator;
    
    public OpenSimplexNoiseGeneratorOctaves(final long seed)
    {
        this.generator = new OpenSimplexNoiseGenerator(seed);
    }
    
    public double eval(final double x, final double y, final double z, final int octaves, final double persistence)
    {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for(int i=0;i<octaves;i++) {
            total += ((this.generator.eval(x * frequency, y * frequency, z * frequency) + 1) / 2) * amplitude;
            
            maxValue += amplitude;
            
            amplitude *= persistence;
            frequency *= 2;
        }
        
        return total/maxValue;
    }
    
    public double eval(final double x, final double y, final double z, final double startingFeatureSize, int octaves, final double persistence)
    {
        return this.eval(x / startingFeatureSize, y / startingFeatureSize, z / startingFeatureSize, octaves, persistence);
    }
    
    public double[] eval(final double[] array, final int arraySizeX, final int arraySizeY, final int arraySizeZ, final double x, final double y, final double z, final int octaves, final double persistence, final int xCoordinateScale, final int yCoordinateScale, final int zCoordinateScale, final double xScale, final double yScale, final double zScale)
    {
        for (int xI = 0; xI < arraySizeX; xI++)
        {
            for (int zI = 0; zI < arraySizeZ; zI++)
            {
                for (int yI = 0; yI < arraySizeY; yI++)
                {
//                    (x * width + z) * height + y
                    int index = (xI * arraySizeX + zI) * arraySizeY + yI;
                    
                    array[index] = this.eval((x + xI * xCoordinateScale) / xScale, yScale == 0 ? 0 : ((y + yI * yCoordinateScale) / yScale), (z + zI * zCoordinateScale) / zScale, octaves, persistence) * 2 - 1;
                }
            }
        }
        return array;
    }
    
    public double eval(final double x, final double z, final int octaves, final double persistence)
    {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for(int i=0;i<octaves;i++) {
            total += ((this.generator.eval(x * frequency, z * frequency) + 1) / 2) * amplitude;
            
            maxValue += amplitude;
            
            amplitude *= persistence;
            frequency *= 2;
        }
        
        return total/maxValue;
    }
}
