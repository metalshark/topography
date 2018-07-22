package com.bloodnbonesgaming.topography.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.DimensionType;

public class ConfigPreset
{
    private final String name;
    private final String image;
    
    private final Map<Integer, ScriptData> scripts = new HashMap<Integer, ScriptData>();
    
    public ConfigPreset(final String name, final String image)
    {
        this.name = name;
        this.image = image;
    }
    
    public void registerDimension(final int dimension, final String script, final String type) throws Exception
    {
        final DimensionTypes dimensionType = DimensionTypes.getType(type);
        
        if (dimensionType == null)
        {
            throw new Exception("No DimensionType for '" + type + "'");
        }
        this.scripts.put(dimension, new ScriptData(script, type));
    }

    public String getName()
    {
        return name;
    }

    public ScriptData getScript(final int dimension)
    {
        return this.scripts.get(dimension);
    }
    
    public Set<Integer> getDimensions()
    {
        return this.scripts.keySet();
    }

    public String getImage()
    {
        return image;
    }
    
    
}
