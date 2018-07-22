package com.bloodnbonesgaming.topography.world;

import java.io.File;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.config.definitions.SkyIslandDefinition;
import com.bloodnbonesgaming.topography.world.layer.GenLayerBiomeSkyIslands;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderSkyIslands extends BiomeProvider {
    
    final SkyIslandDefinition skyIslandData;
    
    public BiomeProviderSkyIslands(final World world, final SkyIslandDefinition definition)
    {
        super();
        
        this.skyIslandData = definition;
//        this.skyIslandData = IOHelper.loadDataHandler(scriptFile, new SkyIslandDataHandler(), SkyIslandDataHandler.classKeywords);

        final GenLayer layer = new GenLayerBiomeSkyIslands(world.getSeed(), this.skyIslandData);
        layer.initWorldGenSeed(world.getSeed());
        this.genBiomes = layer;
        this.biomeIndexLayer = layer;
    }
    
    public SkyIslandDefinition getHandler()
    {
        return this.skyIslandData;
    }
}