package com.bloodnbonesgaming.topography.world.generator;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

public class CellNoiseGenerator implements IGenerator
{
    final FastNoise noise = new FastNoise();
    final FastNoise noise2 = new FastNoise();
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    final IBlockState state;
    
    public CellNoiseGenerator(final ItemBlockData data) throws Exception
    {
        this.state = data.buildBlockState();
        
        noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance2Sub);

        noise2.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise2.SetFrequency(0.01f);
        noise2.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise2.SetCellularReturnType(FastNoise.CellularReturnType.Distance2Sub);
    }
    
    private void generateNoise(final double[] array, final int arraySizeX, final int arraySizeY, final int arraySizeZ, final int x, final int y, final int z, final int xCoordinateScale, final int yCoordinateScale, final int zCoordinateScale)
    {
        for (int xI = 0; xI < arraySizeX; xI++)
        {
            for (int zI = 0; zI < arraySizeZ; zI++)
            {
                for (int yI = 0; yI < arraySizeY; yI++)
                {
                    int index = (xI * arraySizeX + zI) * arraySizeY + yI;
                    double noise = this.noise.GetNoise(x + xI * xCoordinateScale, y + yI * yCoordinateScale, z + zI * zCoordinateScale);
                    
                    array[index] = noise;
                }
            }
        }
    }
    
    @Override
    public void generate(ChunkPrimer primer, int chunkX, int chunkZ)
    {
        this.generateNoise(smallNoiseArray, 5, 33, 5, chunkX * 16, 0, chunkZ * 16, 4, 8, 4);
        NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
        
        for (int x = 0; x < 16; x++)
        {
            final int realX = x + chunkX * 16;
            
            for (int z = 0; z < 16; z++)
            {
                final int realZ = z + chunkZ * 16;
                
                for (int y = 0; y < 256; y++)
                {
//                    float value = noise.GetNoise(realX, y, realZ);
                    final double value = this.largeNoiseArray[(x * 16 + z) * 256 + y];
                    
                    if (value < -0.85 && noise2.GetNoise(realX, y, realZ) < -0.85)
                    {
                        primer.setBlockState(x, y, z, state);
                    }
                    else
                    {
                        continue;
                    }
                }
            }
        }
    }
}
