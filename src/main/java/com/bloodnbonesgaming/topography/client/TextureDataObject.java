package com.bloodnbonesgaming.topography.client;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class TextureDataObject {
	
	private int width = 16;
	private int height = 16;
	private final ResourceLocation location;
	
	public TextureDataObject(final ResourceLocation location)
	{
		this.location = location;
		
        try (IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(location);)
        {
            
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            
            if (bufferedimage != null)
            {
            	this.width = bufferedimage.getWidth();
            	this.height = bufferedimage.getHeight();
            	Topography.instance.getLog().info("Texture " + location + " " + this.height + " " + this.width);
            }

        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void bind()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
}
