package com.bloodnbonesgaming.topography.client.gui.element;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bloodnbonesgaming.topography.Topography;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;

public abstract class GuiElementTexture extends GuiElementBase
{
    protected final ResourceLocation texture;
    protected int imageWidth;
    protected int imageHeight;
    protected int absRenderWidth = 0;
    protected int absRenderHeight = 0;
    protected double relRenderWidth = 0;
    protected double relRenderHeight = 0;
    
    public GuiElementTexture(final EnumGuiLocation location, final ResourceLocation texture)
    {
        super(location);
        this.texture = texture;
        int width = 0;
        int height = 0;
        
        try (IResource iresource = Minecraft.getInstance().getResourceManager().getResource(texture))
        {
            BufferedImage bufferedimage = ImageIO.read(iresource.getInputStream());
            width = bufferedimage.getWidth();
            height = bufferedimage.getHeight();
        }
        catch (IOException e)
        {
            Topography.getLog().error("Could not load texture for " + texture.toString());
        }
        this.imageWidth = width;
        this.imageHeight = height;
    }
    
    public GuiElementTexture(final EnumGuiLocation location, final ResourceLocation texture, final int imageWidth, final int imageHeight)
    {
        super(location);
        this.texture = texture;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    public void setAbsRenderWidth(final int width)
    {
        this.absRenderWidth = width;
    }
    
    public void setAbsRenderHeight(final int height)
    {
        this.absRenderHeight = height;
    }
    
    public void setAbsRender(final int width, final int height)
    {
        this.absRenderWidth = width;
        this.absRenderHeight = height;
    }
    
    public void setRelRenderWidth(final double width)
    {
        this.relRenderWidth = width;
    }
    
    public void setRelRenderHeight(final double height)
    {
        this.relRenderHeight = height;
    }
    
    public void setRelRender(final double width, final double height)
    {
        this.relRenderWidth = width;
        this.relRenderHeight = height;
    }
    
    public void render(final Minecraft minecraft, final int guiWidth, final int guiHeight)
    {
//        GlStateManager.disableLighting();
//        GlStateManager.disableFog();
        minecraft.getTextureManager().bindTexture(this.texture);
        int renderWidth = this.relRenderWidth != 0 ? (int)(this.relRenderWidth * guiWidth) : this.absRenderWidth;
        int renderHeight = this.relRenderHeight != 0 ? (int)(this.relRenderHeight * guiHeight) : this.absRenderHeight;
        int locX = this.location.getX(guiWidth, renderWidth) + this.absXOffset + (int)(guiWidth * this.relXOffset);
        int locY = this.location.getY(guiHeight, renderHeight) + this.absYOffset + (int)(guiHeight * this.relYOffset);
        this.drawTexture(locX, locY, this.imageWidth, this.imageHeight, renderWidth, renderHeight, 0.0F, 0.0F, (float)this.imageWidth, (float)this.imageHeight);
    }
    
    /*
     * xPos, yPos, texWidth, texHeight, width to render as, height to render as, tex positions
     */
    abstract void drawTexture(int x, int y, int texWidth, int texHeight, int width, int height, float texStartX, float texStartY, float texEndX, float texEndY);
}