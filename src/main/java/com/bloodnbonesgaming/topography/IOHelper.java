package com.bloodnbonesgaming.topography;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.FileHelper;
import com.bloodnbonesgaming.lib.util.script.ScriptUtil;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.config.DimensionTypes;
import com.bloodnbonesgaming.topography.world.SkyIslandDataHandler;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;

public class IOHelper
{
    public static SkyIslandDataHandler loadDataHandler(final File file, final SkyIslandDataHandler handler, final Map<String, Class> classKeywords)
    {
        return (SkyIslandDataHandler) ScriptUtil.readScript(file, handler, classKeywords);
    }

    public static void readMainFile(final ConfigurationManager config, final Map<String, Class> classKeywords)
    {
        final File file = new File(ModInfo.MAIN_CONFIG_FILE);

        if (!file.exists())
        {
            FileHelper.copyDirectoryFromJar(IOHelper.class, "/defaultconfigs/", ModInfo.SCRIPT_FOLDER);
        }
        ScriptUtil.readScript(file, config, classKeywords);
    }
    
    public static Template loadStructureTemplate(final String name)
    {
        final File file = new File(ModInfo.STRUCTURE_FOLDER + name + ".nbt");
        
//        if (file.exists())
        {
            try
            {
                final FileInputStream stream = new FileInputStream(file);
                
                final NBTTagCompound nbt = CompressedStreamTools.readCompressed(stream);
                stream.close();
                
//                if (nbt != null)
                {
                    final Template template = new Template();
                    template.read(nbt);
                    
                    return template;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static DimensionDefinition loadDimensionDefinition(final String name, final String type)
    {
        DimensionDefinition definition = DimensionTypes.getDefinition(type);
        
        return (DimensionDefinition) ScriptUtil.readScript(new File(ModInfo.SCRIPT_FOLDER + name + ".txt"), definition, definition.classKeywords);
    }
}
