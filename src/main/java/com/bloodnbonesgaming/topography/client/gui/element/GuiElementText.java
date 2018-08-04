package com.bloodnbonesgaming.topography.client.gui.element;

import net.minecraft.client.gui.FontRenderer;

public class GuiElementText extends GuiElementBase
{
    private final String text;
    private int color;
    
    public GuiElementText(EnumGuiLocation location, final String text, final int color)
    {
        super(location);
        this.text = text;
        this.color = color;
    }
    
    public GuiElementText(EnumGuiLocation location, final String text)
    {
        super(location);
        this.text = text;
        this.color = 16777215;
    }
    
    public void setColor(final int color)
    {
        this.color = color;
    }
    
    public void render(final FontRenderer fontRenderer, final int guiWidth, final int guiHeight)
    {
        final int width = fontRenderer.getStringWidth(this.text);
        final int height = fontRenderer.FONT_HEIGHT;
        
        fontRenderer.drawStringWithShadow(text, (float)(this.location.getX(guiWidth, width) + (this.relXOffset * guiWidth) + this.absXOffset), (float)(this.location.getY(guiHeight, height) + (this.relYOffset * guiWidth) + this.absYOffset), this.color);
    }
}