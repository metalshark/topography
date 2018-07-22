package com.bloodnbonesgaming.topography.config.definitions;

import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.world.chunkgenerator.ChunkGeneratorVoid;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class VoidDefinition extends DimensionDefinition
{

    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        return new BiomeProvider(world.getWorldInfo());
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world)
    {
        return new ChunkGeneratorVoid(world, world.getSeed(), this);
    }

}
