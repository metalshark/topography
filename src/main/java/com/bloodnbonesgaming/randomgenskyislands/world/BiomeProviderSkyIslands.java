package com.bloodnbonesgaming.randomgenskyislands.world;

import com.bloodnbonesgaming.randomgenskyislands.world.layer.GenLayerBiomeSkyIslands;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderSkyIslands extends BiomeProvider {
    
    public BiomeProviderSkyIslands(final World world)
    {
        super();

        final GenLayer layer = new GenLayerBiomeSkyIslands(world.getSeed());
        layer.initWorldGenSeed(world.getSeed());
        this.genBiomes = layer;
        this.biomeIndexLayer = layer;
    }
}