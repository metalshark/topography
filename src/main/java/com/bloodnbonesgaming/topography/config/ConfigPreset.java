package com.bloodnbonesgaming.topography.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigPreset
{
    private final String name;
    private final String image;
    private final String description;
    private final String worldType;
    private final String generatorSettings;
    
    private boolean enableHardcore;
    
    private final Map<Integer, String> scripts = new HashMap<Integer, String>();
    private boolean locked = false;
    
    public ConfigPreset(final String name, final String image)
    {
        this(name, image, null);
    }
    
    public ConfigPreset(final String name, final String image, final String description)
    {
        this(name, image, description, null, null);
    }
    
    public ConfigPreset(final String name, final String image, final String description, final String worldType)
    {
        this(name, image, description, worldType, null);
    }
    
    public ConfigPreset(final String name, final String image, final String description, final String worldType, final String generatorOptions)
    {
        this.name = name;
        this.image = image;
        this.description = description;
        this.worldType = worldType;
        this.generatorSettings = generatorOptions;
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
    
    public String getWorldType()
    {
        return this.worldType;
    }
    
    public String getGeneratorOptions()
    {
        return this.generatorSettings;
    }
    
    public boolean hardcore()
    {
        return this.enableHardcore;
    }
    
    public void enableHardcore()
    {
        this.enableHardcore = true;
    }
    
    public void lock()
    {
        this.locked = true;
    }
    
    public boolean locked()
    {
        return this.locked ? !ConfigurationManager.getInstance().getLockHandler().unlocked(this.name) : false;
    }
}
