package com.bloodnbonesgaming.randomgenskyislands;

import java.io.File;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.FileHelper;
import com.bloodnbonesgaming.lib.util.script.ScriptUtil;
import com.bloodnbonesgaming.randomgenskyislands.config.ConfigurationManager;
import com.bloodnbonesgaming.randomgenskyislands.world.SkyIslandDataHandler;

public class IOHelper
{
//    public static ConfigurationManager loadConfig(final ConfigurationManager config)
//    {
//        return (ConfigurationManager) ScriptUtil.readScript(new File(ModInfo.MAIN_CONFIG_FILE), config, null);
//    }
    
    public static SkyIslandDataHandler loadDataHandler(final File file, final SkyIslandDataHandler handler, final Map<String, Class> classKeywords)
    {
        return (SkyIslandDataHandler) ScriptUtil.readScript(file, handler, classKeywords);
    }
    
    public static void readMainFile(final ConfigurationManager config, final Map<String, Class> classKeywords)
    {
        final File file = new File(ModInfo.MAIN_CONFIG_FILE);
        
        if (!file.exists())
        {
            FileHelper.copyDirectoryFromJar(IOHelper.class, "/defaultconfigs/", "./config/randomgenskyislands/");
        }
        ScriptUtil.readScript(file, config, classKeywords);
    }
}
