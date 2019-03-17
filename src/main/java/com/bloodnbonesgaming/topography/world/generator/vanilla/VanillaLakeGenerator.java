package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaLakeGenerator", classExplaination = 
"This file is for the VanillaLakeGenerator. Generates vanilla lakes.")
public class VanillaLakeGenerator extends WorldGenerator implements IGenerator
{
    private final IBlockState state;
    private int minHeight = 0;
    private int maxHeight = 255;
    private List<Biome> biomeBlacklist = new ArrayList<Biome>();
    private int spawnChance = 4;

    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "block to fill with", notes = "This constructs a VanillaLakeGenerator. Generates lakes filled with the supplied block.")
	public VanillaLakeGenerator(ItemBlockData data) throws Exception
    {
        this.state = data.buildBlockState();
    }
    
    @ScriptMethodDocumentation(args = "int, int", usage = "min height, max height", notes = "Sets the minimum and maximum heights the generator will attempt to spawn in.")
	public void setHeight(final int min, int max)
    {
    	this.minHeight = min;
    	this.maxHeight = max;
    }
    
    @ScriptMethodDocumentation(usage = "biomes to blacklist", notes = "Adds biomes that the generator will not attempt to spawn in.")
	@ScriptArgs(args = ArgType.NON_NULL_BIOME_ID_ARRAY)
    public void blacklistBiome(final int[] biomes) throws Exception
    {
    	for (final int id : biomes)
    	{
    		final Biome biome = Biome.getBiome(id);
    		
    		if (biome == null)
    		{
    			throw new Exception(id + " is not a valid biome id.");
    		}
    		this.biomeBlacklist.add(biome);
    	}
    }
    
    @ScriptMethodDocumentation(args = "int", usage = "spawn chance", notes = "Sets the 1/chance per chunk for the generator to attempt to generate lakes.")
	public void setSpawnChance(final int chance)
    {
    	this.spawnChance = chance;
    }
    
    @Override
    public void populate(World world, int chunkX, int chunkZ, Random random) {
    	
    	int i = chunkX * 16;
        int j = chunkZ * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = world.getBiome(blockpos.add(16, 0, 16));
        
        for (final Biome blacklist : this.biomeBlacklist)
        {
        	if (blacklist == biome)
        	{
        		return;
        	}
        }
        
        if (random.nextInt(this.spawnChance) != 0)
        {
        	return;
        }
        
        int i1 = random.nextInt(16) + 8;
        int j1 = random.nextInt(this.maxHeight + 1) + this.minHeight;
        int k1 = random.nextInt(16) + 8;
        
    	this.generate(world, random, blockpos.add(i1, j1, k1));
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (position = position.add(-8, 0, -8); position.getY() > 5 && worldIn.isAirBlock(position); position = position.down())
        {
            ;
        }

        if (position.getY() <= 4)
        {
            return false;
        }
        else
        {
            position = position.down(4);
            boolean[] aboolean = new boolean[2048];
            int i = rand.nextInt(4) + 4;

            for (int j = 0; j < i; ++j)
            {
                double d0 = rand.nextDouble() * 6.0D + 3.0D;
                double d1 = rand.nextDouble() * 4.0D + 2.0D;
                double d2 = rand.nextDouble() * 6.0D + 3.0D;
                double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int l = 1; l < 15; ++l)
                {
                    for (int i1 = 1; i1 < 15; ++i1)
                    {
                        for (int j1 = 1; j1 < 7; ++j1)
                        {
                            double d6 = ((double)l - d3) / (d0 / 2.0D);
                            double d7 = ((double)j1 - d4) / (d1 / 2.0D);
                            double d8 = ((double)i1 - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D)
                            {
                                aboolean[(l * 16 + i1) * 8 + j1] = true;
                            }
                        }
                    }
                }
            }

            for (int k1 = 0; k1 < 16; ++k1)
            {
                for (int l2 = 0; l2 < 16; ++l2)
                {
                    for (int k = 0; k < 8; ++k)
                    {
                        boolean flag = !aboolean[(k1 * 16 + l2) * 8 + k] && (k1 < 15 && aboolean[((k1 + 1) * 16 + l2) * 8 + k] || k1 > 0 && aboolean[((k1 - 1) * 16 + l2) * 8 + k] || l2 < 15 && aboolean[(k1 * 16 + l2 + 1) * 8 + k] || l2 > 0 && aboolean[(k1 * 16 + (l2 - 1)) * 8 + k] || k < 7 && aboolean[(k1 * 16 + l2) * 8 + k + 1] || k > 0 && aboolean[(k1 * 16 + l2) * 8 + (k - 1)]);

                        if (flag)
                        {
                            Material material = worldIn.getBlockState(position.add(k1, k, l2)).getMaterial();

                            if (k >= 4 && material.isLiquid())
                            {
                                return false;
                            }

                            if (k < 4 && !material.isSolid() && worldIn.getBlockState(position.add(k1, k, l2)) != this.state)
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            for (int l1 = 0; l1 < 16; ++l1)
            {
                for (int i3 = 0; i3 < 16; ++i3)
                {
                    for (int i4 = 0; i4 < 8; ++i4)
                    {
                        if (aboolean[(l1 * 16 + i3) * 8 + i4])
                        {
                            worldIn.setBlockState(position.add(l1, i4, i3), i4 >= 4 ? Blocks.AIR.getDefaultState() : this.state, 2);
                        }
                    }
                }
            }

            for (int i2 = 0; i2 < 16; ++i2)
            {
                for (int j3 = 0; j3 < 16; ++j3)
                {
                    for (int j4 = 4; j4 < 8; ++j4)
                    {
                        if (aboolean[(i2 * 16 + j3) * 8 + j4])
                        {
                            BlockPos blockpos = position.add(i2, j4 - 1, j3);

                            if (worldIn.getBlockState(blockpos).getBlock() == Blocks.DIRT && worldIn.getLightFor(EnumSkyBlock.SKY, position.add(i2, j4, j3)) > 0)
                            {
                                Biome biome = worldIn.getBiome(blockpos);

                                if (biome.topBlock.getBlock() == Blocks.MYCELIUM)
                                {
                                    worldIn.setBlockState(blockpos, Blocks.MYCELIUM.getDefaultState(), 2);
                                }
                                else
                                {
                                    worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }

            if (state.getMaterial() == Material.LAVA)
            {
                for (int j2 = 0; j2 < 16; ++j2)
                {
                    for (int k3 = 0; k3 < 16; ++k3)
                    {
                        for (int k4 = 0; k4 < 8; ++k4)
                        {
                            boolean flag1 = !aboolean[(j2 * 16 + k3) * 8 + k4] && (j2 < 15 && aboolean[((j2 + 1) * 16 + k3) * 8 + k4] || j2 > 0 && aboolean[((j2 - 1) * 16 + k3) * 8 + k4] || k3 < 15 && aboolean[(j2 * 16 + k3 + 1) * 8 + k4] || k3 > 0 && aboolean[(j2 * 16 + (k3 - 1)) * 8 + k4] || k4 < 7 && aboolean[(j2 * 16 + k3) * 8 + k4 + 1] || k4 > 0 && aboolean[(j2 * 16 + k3) * 8 + (k4 - 1)]);

                            if (flag1 && (k4 < 4 || rand.nextInt(2) != 0) && worldIn.getBlockState(position.add(j2, k4, k3)).getMaterial().isSolid())
                            {
                                worldIn.setBlockState(position.add(j2, k4, k3), Blocks.STONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            if (state.getMaterial() == Material.WATER)
            {
                for (int k2 = 0; k2 < 16; ++k2)
                {
                    for (int l3 = 0; l3 < 16; ++l3)
                    {
                        if (worldIn.canBlockFreezeWater(position.add(k2, 4, l3)))
                        {
                            worldIn.setBlockState(position.add(k2, 4, l3), Blocks.ICE.getDefaultState(), 2| 16); //Forge
                        }
                    }
                }
            }

            return true;
        }
    }
}