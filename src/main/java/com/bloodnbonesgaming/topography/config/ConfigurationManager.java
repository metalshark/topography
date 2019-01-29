package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptDocumentationHandler;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

@ScriptClassDocumentation(documentationFile = ModInfo.DOCUMENTATION_FOLDER + "Topography", classExplaination = 
"This file is for options in the Topography.txt file. If Topography.txt does not exist, default configs and documentation will be printed.")
public class ConfigurationManager {
    
    private final List<WorldType> worldTypes = new ArrayList<WorldType>();

    final Map<String, ConfigPreset> presets = new LinkedHashMap<String, ConfigPreset>();
    private final Map<Integer, DimensionType> dimensionMapCopy = new HashMap<Integer, DimensionType>();
    private static final Map<Integer, DimensionType> dimensionTypes = new HashMap<Integer, DimensionType>();
    private final List<Integer> registeredDimensions = new ArrayList<Integer>();
    private final LockHandler locks = new LockHandler();
    
    private String generatorSettings = null;
    
    private boolean defaultWorldType = false;
    private boolean printDocumentation = true;

    private ExecutorService executor;
    
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
        ConfigurationManager.printDocumentation();
        ConfigurationManager.instance.loadLockHandler();
    }
    
    public static void printDocumentation()
    {
    	if (ConfigurationManager.instance.printDocumentation)
        {
        	Topography.instance.getLog().info("Printing documentation to " + ModInfo.DOCUMENTATION_FOLDER);
			ScriptDocumentationHandler.printAnnotatedDocumentation("com.bloodnbonesgaming.topography");
			ScriptDocumentationHandler.copyDocumentationFolder(ConfigurationManager.class, "./config/topography/documentation/");
        }
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
            if (ConfigurationManager.instance.executor != null)
            {
                ConfigurationManager.instance.executor.shutdown();
                try {
					ConfigurationManager.instance.executor.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					e.printStackTrace();
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
    
    @ScriptMethodDocumentation(args = "String", usage = "preset name", notes = "Creates a preset object and returns it.")
	public ConfigPreset registerPreset(final String name)
    {
        final ConfigPreset preset = new ConfigPreset(name, "");
        this.presets.put(name, preset);
        return preset;
    }
    
    @ScriptMethodDocumentation(args = "String, String", usage = "preset name, image location", notes = "Creates a preset object and returns it. Image references a file in the topography folder. Image may be null.")
	public ConfigPreset registerPreset(final String name, final String image)
    {
        final ConfigPreset preset = new ConfigPreset(name, image);
        this.presets.put(name, preset);
        return preset;
    }
    
    @ScriptMethodDocumentation(args = "String, String, String", usage = "preset name, image location, description", notes = "Creates a preset object and returns it. Image references a file in the topography folder. Image/description may be null.")
	public ConfigPreset registerPreset(final String name, final String image, final String description)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description);
        this.presets.put(name, preset);
        return preset;
    }
    
    @ScriptMethodDocumentation(args = "String, String, String, String", usage = "preset name, image location, description, WorldType", notes = "Creates a preset object and returns it. Image references a file in the topography folder. Setting a WorldType uses that WorldType as the base for the preset, allowing for things like using BoP for the overworld while having a custom nether dimension. Image/description/worldtype may be null.")
	public ConfigPreset registerPreset(final String name, final String image, final String description, final String worldType)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description, worldType);
        this.presets.put(name, preset);
        return preset;
    }
    
    @ScriptMethodDocumentation(args = "String, String, String, String, String", usage = "preset name, image location, description, WorldType, generator options", notes = "Creates a preset object and returns it. Image references a file in the topography folder. Setting a WorldType uses that WorldType as the base for the preset, allowing for things like using BoP for the overworld while having a custom nether dimension. Generator options sets the generator options for the chosen WorldType. Image/description/worldtype/generator options may be null.")
	public ConfigPreset registerPreset(final String name, final String image, final String description, final String worldType, final String generatorOptions)
    {
        final ConfigPreset preset = new ConfigPreset(name, image, description, worldType, generatorOptions);
        this.presets.put(name, preset);
        return preset;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Sets the Topography WorldType as default.")
	public void setAsDefaultWorldType()
    {
        this.defaultWorldType = true;
    }
    
    public boolean defaultWorldType()
    {
        return this.defaultWorldType;
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
    
    public LockHandler getLockHandler()
    {
        return this.locks;
    }
    
    private void loadLockHandler()
    {
        IOHelper.loadUnlockFile(this.locks);
    }
    
    public boolean unlockPreset(final String preset)
    {
        if (this.locks.unlock(preset))
        {
            IOHelper.saveUnlockFile(this.locks);
            return true;
        }
        return false;
    }
    
    public boolean lockPreset(final String preset)
    {
        if (this.locks.lock(preset))
        {
            IOHelper.saveUnlockFile(this.locks);
            return true;
        }
        return false;
    }
    
    public ExecutorService getExecutor()
    {
    	if (this.executor == null)
    	{
    		this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	}
    	return this.executor;
    }
    
    public void printDocumentation(final boolean bool)
    {
    	this.printDocumentation = bool;
    }
}
