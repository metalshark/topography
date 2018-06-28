package com.bloodnbonesgaming.randomgenskyislands.world;

import java.io.File;

import com.bloodnbonesgaming.randomgenskyislands.IOHelper;
import com.bloodnbonesgaming.randomgenskyislands.world.layer.GenLayerBiomeSkyIslands;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderSkyIslands extends BiomeProvider {
    
    final SkyIslandDataHandler skyIslandData;
    
    public BiomeProviderSkyIslands(final World world, final File scriptFile)
    {
        super();
        
        this.skyIslandData = IOHelper.loadDataHandler(scriptFile, new SkyIslandDataHandler(), SkyIslandDataHandler.classKeywords);

        final GenLayer layer = new GenLayerBiomeSkyIslands(world.getSeed(), this.skyIslandData);
        layer.initWorldGenSeed(world.getSeed());
        this.genBiomes = layer;
        this.biomeIndexLayer = layer;
    }
    
    public SkyIslandDataHandler getHandler()
    {
        return this.skyIslandData;
    }
}