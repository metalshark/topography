package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.topography.util.noise.vanilla.VanillaNoiseGeneratorOctaves;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.fluids.BlockFluidBase;

public class BiomeBlockReplacementGenerator implements IGenerator {
	
    public Biome[] biomesForGeneration;
    public NoiseGeneratorPerlin surfaceNoise;
    public double[] depthBuffer = new double[256];
    private final int seaHeight;
    
    public BiomeBlockReplacementGenerator()
    {
    	this(0);
    }
    
    public BiomeBlockReplacementGenerator(final int seaHeight)
    {
    	this.seaHeight = seaHeight;
    }
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		
		final Random rand = new Random(world.getSeed());
        this.surfaceNoise = new NoiseGeneratorPerlin(rand, 4);
        
		this.biomesForGeneration = world.getBiomeProvider().getBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
//        this.generateBiomeTerrain(biomesForGeneration, random, primer, chunkX, chunkZ);
        this.replaceBiomeBlocks(world, random, chunkX, chunkZ, primer, biomesForGeneration);
	}
    
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final IBlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    protected static final IBlockState ICE = Blocks.ICE.getDefaultState();
    protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    
    public void generateBiomeTerrain(Biome[] biomes, Random rand, ChunkPrimer primer, int chunkX, int chunkZ)
    {
    	IBlockState state,topBlock,fillerBlock;
    	
    	for (int x = 0; x < 16; x++)
    	{
    		for (int z = 0; z < 16; z++)
    		{
    			topBlock = biomes[x * 16 + z].topBlock;
    			fillerBlock = biomes[x * 16 + z].fillerBlock;
    			
    			
    			int blocksToPlace = 4;
    			boolean underFluid = false;
    			
    			for (int y = 255; y >= 0; y--)
    			{
    				state = primer.getBlockState((chunkX * 16 + x) & 15, y, (chunkZ * 16 + z) & 15);
    				
    				if (state == AIR)
    				{
    					blocksToPlace = 4;
    					underFluid = false;
    				}
    				else if (state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof BlockFluidBase)
    				{
    					blocksToPlace = 4;
    					underFluid = true;
    				}
    				else
    				{
    					if (blocksToPlace > 3 && !underFluid)
						{
    						primer.setBlockState(x, y, z, topBlock);
    						blocksToPlace--;
						}
						else if (blocksToPlace > 0)
						{
    						primer.setBlockState(x, y, z, fillerBlock);
    						blocksToPlace--;
						}
    					underFluid = false;
    				}
    			}
    		}
    	}
    }
    
    


    public void replaceBiomeBlocks(World world, Random random, int x, int z, ChunkPrimer primer, Biome[] biomesIn)
    {
        double d0 = 0.03125D;
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                Biome biome = biomesIn[j + i * 16];
                this.generateBiomeTerrain(biome, random, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }
    
    public void generateBiomeTerrain(Biome biome, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
    	int i = this.seaHeight;
        IBlockState iblockstate = biome.topBlock;
        IBlockState iblockstate1 = biome.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1)
        {
//            if (j1 <= rand.nextInt(5))
//            {
//                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
//            }
//            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    j = -1;
                }
                else if (!(iblockstate2.getBlock() instanceof BlockFluidBase || iblockstate2.getBlock() instanceof BlockLiquid))
                {
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            iblockstate = biome.topBlock;
                            iblockstate1 = biome.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                        {
                            if (biome.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F)
                            {
                                iblockstate = ICE;
                            }
                            else
                            {
                                iblockstate = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            iblockstate = AIR;
                            iblockstate1 = STONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1)
                        {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                        }
                    }
                }
                else //If it is fluid
                {
                	j = k;//Skip topblock and go straight to filler
                }
            }
        }
    }
}
