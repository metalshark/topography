package com.bloodnbonesgaming.topography.client.gui.newstuff;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.bloodnbonesgaming.topography.client.gui.GuiCreateWorldTopography;
import com.bloodnbonesgaming.topography.client.gui.GuiScrollingList;
import com.bloodnbonesgaming.topography.config.ConfigPreset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiOptionsListNew extends GuiScrollingList
{
    final FontRenderer fontRenderer;
    private int selectedIndex = 0;
    final List<ConfigPreset> presets;
    final GuiCreateWorldTopography parent;

    public GuiOptionsListNew(Minecraft client, FontRenderer fontRenderer, int width, int height, int top, int bottom, int left, int screenWidth, int screenHeight, List<ConfigPreset> presets, GuiCreateWorldTopography parent)
    {
        super(client, width, height, top, bottom, left, fontRenderer.FONT_HEIGHT + 11, screenWidth, screenHeight);
        this.fontRenderer = fontRenderer;
        this.presets = presets;
        this.parent = parent;
    }
    
    public int getIndex()
    {
        return this.selectedIndex;
    }

    @Override
    protected int getSize()
    {
        return this.presets.size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick)
    {
        this.selectedIndex = index;
        this.parent.onListSelected(presets.get(index));
    }
    
    public void down()
    {
    	this.selectedIndex = this.selectedIndex + 1;
    	
    	if (scrollDistance < this.slotHeight * (this.selectedIndex + 1) - (this.bottom - this.top))
    	{
        	this.scrollDistance += this.slotHeight;
    	}
    			
    	if (this.presets.size() <= this.selectedIndex)
    	{
    		this.selectedIndex = 0;
    		this.scrollDistance = 0;
    	}
    	if (this.presets.get(this.selectedIndex).locked())
    	{
    		this.down();
    	}
    	else
    	{
        	this.elementClicked(this.selectedIndex, false);
    	}
    }
    
    public void up()
    {
    	this.selectedIndex = this.selectedIndex - 1;
    	if (scrollDistance > this.slotHeight * this.selectedIndex)
    	{
        	this.scrollDistance -= this.slotHeight;
    	}
		
    	if (this.selectedIndex < 0)
    	{
    		this.selectedIndex = this.presets.size() - 1;
    		this.scrollDistance = this.slotHeight * this.presets.size();
    	}
    	if (this.presets.get(this.selectedIndex).locked())
    	{
    		this.up();
    	}
    	else
    	{
        	this.elementClicked(this.selectedIndex, false);
    	}
    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == this.selectedIndex;
    }

    @Override
    protected void drawBackground()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        final String text = this.presets.get(slotIdx).getName();
        
        fontRenderer.drawStringWithShadow(text, this.left + 3, slotTop + 4, 0xFFFFFF);
        
        if (this.presets.get(slotIdx).locked())
        {
            fontRenderer.drawStringWithShadow("X", this.right - 15, slotTop + 4, 0xFF0000);
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();

        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.listWidth &&
                             mouseY >= this.top && mouseY <= this.bottom;
        int listLength     = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft  = scrollBarRight - scrollBarWidth;
        int entryLeft      = this.left;
        int entryRight     = scrollBarLeft - 1;
        int viewHeight     = this.bottom - this.top;
        int border         = 4;

        if (Mouse.isButtonDown(0))
        {
            if (this.initialMouseClickY == -1.0F)
            {
                if (isHovering)
                {
                    int mouseListY = mouseY - this.top - this.headerHeight + (int)this.scrollDistance - border;
                    int slotIndex = mouseListY / this.slotHeight;

                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength)
                    {
                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L);
                        this.selectedIndex = slotIndex;
                        this.lastClickTime = System.currentTimeMillis();
                    }
                    else if (mouseX >= entryLeft && mouseX <= entryRight && mouseListY < 0)
                    {
                        this.clickHeader(mouseX - entryLeft, mouseY - this.top + (int)this.scrollDistance - border);
                    }

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight)
                    {
                        this.scrollFactor = -1.0F;
                        int scrollHeight = this.getContentHeight() - viewHeight - border;
                        if (scrollHeight < 1) scrollHeight = 1;

                        int var13 = (int)((float)(viewHeight * viewHeight) / (float)this.getContentHeight());

                        if (var13 < 32) var13 = 32;
                        if (var13 > viewHeight - border*2)
                            var13 = viewHeight - border*2;

                        this.scrollFactor /= (float)(viewHeight - var13) / (float)scrollHeight;
                    }
                    else
                    {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickY = mouseY;
                }
                else
                {
                    this.initialMouseClickY = -2.0F;
                }
            }
            else if (this.initialMouseClickY >= 0.0F)
            {
                this.scrollDistance -= ((float)mouseY - this.initialMouseClickY) * this.scrollFactor;
                this.initialMouseClickY = (float)mouseY;
            }
        }
        else
        {
            this.initialMouseClickY = -1.0F;
        }

        this.applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        ScaledResolution res = new ScaledResolution(client);
        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(left      * scaleW), (int)(client.displayHeight - (bottom * scaleH)),
                       (int)(listWidth * scaleW), (int)(viewHeight * scaleH));

//        if (this.client.world != null)
//        {
            this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
//        }
//        else // Draw dark dirt background
//        {
//            GlStateManager.disableLighting();
//            GlStateManager.disableFog();
//            this.client.renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            final float scale = 32.0F;
//            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//            worldr.pos(this.left,  this.bottom, 0.0D).tex(this.left  / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.right, this.top,    0.0D).tex(this.right / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.left,  this.top,    0.0D).tex(this.left  / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            tess.draw();
//        }

        int baseY = this.top + border - (int)this.scrollDistance;

        if (this.hasHeader) {
            this.drawHeader(entryRight, baseY, tess);
        }

        for (int slotIdx = 0; slotIdx < listLength; ++slotIdx)
        {
            int slotTop = baseY + slotIdx * this.slotHeight + this.headerHeight;
            int slotBuffer = this.slotHeight - border;

            if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top)
            {
                if (this.highlightSelected && this.isSelected(slotIdx))
                {
                    int min = this.left;
                    int max = entryRight;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.disableTexture2D();
                    worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(min,     slotTop + slotBuffer + 2, 0).tex(0, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max,     slotTop + slotBuffer + 2, 0).tex(1, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(max,     slotTop              - 2, 0).tex(1, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min,     slotTop              - 2, 0).tex(0, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop + slotBuffer + 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, slotTop + slotBuffer + 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(max - 1, slotTop              - 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(min + 1, slotTop              - 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    GlStateManager.enableTexture2D();
                }

                this.drawSlot(slotIdx, entryRight, slotTop, slotBuffer, tess);
            }
        }

        GlStateManager.disableDepth();

        int extraHeight = (this.getContentHeight() + border) - viewHeight;
        if (extraHeight > 0)
        {
            int height = (viewHeight * viewHeight) / this.getContentHeight();

            if (height < 32) height = 32;

            if (height > viewHeight - border*2)
                height = viewHeight - border*2;

            int barTop = (int)this.scrollDistance * (viewHeight - height) / extraHeight + this.top;
            if (barTop < this.top)
            {
                barTop = this.top;
            }

            GlStateManager.disableTexture2D();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,  this.bottom, 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarRight, this.top,    0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,  this.top,    0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,  barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarRight, barTop,          0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,  barTop,          0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(scrollBarLeft,      barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarRight - 1, barTop,              0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            worldr.pos(scrollBarLeft,      barTop,              0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
        }

        this.drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}