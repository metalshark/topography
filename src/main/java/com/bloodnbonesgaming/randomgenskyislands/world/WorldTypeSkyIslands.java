package com.bloodnbonesgaming.randomgenskyislands.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeSkyIslands extends WorldType
{
    public WorldTypeSkyIslands()
    {
        super("customskyislands");
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
    
    @Override
    public int getSpawnFuzz(WorldServer world, MinecraftServer server)
    {
        return 0;
    }
}
