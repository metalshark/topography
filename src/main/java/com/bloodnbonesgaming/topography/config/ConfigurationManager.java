package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class ConfigurationManager {
    
    private final List<WorldType> worldTypes = new ArrayList<WorldType>();

    final Map<String, ConfigPreset> presets = new LinkedHashMap<String, ConfigPreset>();
    private final Map<Integer, DimensionType> dimensionMapCopy = new HashMap<Integer, DimensionType>();
    private static final Map<Integer, DimensionType> dimensionTypes = new HashMap<Integer, DimensionType>();
    private final List<Integer> registeredDimensions = new ArrayList<Integer>();
    
    private String generatorSettings = null;
    
    private static ConfigurationManager instance;
    
    public static ConfigurationManager getInstance()
    {
        return ConfigurationManager.instance;
    }
    
    public static void setGeneratorSettings(final String settings)
    {
        ConfigurationManager.setup();
        ConfigurationManager.getInstance().generatorSettings = settings;
        Topography.instance.getLog().info("Settings: " + settings);
    }
    
    public String getGeneratorSettings()
    {
        return this.generatorSettings;
    }
    
    public static void setup()
    {
        ConfigurationManager.cleanUp();
        ConfigurationManager.instance = new ConfigurationManager();
        ConfigurationManager.instance.readWorldTypes();
    }
    
    public static void cleanUp()
    {
        Topography.instance.getLog().info("Cleaning up");
        if (ConfigurationManager.instance != null)
        {
            for (final WorldType type : ConfigurationManager.instance.worldTypes)
            {
                WorldType.WORLD_TYPES[type.getId()] = null;
            }
            
            for (final Integer id : ConfigurationManager.instance.registeredDimensions)
            {
                if (DimensionManager.isDimensionRegistered(id))
                {
                    if (!ConfigurationManager.instance.dimensionMapCopy.containsKey(id))
                    {
                        Topography.instance.getLog().info("Unregistering Dimension " + id);
                        DimensionManager.unregisterDimension(id);
                    }
                    else if (DimensionManager.getProviderType(id) != ConfigurationManager.instance.dimensionMapCopy.get(id))
                    {
                        Topography.instance.getLog().info("Unregistering Dimension " + id);
                        DimensionManager.unregisterDimension(id);
                        Topography.instance.getLog().info("Registering Dimension " + id);
                        DimensionManager.registerDimension(id, ConfigurationManager.instance.dimensionMapCopy.get(id));
                    }
                }
            }
            
            ConfigurationManager.instance = null;
        }
        Topography.instance.getLog().info("Done clean-up");
    }
    
    public void registerDimensions()
    {
        Topography.instance.getLog().info("Re-Registering dimensions");
        this.loadDimensionMap();
        final ConfigPreset preset = this.getPreset();
        
        if (preset != null)
        {
            Topography.instance.getLog().info("dim preset " + preset.getName());
            for (final int dimension : preset.getDimensions())
            {
                final DimensionType type;
                
                if (!ConfigurationManager.dimensionTypes.containsKey(dimension))
                {
                    type = DimensionType.register("DIM_" + dimension, "_DIM_" + dimension, dimension, WorldProviderConfigurable.class, dimension == 0);
                    ConfigurationManager.dimensionTypes.put(dimension, type);
                }
                else
                {
                    type = ConfigurationManager.dimensionTypes.get(dimension);
                }
                
                if (DimensionManager.isDimensionRegistered(dimension))
                {
                    Topography.instance.getLog().info("Unregistering Dimension " + dimension);
                    DimensionManager.unregisterDimension(dimension);
                }
                Topography.instance.getLog().info("Registering Dimension " + dimension);
                DimensionManager.registerDimension(dimension, type);
                this.registeredDimensions.add(dimension);
            }
        }
        Topography.instance.getLog().info("Done Re-Registering dimensions");
    }
    
    private void loadDimensionMap()
    {
        Topography.instance.getLog().info("Caching dimensions");
        for (final DimensionType type : DimensionType.values())
        {
            final int[] dimensions = DimensionManager.getDimensions(type);
            
            for (int i = 0; i < dimensions.length; i++)
            {
                Topography.instance.getLog().info("Caching Dimension " + dimensions[i]);
                this.dimensionMapCopy.put(dimensions[i], type);
            }
        }
        Topography.instance.getLog().info("Done caching dimensions");
    }
    
    
    
    
    private void readWorldTypes()
    {
        IOHelper.readMainFile(this, null);
    }
    
    
    public Map<String, ConfigPreset> getPresets()
    {
        return this.presets;
    }
    
    public ConfigPreset registerPreset(final String name, final String image)
    {
        final ConfigPreset preset = new ConfigPreset(name, image);
        this.presets.put(name, preset);
        return preset;
    }
    
    public ConfigPreset registerPreset(final String name, final String image, final String description)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description);
        this.presets.put(name, preset);
        return preset;
    }
    
    public ConfigPreset registerPreset(final String name, final String image, final String description, final String worldType)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description, worldType);
        this.presets.put(name, preset);
        return preset;
    }
    
    public ConfigPreset registerPreset(final String name, final String image, final String description, final String worldType, final String generatorOptions)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description, worldType, generatorOptions);
        this.presets.put(name, preset);
        return preset;
    }
    
//    public void registerWorldType(final String name, final String script) throws Exception
//    {
//        if (name.length() > 16)
//        {
//            throw new Exception("WorldType names cannot be longer than 16 characters!");
//        }
//        final File file = new File(ModInfo.SCRIPT_FOLDER + script + ".txt");
//        
//        if (!file.exists())
//        {
//            throw new Exception("Script file `" + script + "` does not exist!");
//        }
//        
//        this.worldTypes.add(new WorldTypeSkyIslands(name, file));
//    }
    
    public ConfigPreset getPreset()
    {
        ConfigPreset preset = this.presets.get(this.generatorSettings);
        
        if (preset == null)
        {
            for (final ConfigPreset value : this.presets.values())
            {
                preset = value;
                break;
            }
        }
        return preset;
    }
}
