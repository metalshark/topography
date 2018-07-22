package com.bloodnbonesgaming.topography.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiCustomizeWorldType extends GuiScreen
{
    private GuiButton done;
    final GuiCreateWorld parent;
    private GuiOptionsList list;
    private List<ConfigPreset> presets;
    
    public GuiCustomizeWorldType(GuiScreen parent)
    {
        this.parent = (GuiCreateWorld)parent;
    }
    
    @Override
    public void initGui()
    {
        ConfigurationManager.setup();
        this.presets = new ArrayList<ConfigPreset>(ConfigurationManager.getInstance().getPresets().values());
        
        this.done = this.addButton(new GuiButton(300, this.width / 2 + 98, this.height - 27, 90, 20, I18n.format("gui.done")));
        int distanceFromTopBottom = this.height / 5;
        int distanceFromLeft = this.width / 10;
        this.list = new GuiOptionsList(Minecraft.getMinecraft(), this.fontRenderer, this.width / 2 - distanceFromLeft, this.height - distanceFromTopBottom * 2, distanceFromTopBottom, this.height - distanceFromTopBottom, 25, this.width, this.height, this.presets);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 300:
                    this.parent.chunkProviderSettingsJson = presets.get(this.list.getIndex()).getName();
                    ConfigurationManager.getInstance().registerDimensions(presets.get(this.list.getIndex()).getName());
                    this.mc.displayGuiScreen(this.parent);
                    break;
            }
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.list.drawScreen(mouseX, mouseY, partialTicks);
    }
}
