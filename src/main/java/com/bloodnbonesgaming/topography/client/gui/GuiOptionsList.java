package com.bloodnbonesgaming.topography.client.gui;

import java.util.List;

import com.bloodnbonesgaming.topography.config.ConfigPreset;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiOptionsList extends GuiScrollingList
{
    final FontRenderer fontRenderer;
    private int selectedIndex = 0;
    final List<ConfigPreset> presets;
    final GuiCustomizeWorldType parent;

    public GuiOptionsList(Minecraft client, FontRenderer fontRenderer, int width, int height, int top, int bottom, int left, int screenWidth, int screenHeight, List<ConfigPreset> presets, GuiCustomizeWorldType parent)
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
        
        String trimmed = fontRenderer.trimStringToWidth(text, this.listWidth - 17);
        
        if (!trimmed.equals(text))
        {
            trimmed = trimmed.trim().concat("...");
        }
        
        fontRenderer.drawStringWithShadow(trimmed, this.left + 3, slotTop + 4, 0xFFFFFF);
    }
    
}