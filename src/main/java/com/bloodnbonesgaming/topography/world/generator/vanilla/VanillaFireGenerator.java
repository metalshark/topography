package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.layer.GenLayer;

public class VanillaFireGenerator implements IGenerator
{
    private final WorldGenFire fireFeature = new WorldGenFire();

    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ)
    {
        
    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        
        for (int i1 = 0; i1 < rand.nextInt(rand.nextInt(10) + 1) + 1; ++i1)
        {
            this.fireFeature.generate(world, rand, blockpos.add(rand.nextInt(16) + 8, rand.nextInt(245) + 4, rand.nextInt(16) + 8));
        }
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

}
