package com.bloodnbonesgaming.randomgenskyislands;

import java.io.File;

import com.bloodnbonesgaming.lib.util.script.ScriptUtil;
import com.bloodnbonesgaming.randomgenskyislands.config.ConfigurationManager;

public class IOHelper
{
    public static ConfigurationManager loadConfig(final ConfigurationManager config)
    {
        return (ConfigurationManager) ScriptUtil.readScript(new File(ModInfo.MAIN_CONFIG_FILE), config, null);
    }
}
