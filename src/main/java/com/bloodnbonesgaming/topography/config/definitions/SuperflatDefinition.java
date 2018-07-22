package com.bloodnbonesgaming.topography.config.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.world.chunkgenerator.ChunkGeneratorSuperflat;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class SuperflatDefinition extends DimensionDefinition
{
    private final Map<MinMaxBounds, IBlockState> layers = new LinkedHashMap<MinMaxBounds, IBlockState>();
    
    public void addLayer(final MinMaxBounds bounds, final ItemBlockData block) throws Exception
    {
        this.layers.put(bounds, block.buildBlockState());
    }
    
    public Map<MinMaxBounds, IBlockState> getLayers()
    {
        return this.layers;
    }
    
    

    @Override
    public BiomeProvider getBiomeProvider(World world)
    {
        return new BiomeProvider(world.getWorldInfo());
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world)
    {
        return new ChunkGeneratorSuperflat(world, world.getSeed(), this);
    }

}
