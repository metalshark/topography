package com.bloodnbonesgaming.topography.common.world.gen;

import com.bloodnbonesgaming.topography.common.util.MathUtil;
import com.bloodnbonesgaming.topography.common.util.noise.RunnableSimplexSkewedCellNoise;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

public class CellNoiseGenerator implements IGenerator
{
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    BlockState state = Blocks.AIR.getDefaultState();
    boolean invert = false;
    boolean closeTop = false;
    boolean openTop = true;
    private float frequency = 0.005f;
    private double cutoff = -0.15;
    
	public CellNoiseGenerator()
    {
    }
	
	public CellNoiseGenerator(final BlockState state)
    {
		this();
		this.state = state;
    }
    
//	public CellNoiseGenerator(final ItemBlockData data) throws Exception
//    {
//        this();
//        this.state = data.buildBlockState();
//    }
    
//    private final Map<MinMaxBounds, BlockState> blocks = new LinkedHashMap<MinMaxBounds, BlockState>();
//    
//    public void addBlock(final MinMaxBounds bounds, final ItemBlockData block) throws Exception
//    {
//        this.blocks.put(bounds, block.buildBlockState());
//    }
    
	public void closeTop()
    {
        this.closeTop = true;
    }
    
	public void openTop()
    {
        this.openTop = true;
    }
    
	public void setCellFrequency(final float frequency)
    {
    	this.frequency = frequency;
    }
    
	public void setCutoff(final double cutoff)
    {
    	this.cutoff = cutoff;
    }
    
	@Override
    public void generate(final IWorld world, IChunk chunk, SharedSeedRandom rand, long seed)
    {
    	ChunkPos pos = chunk.getPos();
    	Mutable mutable = new Mutable();

//    	RunnableSimplexNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16);
//    	RunnableFastNoise.getNoise(this.smallNoiseArray, world.getSeed(), chunkX * 16, 0, chunkZ * 16);
    	RunnableSimplexSkewedCellNoise.getNoise(this.smallNoiseArray, seed, pos.x * 16, 0, pos.z * 16, 5, 33, 4, 8, this.frequency);
//    	InterpolationTest.interpolate(this.smallNoiseArray, this.largeNoiseArray, 4, 52, 5, 5);
//    	long start = System.nanoTime();
    	MathUtil.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
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
                        if (value + scale > this.cutoff)
                        {
                            {
                                BlockState block = this.state;
                                
                                mutable.setPos(x, y, z);
                                chunk.setBlockState(mutable, block, false);
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
}
