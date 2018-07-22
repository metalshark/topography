package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.topography.world.StructureHandler;
import com.bloodnbonesgaming.topography.world.decorator.DecoratorScattered;
import com.bloodnbonesgaming.topography.world.generator.CellNoiseGenerator;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.LayerGenerator;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class DimensionDefinition
{
    public final Map<String, Class> classKeywords = new HashMap<String, Class>();
    private String spawnStructure;
    private Integer fogColor;
    private boolean enviromentalFog = false;
    private Float celestialAngle;
    private boolean renderSky = true;
    private final List<IGenerator> generators = new ArrayList<IGenerator>();
    private boolean skylight = true;
    
    private final StructureHandler structureHandler = new StructureHandler();
    
    public DimensionDefinition()
    {
        this.classKeywords.put("DecoratorScattered", DecoratorScattered.class);
        this.classKeywords.put("CellNoiseGenerator", CellNoiseGenerator.class);
        this.classKeywords.put("LayerGenerator", LayerGenerator.class);
    }
    
    public abstract BiomeProvider getBiomeProvider(final World world);
    public abstract IChunkGenerator getChunkGenerator(final World world);
    
    public void setSpawnStructure(final String structure)
    {
        this.spawnStructure = structure;
    }
    
    public String getSpawnStructure()
    {
        return this.spawnStructure;
    }
    
    public void setFogColor(final int color)
    {
        this.fogColor = color;
    }
    
    public Integer getFogColor()
    {
        return this.fogColor;
    }
    
    public void enableEnviromentalFog()
    {
        this.enviromentalFog = true;
    }
    
    public boolean renderEnviromentalFog()
    {
        return this.enviromentalFog;
    }
    
    public void setCelestialAngle(final float angle)
    {
        this.celestialAngle = angle;
    }
    
    public Float getCelestialAngle()
    {
        return this.celestialAngle;
    }
    
    public boolean renderSky()
    {
        return this.renderSky;
    }
    
    public void disableSky()
    {
        this.renderSky = false;
    }
    
    public StructureHandler getStructureHandler()
    {
        return this.structureHandler;
    }
    
    public void generateNetherFortress()
    {
        this.structureHandler.generateNetherFortress();
    }
    
    public void addGenerator(final IGenerator generator)
    {
        this.generators.add(generator);
    }
    
    public List<IGenerator> getGenerators()
    {
        return this.generators;
    }
    
    public void disableSkylight()
    {
        this.skylight = false;
    }
    
    public boolean skylight()
    {
        return this.skylight;
    }
}
