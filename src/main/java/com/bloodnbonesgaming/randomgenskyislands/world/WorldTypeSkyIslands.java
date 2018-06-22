package com.bloodnbonesgaming.randomgenskyislands.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeSkyIslands extends WorldType
{
    public WorldTypeSkyIslands()
    {
        super("Sky Islands");
    }
    
    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        return new BiomeProviderSkyIslands(world);
    }
    
    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkGeneratorSkyIslands(world);
    }

}
