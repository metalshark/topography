package com.bloodnbonesgaming.randomgenskyislands.config;

public class ConfigurationManager {
    
    private static ConfigurationManager instance;
    
    public static ConfigurationManager getInstance()
    {
        return ConfigurationManager.instance;
    }
    
    public static void setup()
    {
        ConfigurationManager.instance = new ConfigurationManager();
    }
    
    public static void cleanUp()
    {
        ConfigurationManager.instance = null;
    }
    
    
    
    
    
}
