package com.bloodnbonesgaming.topography.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.WorldTypeSkyIslands;

import net.minecraft.world.WorldType;

public class ConfigurationManager {
    
    private final List<WorldType> worldTypes = new ArrayList<WorldType>();
    
    private static ConfigurationManager instance;
    
    public static ConfigurationManager getInstance()
    {
        return ConfigurationManager.instance;
    }
    
    public static void setup()
    {
        if (ConfigurationManager.instance != null)
        {
            ConfigurationManager.cleanUp();
        }
        ConfigurationManager.instance = new ConfigurationManager();
        ConfigurationManager.instance.readWorldTypes();
    }
    
    public static void cleanUp()
    {
        for (final WorldType type : ConfigurationManager.instance.worldTypes)
        {
            WorldType.WORLD_TYPES[type.getId()] = null;
        }
        ConfigurationManager.instance = null;
    }
    
    
    
    
    private void readWorldTypes()
    {
        IOHelper.readMainFile(this, null);
    }
    
    
    
    
    public void registerWorldType(final String name, final String script) throws Exception
    {
        if (name.length() > 16)
        {
            throw new Exception("WorldType names cannot be longer than 16 characters!");
        }
        final File file = new File(ModInfo.SCRIPT_FOLDER + script + ".txt");
        
        if (!file.exists())
        {
            throw new Exception("Script file `" + script + "` does not exist!");
        }
        
        this.worldTypes.add(new WorldTypeSkyIslands(name, file));
    }
}
