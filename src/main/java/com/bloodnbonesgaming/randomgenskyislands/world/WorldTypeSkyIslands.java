package com.bloodnbonesgaming.randomgenskyislands.world;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTypeSkyIslands extends WorldType
{
    private final File scriptFile;
    
    public WorldTypeSkyIslands(final String name, final File scriptFile)
    {
        super(name);
        this.scriptFile = scriptFile;
    }
    
    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        return new BiomeProviderSkyIslands(world, this.scriptFile);
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTranslationKey()
    {
        return this.getName();
    }
}
