package com.bloodnbonesgaming.topography.world.biome.provider;

import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.layer.GenLayerSingle;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;

public class BiomeProviderConfigurable extends BiomeProvider
{
    private final DimensionDefinition definition;
    
    
    
    public BiomeProviderConfigurable(final World world, final DimensionDefinition definition)
    {
        super();
        this.definition = definition;

        final long seed = world.getSeed();
        final WorldType type = world.getWorldType();
        
        GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, type, this.settings);
        agenlayer = getModdedBiomeGenerators(type, seed, agenlayer);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
        
        for (final IGenerator generator : this.definition.getGenerators())
        {
            final GenLayer genLayer = generator.getLayer(world, this.genBiomes);
            final GenLayer genLayer2 = generator.getLayer(world, this.biomeIndexLayer);
            
            if (genLayer != null)
            {
                this.genBiomes = genLayer;
                this.biomeIndexLayer = genLayer2;
            }
        }
    }
    
    public BiomeProviderConfigurable(final World world, final int singleBiome, final DimensionDefinition definition)
    {
        super();
        this.definition = definition;
        
        GenLayer layer = new GenLayerSingle(world.getSeed(), singleBiome);
        layer.initWorldGenSeed(world.getSeed());
        
        for (final IGenerator generator : this.definition.getGenerators())
        {
            final GenLayer genLayer = generator.getLayer(world, layer);
            
            if (genLayer != null)
            {
                layer = genLayer;
            }
        }
        
        this.genBiomes = layer;
        this.biomeIndexLayer = layer;
    }
}
