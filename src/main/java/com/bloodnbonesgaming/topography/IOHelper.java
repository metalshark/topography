package com.bloodnbonesgaming.topography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.bloodnbonesgaming.lib.util.FileHelper;
import com.bloodnbonesgaming.lib.util.script.ScriptUtil;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.config.LockHandler;
import com.bloodnbonesgaming.topography.util.FixedTemplate;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;

public class IOHelper
{
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
                    final Template template = new FixedTemplate();
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
    
    public static BufferedImage loadImage(final String name)
    {
        final File file = new File(ModInfo.SCRIPT_FOLDER + name + ".png");
        
        if (file.exists())
        {
            try
            {
//                final FileInputStream stream = new FileInputStream(file);
                return ImageIO.read(file);
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static boolean loadDimensionDefinition(final String name, final DimensionDefinition definition)
    {        
        return ScriptUtil.readScript(new File(ModInfo.SCRIPT_FOLDER + name + ".txt"), definition, definition.classKeywords);
    }
    
    public static boolean loadUnlockFile(final LockHandler handler)
    {
        final File file = new File(ModInfo.UNLOCK_FILE);
        
        if (!file.exists())
        {
            return false;
        }
        return ScriptUtil.readScript(new File(ModInfo.UNLOCK_FILE), handler, null);
    }
    
    public static void saveUnlockFile(final LockHandler handler)
    {
        final File file = new File(ModInfo.UNLOCK_FILE);
        
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
        }
        FileHelper.writeText(file, handler.getUnlocks());
    }
}
