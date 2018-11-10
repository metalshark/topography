package com.bloodnbonesgaming.topography.world.generator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.util.InterpolationTest;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;
import com.bloodnbonesgaming.topography.util.noise.RunnableFastNoise;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class CellNoiseGenerator implements IGenerator
{
    final FastNoise noise = new FastNoise();
    protected OpenSimplexNoiseGeneratorOctaves skewNoise;
    double[] smallNoiseArray = new double[10449];
    double[] largeNoiseArray = new double[65536];
    IBlockState state = Blocks.AIR.getDefaultState();
    boolean invert = false;
    boolean closeTop = false;
    boolean openTop = true;
    final ExecutorService service = Executors.newFixedThreadPool(4);
    
    public CellNoiseGenerator()
    {        
        noise.SetNoiseType(FastNoise.NoiseType.Cellular);
        noise.SetFrequency(0.005f);
        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());
        
    }
    
    public CellNoiseGenerator(final ItemBlockData data) throws Exception
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
                    float skew = (float) (this.skewNoise.eval((x + xI * xCoordinateScale) / 32.0, (y + yI * yCoordinateScale) / 32.0, (z + zI * zCoordinateScale) / 32.0, 3, 0.5) * 16);

                    double noise = this.noise.GetNoise((x + xI * xCoordinateScale) + skew, (y + yI * yCoordinateScale) + skew, (z + zI * zCoordinateScale) + skew);
                    
                    array[index] = noise;
                }
            }
        }
    }
    
    @Override
    public void generate(final World world, ChunkPrimer primer, int chunkX, int chunkZ)
    {
    	long start = System.currentTimeMillis();
    	
//    	List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
//    	
//    	for (int i = 0; i < 16; i++)
//    	{
//    		callables.add(Executors.callable(new ThreadedFastNoise(primer, state, world.getSeed(), i * 16, chunkX, chunkZ, openTop, closeTop)));
//    	}
//    	try {
//			this.service.invokeAll(callables);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
    	
    	RunnableFastNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16);
    	InterpolationTest.interpolate(this.smallNoiseArray, this.largeNoiseArray, 9, 129, 9, 2, 2, 2);
    	System.out.println(System.currentTimeMillis() - start);
        
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
                        if (value + scale > -0.15)
                        {
                            {
                                IBlockState block = this.state;
                                
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
    
    public void invert()
    {
        this.invert = true;
    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {        
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }
    
    public static synchronized void setBlock(final int x, final int y, final int z, final ChunkPrimer primer, final IBlockState state)
    {
    	primer.setBlockState(x, y, z, state);
    }
}
