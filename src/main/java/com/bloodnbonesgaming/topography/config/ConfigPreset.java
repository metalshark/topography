package com.bloodnbonesgaming.topography.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigPreset
{
    private final String name;
    private final String image;
    private final String description;
    
    private final Map<Integer, String> scripts = new HashMap<Integer, String>();
    
    public ConfigPreset(final String name, final String image)
    {
        this(name, image, null);
    }
    
    public ConfigPreset(final String name, final String image, final String description)
    {
        this.name = name;
        this.image = image;
        this.description = description;
    }
    
    public void registerDimension(final int dimension, final String script) throws Exception
    {
        this.scripts.put(dimension, script);
    }

    public String getName()
    {
        return name;
    }

    public String getScript(final int dimension)
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
    
    public String getDescription()
    {
        return this.description;
    }
}
