package com.bloodnbonesgaming.topography.event;

import com.bloodnbonesgaming.topography.client.gui.GuiCreateWorldTopography;
import com.bloodnbonesgaming.topography.client.gui.GuiWorldSelectionOverride;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventSubscriber
{
    @SubscribeEvent
    public void onOpenGui(final GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld && Minecraft.getMinecraft().currentScreen instanceof GuiWorldSelection)
        {
        	event.setGui(new GuiCreateWorldTopography(Minecraft.getMinecraft().currentScreen));
            ConfigurationManager.setup();
            WorldTypeCustomizable.gui = (GuiCreateWorld) event.getGui();
            
            if (ConfigurationManager.getInstance().defaultWorldType())
            {
                for (int i = 0; i < WorldType.WORLD_TYPES.length; i++)
                {
                    if (WorldType.WORLD_TYPES[i] instanceof WorldTypeCustomizable)
                    {
                        ((GuiCreateWorld) event.getGui()).selectedIndex = i;
                        break;
                    }
                }
            }
        }
        if (event.getGui() instanceof GuiWorldSelection && !(event.getGui() instanceof GuiWorldSelectionOverride))
        {
        	event.setGui(new GuiWorldSelectionOverride(((GuiWorldSelection)event.getGui()).prevScreen));
        }
    }
}
