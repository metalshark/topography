package com.bloodnbonesgaming.topography.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.gen.feature.template.Template;

public class IOHelper {
	
	public static Template loadStructureTemplate(final String name)
    {
        final File file = new File(ModInfo.CONFIG_FOLDER + name + ".nbt");
        
//        if (file.exists())
        {
            try (FileInputStream stream = new FileInputStream(file))
            {
                final CompoundNBT nbt = CompressedStreamTools.readCompressed(stream);
                stream.close();
                
//                if (nbt != null)
                {
                    final Template template = new Template();
                    template.read(nbt);
                    
                    return template;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static BufferedImage loadImage(final String name) {
        final File file = new File(ModInfo.CONFIG_FOLDER + name + ".png");
        
        if (file.exists()) {
            try {
                return ImageIO.read(file);
                
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static NativeImage loadNativeImage(final String name) {
        final File file = new File(ModInfo.CONFIG_FOLDER + name + ".png");
        
        if (file.exists()) {
            try (BufferedInputStream stream = FileHelper.openStreamReader(file)) {
                return NativeImage.read(stream);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
