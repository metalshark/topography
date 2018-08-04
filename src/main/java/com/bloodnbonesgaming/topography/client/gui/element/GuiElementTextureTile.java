package com.bloodnbonesgaming.topography.client.gui.element;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiElementTextureTile extends GuiElementTexture
{
    private double tileXSize;
    private double tileYSize;

    public GuiElementTextureTile(EnumGuiLocation location, ResourceLocation texture)
    {
        super(location, texture);
        this.tileXSize = this.imageWidth;
        this.tileYSize = this.imageHeight;
    }
    
    public void setTileSize(final double x, final double y)
    {
        this.tileXSize = x;
        this.tileYSize = y;
    }

    @Override
    protected void drawTexture(int x, int y, int texWidth, int texHeight, int width, int height, double texStartX, double texStartY, double texEndX, double texEndY)
    {
        double xStartPos = 0;
        double yStartPos = 0;
        double xEndPos = width / this.tileXSize * 2;
        double yEndPos = height / this.tileYSize * 2;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), 0.0).tex(xStartPos, yEndPos).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0).tex(xEndPos, yEndPos).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), 0.0).tex(xEndPos, yStartPos).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), 0.0).tex(xStartPos, yStartPos).endVertex();
        tessellator.draw();
    }
}