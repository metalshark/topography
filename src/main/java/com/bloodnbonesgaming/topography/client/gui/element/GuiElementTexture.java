package com.bloodnbonesgaming.topography.client.gui.element;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
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
        
        IResource iresource = null;

        try
        {
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(texture);
            BufferedImage bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            width = bufferedimage.getWidth();
            height = bufferedimage.getHeight();
        }
        catch (IOException e)
        {
            Topography.instance.getLog().error("Could not load texture for " + texture.toString());
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
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
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        minecraft.getTextureManager().bindTexture(this.texture);
        this.drawTexture(this.location.getX(guiWidth, (int)(this.relRenderWidth * guiWidth)), this.location.getY(guiHeight, (int)(this.relRenderHeight * guiHeight)), this.imageWidth, this.imageHeight, (int)(this.relRenderWidth * guiWidth), (int)(this.relRenderHeight * guiHeight), 0.0, 0.0, (double)this.imageWidth, (double)this.imageHeight);
    }
    
    /*
     * xPos, yPos, texWidth, texHeight, width to render as, height to render as, tex positions
     */
    abstract void drawTexture(int x, int y, int texWidth, int texHeight, int width, int height, double texStartX, double texStartY, double texEndX, double texEndY);
//    double yStartPos = 0;
//    double xEndPos = width / this.tileXSize * 2;
//    double yEndPos = height / this.tileYSize * 2;
//    
//    Tessellator tessellator = Tessellator.getInstance();
//    BufferBuilder bufferbuilder = tessellator.getBuffer();
//    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
//    bufferbuilder.pos((double)(x + 0), (double)(y + height), 0.0).tex(xStartPos, yEndPos).endVertex();
//    bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0).tex(xEndPos, yEndPos).endVertex();
//    bufferbuilder.pos((double)(x + width), (double)(y + 0), 0.0).tex(xEndPos, yStartPos).endVertex();
//    bufferbuilder.pos((double)(x + 0), (double)(y + 0), 0.0).tex(xStartPos, yStartPos).endVertex();
//    tessellator.draw();
}