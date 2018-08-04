package com.bloodnbonesgaming.topography.world.generator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class CellInterpolationTestGenerator implements IGenerator
{
    final FastNoise noise = new FastNoise();
    protected OpenSimplexNoiseGeneratorOctaves skewNoise;
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    IBlockState state = Blocks.AIR.getDefaultState();
    boolean closeTop = false;
    boolean openTop = true;
    
    public CellInterpolationTestGenerator()
    {        
        noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(1);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
    }
    
    public CellInterpolationTestGenerator(final ItemBlockData data) throws Exception
    {
        this();
        this.state = data.buildBlockState();
    }
    
    private final Map<MinMaxBounds, IBlockState> blocks = new LinkedHashMap<MinMaxBounds, IBlockState>();
    
    public void addBlock(final MinMaxBounds bounds, final ItemBlockData block) throws Exception
    {
        this.blocks.put(bounds, block.buildBlockState());
    }
    
    public void closeTop()
    {
        this.closeTop = true;
    }
    
    public void openTop()
    {
        this.openTop = true;
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
//                    float skew = (float) (this.skewNoise.eval((x + xI * xCoordinateScale) / 32.0, (y + yI * yCoordinateScale) / 32.0, (z + zI * zCoordinateScale) / 32.0, 3, 0.5) * 16);

//                    double noise = this.noise.GetNoise((x + xI * xCoordinateScale) + skew, (y + yI * yCoordinateScale) + skew, (z + zI * zCoordinateScale) + skew);
                    double noise = this.noise.GetNoise((x + xI * 4) / 4.0f * 0.064f, (y + yI * 8) / 8.0f * 0.128f, (z + zI * 4) / 4.0f * 0.064f);
                    
                    array[index] = noise;
                }
            }
        }
    }
    
    @Override
    public void generate(final World world, ChunkPrimer primer, int chunkX, int chunkZ)
    {
        this.noise.SetSeed((int) world.getSeed());
        this.skewNoise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
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
                    final double value = this.largeNoiseArray[(x * 16 + z) * 256 + y];
                    
                    double scale = 0;
                    
                    if (this.closeTop)
                    {
                        if (y >= 224)
                        {
                            scale = (32 - (256 - y)) / 32.0;
                        }
                    }
                    else if (this.openTop)
                    {
                        if (y >= 224)
                        {
                            scale = -((32 - (256 - y)) / 64.0);
                        }
                    }
                    
                    {
                        if (value + scale > -0.18)
                        {
//                            float skew = (float) (this.skewNoise.eval(realX / 32.0, y / 32.0, realZ / 32.0, 3, 0.5) * 8);
//
//                            float value2 = noise2.GetNoise(realX + skew, y + skew, realZ + skew) + scale;
                            
//                            if (value2 < -0.85)
                            {
                                IBlockState block = this.state;
                                
//                                for (final Entry<MinMaxBounds, IBlockState> entry : this.blocks.entrySet())
//                                {
//                                    if (entry.getKey().test(value2))
//                                    {
//                                        block = entry.getValue();
//                                        break;
//                                    }
//                                }
                                primer.setBlockState(x, y, z, block);
                            }
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

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {
        // TODO Auto-generated method stub
        
    }

}
