package com.bloodnbonesgaming.topography.world.generator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.util.InterpolationTest;
import com.bloodnbonesgaming.topography.util.noise.FastNoise;
import com.bloodnbonesgaming.topography.util.noise.RunnableFastNoise;
import com.bloodnbonesgaming.topography.util.noise.RunnableSimplexNoise;
import com.bloodnbonesgaming.topography.util.noise.RunnableSimplexSkewedCellNoise;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

public class CellNoiseGenerator implements IGenerator
{
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    IBlockState state = Blocks.AIR.getDefaultState();
    boolean invert = false;
    boolean closeTop = false;
    boolean openTop = true;
    
    public CellNoiseGenerator()
    {
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
    
    @Override
    public void generate(final World world, ChunkPrimer primer, int chunkX, int chunkZ)
    {

//    	RunnableSimplexNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16);
//    	RunnableFastNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16);
    	RunnableSimplexSkewedCellNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16, 5, 33, 4, 8);
//    	InterpolationTest.interpolate(this.smallNoiseArray, this.largeNoiseArray, 4, 52, 5, 5);
//    	long start = System.nanoTime();
    	NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
//    	System.out.println(System.nanoTime() - start);
        
        for (int x = 0; x < 16; x++)
        {            
            for (int z = 0; z < 16; z++)
            {                
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
}
