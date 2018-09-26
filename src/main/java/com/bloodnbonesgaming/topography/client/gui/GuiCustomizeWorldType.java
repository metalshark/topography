package com.bloodnbonesgaming.topography.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.client.gui.element.EnumGuiLocation;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementText;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTexture;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTextureStretch;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;

public class GuiCustomizeWorldType extends GuiScreen
{
    private Random rand = new Random();
    private GuiButton done;
    private GuiButton random;
    final GuiCreateWorld parent;
    private GuiOptionsList list;
    private List<ConfigPreset> presets;
    private GuiElementTexture texture;
    private final List<GuiElementText> description = new ArrayList<GuiElementText>();
    
    public GuiCustomizeWorldType(GuiScreen parent)
    {
        this.parent = (GuiCreateWorld)parent;
    }
    
    @Override
    public void initGui()
    {
        ConfigurationManager.setup();
        this.presets = new ArrayList<ConfigPreset>(ConfigurationManager.getInstance().getPresets().values());
        
        this.done = this.addButton(new GuiButton(300, 98, this.height - 27, 90, 20, I18n.format("gui.done")));
        this.random = this.addButton(new GuiButton(301, 8, this.height - 27, 90, 20, "Random"));//TODO localize
        this.list = new GuiOptionsList(Minecraft.getMinecraft(), this.fontRenderer, (int) Math.ceil(this.width / 2.0), this.height - 50, 0, this.height - 50, 0, this.width, this.height, this.presets, this);
        this.onListSelected(this.presets.get(0));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 300:
                    this.parent.chunkProviderSettingsJson = "{\"Topography-Preset\":\"" + presets.get(this.list.getIndex()).getName() + "\"}";
                    this.mc.displayGuiScreen(this.parent);
                    break;
                case 301:
                    if (this.presets.size() > 1)
                    {
                        int index = this.list.getIndex();
                        
                        for (int i = 0; i < 10 && index == this.list.getIndex(); i++)
                        {
                            int randInt = this.rand.nextInt(this.presets.size());
                            
                            if (!this.presets.get(randInt).locked())
                            {
                                index = randInt;
                            }
                        }
                        this.list.elementClicked(index, false);
                    }
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
        for (final GuiElementText text : this.description)
        {
            text.render(this.fontRenderer, this.width, this.height);
        }
        if (this.texture != null)
        {
            this.texture.render(Minecraft.getMinecraft(), this.width, this.height);
        }
    }
    
    public void onListSelected(final ConfigPreset preset)
    {
        //Set description
        this.description.clear();
        final String description = preset.getDescription();
        
        if (description != null)
        {
            List<String> list = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(description, this.width / 2 - 5);
            int index = 0;
            int totalHeight = (list.size() + 1) * this.fontRenderer.FONT_HEIGHT;
            
            for (final String string : list)
            {
                index++;
                final GuiElementText text = new GuiElementText(EnumGuiLocation.CENTER, string);
                text.setAbsYOffset(this.fontRenderer.FONT_HEIGHT * index - totalHeight);
                text.setAbsXOffset(fontRenderer.getStringWidth(string) / 2 + 5);
                
                this.description.add(text);
            }
        }
        
        //Set texture
        this.texture = null;
        final String imageName = preset.getImage();
        
        if (!imageName.isEmpty())
        {
            final BufferedImage image = IOHelper.loadImage(imageName);
            
            if (image != null)
            {
                this.texture = new GuiElementTextureStretch(EnumGuiLocation.BOTTOM_RIGHT, this.mc.getTextureManager().getDynamicTextureLocation("presetImage", new DynamicTexture(image)), image.getWidth(), image.getHeight());
                this.texture.setRelRender(0.5, 0.5);
            }
        }
        //Lock
        this.done.enabled = !preset.locked();
    }
}
